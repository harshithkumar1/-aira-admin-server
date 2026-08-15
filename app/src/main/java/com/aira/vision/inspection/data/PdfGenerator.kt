package com.aira.vision.inspection.data

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.aira.vision.inspection.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {

    private const val TAG = "PdfGenerator"

    data class PdfData(
        val clientName: String,
        val project: String,
        val inspectedBy: String,
        val inspectionDate: String,
        val dimensions: List<DimensionData>,
        val uploadedImages: Map<String, String>,
        val sections: List<SnagSection>
    )

    data class PdfResult(
        val file: File?,
        val uri: Uri?,
        val path: String
    )

    // ===== CONSTANTS =====
    private const val PAGE_W = 595
    private const val PAGE_H = 842
    private const val MARGIN = 35f
    private const val RIGHT = PAGE_W - MARGIN
    private const val CONTENT_W = RIGHT - MARGIN
    private const val MAX_ROWS_PER_PAGE = 3

    // Snag table column positions (relative to MARGIN)
    private const val COL_SNO_W = 50f       // S.No column width
    private const val COL_PROOF_W = 200f    // Proof column width (FIX #3: was 130, now 200)
    private const val COL_DESC_W = CONTENT_W - COL_SNO_W - COL_PROOF_W

    // Row height for snag rows (FIX #3: increased for larger image box)
    private const val ROW_H = 170f

    // Image bounding box (FIX #3: was 120x100, now 200x150)
    private const val IMG_BOX_W = 200f
    private const val IMG_BOX_H = 150f

    // Colors
    private const val C_NAVY = "#0B1D3A"
    private const val C_ORANGE = "#E8630A"
    private const val C_DARK = "#1B2A4A"
    private const val C_GRAY = "#6B7280"
    private const val C_LIGHT_GRAY = "#9CA3AF"
    private const val C_BORDER = "#D1D5DB"
    private const val C_BG_ROW = "#F8FAFC"
    private const val C_TABLE_HDR_BG = "#F1F5F9"
    private const val C_WHITE = "#FFFFFF"
    private const val C_RED = "#DC2626"
    private const val C_GREEN = "#059669"
    private const val C_WARN_BG = "#FEF3C7"

    fun generate(context: Context, data: PdfData): PdfResult? {
        val doc = PdfDocument()
        var pgNum = 0
        var page: PdfDocument.Page? = null
        var cv: Canvas? = null
        var y = 0f

        fun color(hex: String) = Color.parseColor(hex)

        fun paint(size: Float, clr: String, bold: Boolean = false, antialias: Boolean = true): Paint {
            return Paint().apply {
                color = color(clr)
                textSize = size
                isAntiAlias = antialias
                if (bold) typeface = Typeface.DEFAULT_BOLD
            }
        }

        fun newPage(drawHeader: Boolean = true) {
            if (page != null) doc.finishPage(page!!)
            pgNum++
            page = doc.startPage(PdfDocument.PageInfo.Builder(PAGE_W, PAGE_H, pgNum).create())
            cv = page!!.canvas
            if (drawHeader) {
                // Navy header bar with title
                cv!!.drawRect(0f, 0f, PAGE_W.toFloat(), 38f, Paint().apply { this.color = color(C_NAVY) })
                cv!!.drawText("Home Inspection Report", MARGIN, 26f, paint(13f, C_WHITE, bold = true))
                // Orange separator line below header
                cv!!.drawLine(0f, 38f, PAGE_W.toFloat(), 38f, Paint().apply { this.color = color(C_ORANGE); strokeWidth = 2f })
                y = 60f  // More spacing below header
            } else {
                y = 0f
            }
        }

        fun footer() {
            val fy = PAGE_H - 18f
            cv!!.drawLine(MARGIN, fy - 4f, RIGHT, fy - 4f, Paint().apply { this.color = color(C_BORDER); strokeWidth = 0.5f })
            cv!!.drawText("AIRA Vision Pvt. Ltd.", MARGIN, fy + 4f, paint(7f, C_LIGHT_GRAY))
            // Orange decorative stripe at bottom
            cv!!.drawRect(0f, PAGE_H.toFloat() - 6f, PAGE_W.toFloat(), PAGE_H.toFloat(), Paint().apply { this.color = color(C_ORANGE) })
        }

        fun need(need: Float) = y + need > PAGE_H - 30f

        fun wrap(text: String, x: Float, startY: Float, maxW: Float, p: Paint): Float {
            var cy = startY
            for (line in text.split("\n")) {
                var cur = ""
                for (w in line.split(" ")) {
                    val test = if (cur.isEmpty()) w else "$cur $w"
                    if (p.measureText(test) > maxW) {
                        cv!!.drawText(cur, x, cy, p)
                        cur = w
                        cy += p.textSize + 3.5f
                    } else cur = test
                }
                if (cur.isNotEmpty()) {
                    cv!!.drawText(cur, x, cy, p)
                    cy += p.textSize + 3.5f
                }
            }
            return cy
        }

        // ===== ROOM HEADER + TABLE HEADER (FIX #4: better typography, centered title) =====
        fun drawRoomHeader(roomTitle: String) {
            // Room title - navy background block, centered text
            val titleH = 26f
            cv!!.drawRect(MARGIN, y, RIGHT, y + titleH, Paint().apply { this.color = color(C_NAVY) })
            val titleP = paint(15f, C_WHITE, bold = true)
            val titleW = titleP.measureText(roomTitle.uppercase())
            val titleX = MARGIN + (CONTENT_W - titleW) / 2f
            cv!!.drawText(roomTitle.uppercase(), titleX, y + 18f, titleP)
            y += titleH + 6f

            // Table header row - light gray bg with bold text and clear borders
            val hdrTop = y
            val hdrBot = y + 22f
            cv!!.drawRect(MARGIN, hdrTop, RIGHT, hdrBot, Paint().apply { this.color = color(C_TABLE_HDR_BG) })

            // Top and bottom borders
            cv!!.drawLine(MARGIN, hdrTop, RIGHT, hdrTop, Paint().apply { this.color = color(C_BORDER); strokeWidth = 1f })
            cv!!.drawLine(MARGIN, hdrBot, RIGHT, hdrBot, Paint().apply { this.color = color(C_BORDER); strokeWidth = 1f })

            val hdrP = paint(11f, C_DARK, bold = true)
            cv!!.drawText("S.no", MARGIN + 14f, hdrTop + 15f, hdrP)
            cv!!.drawText("Issue Description", MARGIN + COL_SNO_W + 10f, hdrTop + 15f, hdrP)
            cv!!.drawText("Proof", RIGHT - COL_PROOF_W + 60f, hdrTop + 15f, hdrP)

            // Column dividers
            cv!!.drawLine(MARGIN + COL_SNO_W, hdrTop, MARGIN + COL_SNO_W, hdrBot, Paint().apply { this.color = color(C_BORDER); strokeWidth = 1f })
            cv!!.drawLine(RIGHT - COL_PROOF_W, hdrTop, RIGHT - COL_PROOF_W, hdrBot, Paint().apply { this.color = color(C_BORDER); strokeWidth = 1f })

            y = hdrBot + 2f
        }

        // ===== DRAW SINGLE ROW (FIX #3: 200x150 image box, FIX #4: better borders/line-height) =====
        fun drawRow(sno: Int, desc: String, photoPath: String?, rowIdx: Int) {
            val rowTop = y
            val rowBot = y + ROW_H

            // Row background (alternating)
            if (rowIdx % 2 == 0) {
                cv!!.drawRect(MARGIN, rowTop, RIGHT, rowBot, Paint().apply { this.color = color(C_BG_ROW) })
            }

            // Outer cell border
            cv!!.drawRect(MARGIN, rowTop, RIGHT, rowBot, Paint().apply {
                this.color = color(C_BORDER); style = Paint.Style.STROKE; strokeWidth = 1f
            })

            // S.No column divider
            cv!!.drawLine(MARGIN + COL_SNO_W, rowTop, MARGIN + COL_SNO_W, rowBot, Paint().apply {
                this.color = color(C_BORDER); strokeWidth = 1f
            })

            // Proof column divider
            cv!!.drawLine(RIGHT - COL_PROOF_W, rowTop, RIGHT - COL_PROOF_W, rowBot, Paint().apply {
                this.color = color(C_BORDER); strokeWidth = 1f
            })

            // S.No - orange badge centered in column
            val badgeCx = MARGIN + COL_SNO_W / 2f
            cv!!.drawRoundRect(badgeCx - 15f, rowTop + ROW_H / 2f - 12f, badgeCx + 15f, rowTop + ROW_H / 2f + 12f, 4f, 4f, Paint().apply { this.color = color(C_ORANGE) })
            val snoP = paint(11f, C_WHITE, bold = true)
            val snoW = snoP.measureText("$sno")
            cv!!.drawText("$sno", badgeCx - snoW / 2f, rowTop + ROW_H / 2f + 4f, snoP)

            // Description - wrapped text with more line height
            val descP = paint(10.5f, C_DARK)
            val descX = MARGIN + COL_SNO_W + 10f
            val descMaxW = COL_DESC_W - 20f
            wrap(desc, descX, rowTop + 14f, descMaxW, descP)

            // Photo / Placeholder (FIX #3: 200x150 bounding box)
            val phX = RIGHT - COL_PROOF_W + 8f
            val phY = rowTop + (ROW_H - IMG_BOX_H) / 2f

            if (!photoPath.isNullOrEmpty()) {
                try {
                    val file = File(photoPath)
                    if (file.exists()) {
                        val bmp = BitmapFactory.decodeFile(file.absolutePath)
                        if (bmp != null) {
                            // Scale to fit within box maintaining aspect ratio (object-fit: contain)
                            val scale = minOf(IMG_BOX_W / bmp.width, IMG_BOX_H / bmp.height)
                            val dstW = bmp.width * scale
                            val dstH = bmp.height * scale
                            val dstX = phX + (IMG_BOX_W - dstW) / 2f
                            val dstY = phY + (IMG_BOX_H - dstH) / 2f
                            cv!!.drawBitmap(bmp, null, RectF(dstX, dstY, dstX + dstW, dstY + dstH), null)
                            bmp.recycle()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Photo error: $photoPath", e)
                }
            }
            // Always draw the bounding box border (whether photo exists or not)
            cv!!.drawRect(phX, phY, phX + IMG_BOX_W, phY + IMG_BOX_H, Paint().apply {
                this.color = color(C_BORDER); style = Paint.Style.STROKE; strokeWidth = 1f
            })
            if (photoPath.isNullOrEmpty()) {
                cv!!.drawText("No Photo", phX + (IMG_BOX_W - paint(9f, C_LIGHT_GRAY).measureText("No Photo")) / 2f, phY + IMG_BOX_H / 2f + 3f, paint(9f, C_LIGHT_GRAY))
            }

            y = rowBot
        }

        // ===== SECTION HEADER (FIX #4: better visual hierarchy) =====
        fun drawSectionTitle(title: String) {
            cv!!.drawRect(MARGIN, y, RIGHT, y + 28f, Paint().apply { this.color = color(C_NAVY) })
            val tp = paint(15f, C_WHITE, bold = true)
            val tw = tp.measureText(title)
            cv!!.drawText(title, MARGIN + (CONTENT_W - tw) / 2f, y + 19f, tp)
            y += 36f
        }

        try {
            // =============================================
            // PAGE 1: COVER (with logo, hero image, decorative waves)
            // =============================================
            newPage(drawHeader = false)

            // --- Decorative wave shapes (top-left corner) ---
            val wavePath = Path()
            wavePath.moveTo(0f, 0f)
            wavePath.lineTo(180f, 0f)
            wavePath.quadTo(120f, 60f, 0f, 100f)
            wavePath.close()
            cv!!.drawPath(wavePath, Paint().apply { this.color = color(C_ORANGE) })

            val wavePath2 = Path()
            wavePath2.moveTo(0f, 0f)
            wavePath2.lineTo(120f, 0f)
            wavePath2.quadTo(80f, 40f, 0f, 70f)
            wavePath2.close()
            cv!!.drawPath(wavePath2, Paint().apply { this.color = color("#1E3A8A") })

            // --- AIRA Full Logo ---
            try {
                val logoBmp = BitmapFactory.decodeResource(context.resources, R.drawable.aira_fulllogo)
                if (logoBmp != null) {
                    val logoW = 320f
                    val logoH = logoW * logoBmp.height / logoBmp.width
                    val logoX = (PAGE_W - logoW) / 2f
                    cv!!.drawBitmap(logoBmp, null, RectF(logoX, 30f, logoX + logoW, 30f + logoH), null)
                    logoBmp.recycle()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Logo load failed", e)
                // Fallback: draw text logo
                cv!!.drawText("AIRA", PAGE_W / 2f - 60f, 80f, paint(36f, C_NAVY, bold = true))
                cv!!.drawText("VISION PVT. LTD.", PAGE_W / 2f - 80f, 110f, paint(14f, C_NAVY))
            }

            // --- Service bar ---
            y = 170f
            cv!!.drawRect(0f, y, PAGE_W.toFloat(), y + 28f, Paint().apply { this.color = color(C_NAVY) })
            cv!!.drawText("Project Consultancy  |  Home Inspections  |  Multistage Inspections  |  Interiors", MARGIN, y + 19f, paint(9f, C_WHITE, bold = true))
            y += 36f

            // --- House Hero Image ---
            try {
                val heroBmp = BitmapFactory.decodeResource(context.resources, R.drawable.house_hero)
                if (heroBmp != null) {
                    val heroW = PAGE_W.toFloat()
                    val heroH = 260f
                    cv!!.drawBitmap(heroBmp, null, RectF(0f, y, heroW, y + heroH), null)
                    y += heroH
                } else {
                    y += 260f
                }
            } catch (e: Exception) {
                Log.e(TAG, "Hero image load failed", e)
                y += 260f
            }

            // --- Decorative line below image ---
            cv!!.drawRect(0f, y, PAGE_W.toFloat(), y + 3f, Paint().apply { this.color = color(C_ORANGE) })
            y += 16f

            // --- Metadata Grid (two columns) ---
            val projectLines = data.project.split("\n")
            val siteName = projectLines.getOrElse(0) { data.project }
            val towerFlat = projectLines.getOrElse(1) { "" }

            // Left column
            cv!!.drawText("Site Name:", MARGIN, y, paint(11f, C_DARK, bold = true))
            cv!!.drawText(siteName, MARGIN + 90f, y, paint(12f, C_DARK))
            y += 22f

            if (towerFlat.isNotEmpty()) {
                cv!!.drawText(towerFlat, MARGIN + 90f, y, paint(12f, C_DARK))
                y += 22f
            }

            y += 6f
            cv!!.drawText("Client Name :", MARGIN, y, paint(11f, C_DARK, bold = true))
            cv!!.drawText(data.clientName, MARGIN + 90f, y, paint(12f, C_DARK))
            y += 28f

            cv!!.drawText("Inspected By:", MARGIN, y, paint(11f, C_DARK, bold = true))
            val inspectorLines = data.inspectedBy.split("\n")
            cv!!.drawText(inspectorLines.getOrElse(0) { data.inspectedBy }, MARGIN + 90f, y, paint(12f, C_DARK))

            // Right column
            val rightCol = MARGIN + 300f
            var ry = y - 50f
            cv!!.drawText("Date of Inspection:", rightCol, ry, paint(11f, C_DARK, bold = true))
            cv!!.drawText(data.inspectionDate, rightCol + 130f, ry, paint(12f, C_DARK))
            ry += 28f
            cv!!.drawText("Mobile Number:", rightCol, ry, paint(11f, C_DARK, bold = true))
            cv!!.drawText(inspectorLines.getOrElse(1) { "" }, rightCol + 105f, ry, paint(12f, C_DARK))

            // --- Decorative wave shapes (bottom) ---
            val bottomWave = Path()
            bottomWave.moveTo(0f, PAGE_H.toFloat())
            bottomWave.lineTo(PAGE_W.toFloat(), PAGE_H.toFloat())
            bottomWave.lineTo(PAGE_W.toFloat(), PAGE_H.toFloat() - 60f)
            bottomWave.quadTo(PAGE_W * 0.7f, PAGE_H.toFloat() - 30f, 0f, PAGE_H.toFloat() - 80f)
            bottomWave.close()
            cv!!.drawPath(bottomWave, Paint().apply { this.color = color(C_ORANGE) })

            val bottomWave2 = Path()
            bottomWave2.moveTo(0f, PAGE_H.toFloat())
            bottomWave2.lineTo(PAGE_W.toFloat(), PAGE_H.toFloat())
            bottomWave2.lineTo(PAGE_W.toFloat(), PAGE_H.toFloat() - 35f)
            bottomWave2.quadTo(PAGE_W * 0.5f, PAGE_H.toFloat() - 10f, 0f, PAGE_H.toFloat() - 50f)
            bottomWave2.close()
            cv!!.drawPath(bottomWave2, Paint().apply { this.color = color("#1E3A8A") })

            footer()
            // =============================================

            // =============================================
            // SUMMARY PAGES (with logo and styled title bar)
            // =============================================
            newPage()

            // SUMMARY title bar with navy bg + orange accent stripe
            val sumBarH = 32f
            cv!!.drawRect(MARGIN, y, RIGHT, y + sumBarH, Paint().apply { this.color = color(C_NAVY) })
            // Orange accent stripe on left
            cv!!.drawRect(MARGIN, y, MARGIN + 8f, y + sumBarH, Paint().apply { this.color = color(C_ORANGE) })
            cv!!.drawText("SUMMARY", MARGIN + 18f, y + 22f, paint(16f, C_WHITE, bold = true))

            // AIRA logo to the RIGHT of the SUMMARY bar (same Y position)
            try {
                val logoBmp = BitmapFactory.decodeResource(context.resources, R.drawable.aira_logo)
                if (logoBmp != null) {
                    val logoW = 70f
                    val logoH = logoW * logoBmp.height / logoBmp.width
                    val logoY = y + (sumBarH - logoH) / 2f  // Center vertically with bar
                    cv!!.drawBitmap(logoBmp, null, RectF(RIGHT - logoW - 4f, logoY, RIGHT - 4f, logoY + logoH), null)
                    logoBmp.recycle()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Logo load failed on summary page", e)
            }

            y += sumBarH + 14f

            // Subtitle in orange
            cv!!.drawText("SUMMARY OF OBSERVATIONS & RECOMMENDATIONS", MARGIN, y, paint(12f, C_ORANGE, bold = true))
            y += 18f

            y = wrap("The flat inspection identified multiple finishing, workmanship, alignment, installation, and incomplete-work observations across the inspected areas. The recurring issues are primarily related to flooring and wall finishes, doors and windows, plumbing and sanitary installations, electrical finishing, and incomplete works.", MARGIN, y, CONTENT_W, paint(10f, C_DARK))
            y += 14f

            data class Cat(val title: String, val obs: List<String>, val rec: String)

            val cats = listOf(
                Cat("1. Flooring & Tiling Observations:", listOf(
                    "Tile hollowness observed at multiple locations.",
                    "Tile undulations and offsets observed.",
                    "Tile chip-offs/damaged tiles identified.",
                    "Gaps observed between flooring and skirting.",
                    "Skirting/groove alignment requires correction.",
                    "Scratches and shade variations observed on finished surfaces."
                ), "Affected areas should be identified through detailed inspection and the defective/hollow tiles should be re-fixed or replaced as required. Tile alignment, levels, joints, skirting and grout finishing should be corrected to achieve a uniform and properly finished surface."),

                Cat("2. Walls & Paint Finishing Observations:", listOf(
                    "Wall undulations observed.",
                    "Second coat of paint remains pending in certain areas.",
                    "Shade variations observed.",
                    "Damage/breakage observed at wall edges."
                ), "Wall surfaces should be properly prepared, repaired and finished before repainting. The pending second coat should be completed and shade consistency should be maintained throughout the affected areas."),

                Cat("3. Doors & Frames Observations:", listOf(
                    "Door frame damage observed at multiple locations.",
                    "Door shutter and frame alignment issues identified.",
                    "Gaps observed around door frames.",
                    "Door polish/finishing is not satisfactory.",
                    "Hinges and door accessories are not properly fixed.",
                    "Fastener caps are missing/pending.",
                    "Main door eye piece is missing."
                ), "Damaged frames should be repaired/replaced as appropriate. Door shutters and frames should be properly aligned and checked for smooth operation. Hinges and accessories should be securely fixed, fastener caps installed, and all gaps and finishing defects rectified. The main door eye piece should also be installed."),

                Cat("4. Windows & UPVC Works Observations:", listOf(
                    "Stains and mortar residue observed on UPVC windows.",
                    "Scratches observed on window surfaces/glass.",
                    "Window tracks require cleaning.",
                    "Window fastener caps are missing.",
                    "Protruding/sharp fasteners observed.",
                    "Sealant gaps observed.",
                    "Damaged window mesh observed.",
                    "Damaged rubber gasket observed in Bathroom 2."
                ), "All windows should be thoroughly cleaned and inspected. Mortar residue should be carefully removed without damaging the UPVC finish. Protruding fasteners should be properly secured and made safe. Missing fastener caps should be installed, sealant gaps rectified, damaged mesh/gaskets replaced, and scratched/damaged components assessed for replacement where necessary."),

                Cat("5. Bathrooms & Plumbing/Sanitary Works Observations:", listOf(
                    "Tile hollowness, undulations and chip-offs observed.",
                    "Pipe sleeve areas are not properly packed/finished.",
                    "Gaps observed around sanitary fittings.",
                    "Door frame and finishing defects observed.",
                    "Window tracks require cleaning.",
                    "CP fittings are pending externally in Bedroom 4/associated area.",
                    "Rubber gasket damage observed in Bathroom 2."
                ), "Damaged and hollow tiles should be rectified/replaced. Pipe penetrations and sleeves should be properly packed and sealed. Gaps around sanitary fittings should be properly sealed and finished. Pending CP fittings should be completed and all bathroom fixtures checked for proper installation and functionality."),

                Cat("6. Electrical Works Observations:", listOf(
                    "Switchboard face plates are missing throughout the flat.",
                    "Gaps observed around the Distribution Board (DB).",
                    "DB cover drawing/documentation is pending."
                ), "All switchboard face plates should be installed and properly secured. DB gaps should be rectified to provide proper finishing and protection. The required DB cover drawing/documentation should be provided before handover."),

                Cat("7. Balcony Observations:", listOf(
                    "Tile hollowness observed.",
                    "Flooring offsets and gaps observed.",
                    "Skirting/flooring tile gaps observed.",
                    "Shade variation observed.",
                    "Crack observed in the upstand granite slab.",
                    "Window mesh damaged.",
                    "Scratches observed on glass."
                ), "Affected flooring tiles should be checked and rectified/replaced. Flooring levels, offsets, skirting and joints should be corrected. The cracked granite upstand should be assessed and replaced/rectified as required. Damaged window mesh should be replaced and scratched glass should be assessed for suitable corrective action.")
            )

            for (c in cats) {
                if (need(100f)) { newPage() }

                // Category title in orange (matches sample)
                cv!!.drawText(c.title, MARGIN, y, paint(11f, C_ORANGE, bold = true))
                y += 14f

                for (o in c.obs) {
                    if (need(16f)) { newPage() }
                    cv!!.drawText("•  $o", MARGIN + 8f, y, paint(10f, C_DARK))
                    y += 13f
                }
                y += 6f

                cv!!.drawText("Recommendation:", MARGIN + 8f, y, paint(10f, C_NAVY, bold = true))
                y += 13f
                y = wrap(c.rec, MARGIN + 8f, y, CONTENT_W - 8f, paint(10f, C_DARK))
                y += 14f
            }

            // Overall Recommendation
            if (need(60f)) { newPage() }

            cv!!.drawText("Overall Recommendation", MARGIN, y, paint(18f, C_DARK, bold = true))
            y += 18f
            y = wrap("The inspection indicates that the flat requires substantial rectification and completion of pending works before final handover.", MARGIN, y, CONTENT_W, paint(10f, C_DARK))
            y += 12f

            cv!!.drawText("The builder/contractor should:", MARGIN, y, paint(11f, C_NAVY, bold = true))
            y += 16f

            val tasks = listOf(
                "Rectify all identified workmanship and finishing defects.",
                "Complete all pending works and installations.",
                "Replace damaged tiles, granite, window mesh, gaskets and other defective components where required.",
                "Correct door and window alignment and installation issues.",
                "Complete painting and ensure uniform shade and surface finishing.",
                "Rectify electrical finishing, including switchboard face plates and DB-related works.",
                "Properly seal plumbing penetrations and sanitary fitting gaps.",
                "Remove construction debris, stains and mortar residue from finished surfaces.",
                "Address all sharp/protruding fasteners and other potential safety concerns.",
                "Provide pending technical documentation, including the DB cover drawing.",
                "Conduct functional checks of doors, windows, sanitary fittings and associated installations after rectification."
            )
            for (t in tasks) {
                if (need(16f)) { newPage() }
                cv!!.drawText("•  $t", MARGIN + 8f, y, paint(10f, C_DARK))
                y += 13f
            }

            footer()
            // =============================================

            // =============================================
            // ROOM-WISE SNAG PAGES (FIX #2: max 3 rows per page, headers reprinted)
            // =============================================
            for (section in data.sections) {
                var itemIdx = 0
                val items = section.items

                while (itemIdx < items.size) {
                    // Start a new page for each batch of up to 3 rows
                    newPage()
                    drawRoomHeader(section.title)

                    var rowCount = 0
                    while (rowCount < MAX_ROWS_PER_PAGE && itemIdx < items.size) {
                        val item = items[itemIdx]
                        val itemId = "${section.id}-${item.sno}"
                        val photo = data.uploadedImages[itemId]
                        drawRow(item.sno, item.description, photo, rowCount)
                        rowCount++
                        itemIdx++
                    }

                    footer()
                }
            }
            // =============================================

            // =============================================
            // DIMENSIONS PAGE (matches sample layout)
            // =============================================
            newPage()

            drawSectionTitle("FLAT DIMENSION")

            // Table header - matches sample column headers exactly
            val dimColW = floatArrayOf(0.25f, 0.25f, 0.25f, 0.25f)
            val dimHdrs = listOf("ROOM", "MENTIONED IN BROUCHER", "IN SITE VALUES", "COMMENTS")

            // Navy header row
            val dimHdrH = 24f
            cv!!.drawRect(MARGIN, y, RIGHT, y + dimHdrH, Paint().apply { this.color = color(C_NAVY) })
            var cx = MARGIN
            for (i in dimHdrs.indices) {
                cv!!.drawText(dimHdrs[i], cx + 6f, y + 16f, paint(10f, C_WHITE, bold = true))
                cx += CONTENT_W * dimColW[i]
            }
            y += dimHdrH

            for ((idx, dim) in data.dimensions.withIndex()) {
                // FIX #5: Parse nested room data (multi-line) for dynamic row height
                val roomLines = dim.area.split("\n")
                val lineCount = roomLines.size
                val rowH = maxOf(26f, lineCount * 14f + 10f)

                if (need(rowH + 4f)) {
                    newPage()
                    // Re-draw table header on new page
                    cv!!.drawRect(MARGIN, y, RIGHT, y + dimHdrH, Paint().apply { this.color = color(C_NAVY) })
                    cx = MARGIN
                    for (i in dimHdrs.indices) {
                        cv!!.drawText(dimHdrs[i], cx + 6f, y + 16f, paint(10f, C_WHITE, bold = true))
                        cx += CONTENT_W * dimColW[i]
                    }
                    y += dimHdrH
                }

                // Alternating row background
                if (idx % 2 == 0) {
                    cv!!.drawRect(MARGIN, y, RIGHT, y + rowH, Paint().apply { this.color = color(C_BG_ROW) })
                }

                // Outer cell border (FIX #4: uniform border on all cells)
                cv!!.drawRect(MARGIN, y, RIGHT, y + rowH, Paint().apply {
                    this.color = color(C_BORDER); style = Paint.Style.STROKE; strokeWidth = 0.5f
                })

                // Vertical column dividers
                cx = MARGIN
                for (i in 0 until dimColW.size - 1) {
                    cx += CONTENT_W * dimColW[i]
                    cv!!.drawLine(cx, y, cx, y + rowH, Paint().apply {
                        this.color = color(C_BORDER); strokeWidth = 0.5f
                    })
                }

                // ROOM column - FIX #5: multi-line nested data
                cx = MARGIN
                var lineY = y + 6f
                for ((li, line) in roomLines.withIndex()) {
                    val linePaint = if (li == 0) paint(10.5f, C_DARK, bold = true) else paint(9.5f, C_GRAY)
                    cv!!.drawText(line, cx + 6f, lineY, linePaint)
                    lineY += 14f
                }
                cx += CONTENT_W * dimColW[0]

                // BROCHUR column
                cv!!.drawText(dim.brochure, cx + 6f, y + 16f, paint(10.5f, C_DARK))
                cx += CONTENT_W * dimColW[1]

                // SITE VALUES column
                cv!!.drawText(dim.measured, cx + 6f, y + 16f, paint(10.5f, C_DARK))
                cx += CONTENT_W * dimColW[2]

                // COMMENTS column - status badge
                val isWarn = dim.status == "warn"
                val statusText = if (isWarn) "Discrepancy" else "Satisfactory"
                val statusClr = if (isWarn) C_RED else C_GREEN
                cv!!.drawText(statusText, cx + 6f, y + 16f, paint(10.5f, statusClr, bold = true))

                y += rowH
            }

            // Thank you + Company details (matches sample exactly)
            y += 30f
            if (need(100f)) { newPage(); y = MARGIN }

            cv!!.drawText("THANK YOU", MARGIN, y, paint(18f, C_DARK, bold = true))
            y += 28f
            cv!!.drawText("COMPANY DETAILS:", MARGIN, y, paint(11f, C_NAVY, bold = true))
            y += 18f
            cv!!.drawText("NAME: AIRA VISION PVT. LTD.", MARGIN, y, paint(10f, C_DARK))
            y += 14f
            cv!!.drawText("CONTACT NUMER: +91 7671010806 / 95550234556", MARGIN, y, paint(10f, C_DARK))
            y += 14f
            cv!!.drawText("WEBSITE: WWW.AIRAVISION.IN", MARGIN, y, paint(10f, C_DARK))
            y += 14f
            cv!!.drawText("EMAIL: AIRAVISION.AV@GMAIL.COM", MARGIN, y, paint(10f, C_DARK))

            footer()
            doc.finishPage(page!!)
            // =============================================

            // ===== SAVE =====
            val fileName = "AIRA_Inspection_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val privDir = File(context.filesDir, "reports").also { it.mkdirs() }
            val privFile = File(privDir, fileName)
            FileOutputStream(privFile).use { doc.writeTo(it) }
            doc.close()

            var dlUri: Uri? = null
            var dlPath = privFile.absolutePath
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val cv2 = ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/AIRA_Reports")
                    }
                    val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv2)
                    if (uri != null) {
                        context.contentResolver.openOutputStream(uri)?.use { out ->
                            privFile.inputStream().use { inp -> inp.copyTo(out) }
                        }
                        dlUri = uri
                        dlPath = "Downloads/AIRA_Reports/$fileName"
                    }
                } else {
                    val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "AIRA_Reports").also { it.mkdirs() }
                    val df = File(dir, fileName)
                    privFile.copyTo(df, overwrite = true)
                    dlPath = df.absolutePath
                }
            } catch (e: Exception) {
                Log.w(TAG, "Downloads copy failed", e)
            }

            return PdfResult(file = if (dlUri != null) null else privFile, uri = dlUri, path = dlPath)

        } catch (e: Exception) {
            Log.e(TAG, "PDF generation failed", e)
            try { doc.close() } catch (_: Exception) {}
            return null
        }
    }
}
