package com.aira.vision.inspection.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream

class DashboardRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val reportsDao = db.reportsDao()
    private val dashboardDao = db.dashboardDao()
    private val imageDao = db.imageDao()
    private val dimensionDao = db.dimensionDao()
    private val imageBaseDir = File(context.filesDir, "images").also { it.mkdirs() }

    // Reports
    fun getAllReports(): Flow<List<Report>> = reportsDao.getAllReports()
    suspend fun getReport(reportId: String): Report? = reportsDao.getReport(reportId)
    suspend fun insertReport(report: Report) = reportsDao.insertReport(report)
    suspend fun updateReport(report: Report) = reportsDao.updateReport(report)
    suspend fun deleteReport(reportId: String) {
        reportsDao.deleteReport(reportId)
        dashboardDao.clearForReport(reportId)
        dimensionDao.clearForReport(reportId)
        imageDao.clearForReport(reportId)
        // Delete image files
        val reportImageDir = File(imageBaseDir, reportId)
        if (reportImageDir.exists()) reportImageDir.deleteRecursively()
    }

    // Fields per report
    suspend fun getField(reportId: String, key: String): String? {
        val cursor = db.openHelper.readableDatabase.query(
            "SELECT fieldValue FROM dashboard_fields WHERE reportId = '$reportId' AND fieldKey = '$key'"
        )
        val value = if (cursor.moveToFirst()) cursor.getString(0) else null
        cursor.close()
        return value
    }

    suspend fun updateReportFields(reportId: String) {
        val clientName = getField(reportId, "client-name") ?: ""
        val project = getField(reportId, "project") ?: ""
        val report = reportsDao.getReport(reportId) ?: return
        reportsDao.updateReport(report.copy(clientName = clientName, project = project, updatedAt = System.currentTimeMillis()))
    }

    suspend fun setField(reportId: String, key: String, value: String) {
        // Delete existing field first, then insert new one
        db.openHelper.writableDatabase.execSQL(
            "DELETE FROM dashboard_fields WHERE reportId = '$reportId' AND fieldKey = '$key'"
        )
        dashboardDao.setField(DashboardField(reportId = reportId, fieldKey = key, fieldValue = value))
    }

    suspend fun setFields(reportId: String, fields: Map<String, String>) {
        dashboardDao.setFields(fields.map { DashboardField(reportId = reportId, fieldKey = it.key, fieldValue = it.value) })
    }

    // Images per report
    fun getImagesForReport(reportId: String): Flow<List<UploadedImage>> {
        return db.openHelper.readableDatabase.query(
            "SELECT * FROM uploaded_images WHERE reportId = '$reportId'"
        ).let {
            // Use a simple polling approach
            kotlinx.coroutines.flow.flow {
                while (true) {
                    val cursor = db.openHelper.readableDatabase.query(
                        "SELECT * FROM uploaded_images WHERE reportId = '$reportId'"
                    )
                    val images = mutableListOf<UploadedImage>()
                    while (cursor.moveToNext()) {
                        images.add(UploadedImage(
                            reportId = cursor.getString(cursor.getColumnIndexOrThrow("reportId")),
                            itemId = cursor.getString(cursor.getColumnIndexOrThrow("itemId")),
                            imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))
                        ))
                    }
                    cursor.close()
                    emit(images)
                    kotlinx.coroutines.delay(500)
                }
            }
        }
    }

    suspend fun getImagesList(reportId: String): List<UploadedImage> {
        val cursor = db.openHelper.readableDatabase.query(
            "SELECT * FROM uploaded_images WHERE reportId = '$reportId'"
        )
        val images = mutableListOf<UploadedImage>()
        while (cursor.moveToNext()) {
            images.add(UploadedImage(
                reportId = cursor.getString(cursor.getColumnIndexOrThrow("reportId")),
                itemId = cursor.getString(cursor.getColumnIndexOrThrow("itemId")),
                imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))
            ))
        }
        cursor.close()
        return images
    }

    suspend fun saveImage(context: Context, reportId: String, itemId: String, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open image")

        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val resized = resizeBitmap(bitmap, 1600, 1200)

        val reportDir = File(imageBaseDir, reportId).also { it.mkdirs() }
        val fileName = "${itemId.replace("-", "_")}_${System.currentTimeMillis()}.jpg"
        val file = File(reportDir, fileName)

        FileOutputStream(file).use { out ->
            resized.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        bitmap.recycle()
        resized.recycle()

        imageDao.setImage(UploadedImage(reportId = reportId, itemId = itemId, imagePath = file.absolutePath))
        return file.absolutePath
    }

    suspend fun deleteImage(reportId: String, itemId: String) {
        val cursor = db.openHelper.readableDatabase.query(
            "SELECT imagePath FROM uploaded_images WHERE reportId = '$reportId' AND itemId = '$itemId'"
        )
        if (cursor.moveToFirst()) {
            val path = cursor.getString(0)
            File(path).deleteIfExists()
        }
        cursor.close()
        imageDao.deleteImage(reportId, itemId)
    }

    // Dimensions per report
    fun getDimensionsForReport(reportId: String): Flow<List<DimensionEntry>> {
        return kotlinx.coroutines.flow.flow {
            while (true) {
                val cursor = db.openHelper.readableDatabase.query(
                    "SELECT * FROM dimension_data WHERE reportId = '$reportId' ORDER BY areaIndex"
                )
                val dims = mutableListOf<DimensionEntry>()
                while (cursor.moveToNext()) {
                    dims.add(DimensionEntry(
                        reportId = cursor.getString(cursor.getColumnIndexOrThrow("reportId")),
                        areaIndex = cursor.getInt(cursor.getColumnIndexOrThrow("areaIndex")),
                        area = cursor.getString(cursor.getColumnIndexOrThrow("area")),
                        brochure = cursor.getString(cursor.getColumnIndexOrThrow("brochure")),
                        measured = cursor.getString(cursor.getColumnIndexOrThrow("measured")),
                        status = cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        comment = cursor.getString(cursor.getColumnIndexOrThrow("comment"))
                    ))
                }
                cursor.close()
                emit(dims)
                kotlinx.coroutines.delay(500)
            }
        }
    }

    suspend fun saveDimensions(reportId: String, dimensions: List<DimensionEntry>) {
        dimensionDao.clearForReport(reportId)
        dimensionDao.setDimensions(dimensions.map { it.copy(reportId = reportId) })
    }

    suspend fun initializeReportDefaults(reportId: String) {
        // Insert default fields - empty so placeholder text shows
        val defaultFields = mapOf(
            "client-name" to "",
            "project" to "",
            "inspected-by" to "",
            "inspection-date" to java.text.SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
        )
        setFields(reportId, defaultFields)

        // Insert default dimensions
        dimensionDao.setDimensions(
            SnagDataProvider.DIMENSION_DATA.mapIndexed { index, dim ->
                DimensionEntry(
                    reportId = reportId,
                    areaIndex = index,
                    area = dim.area,
                    brochure = dim.brochure,
                    measured = dim.measured,
                    status = dim.status,
                    comment = dim.comment
                )
            }
        )
    }

    suspend fun getNextReportNumber(): Int {
        val cursor = db.openHelper.readableDatabase.query("SELECT reportNumber FROM reports ORDER BY createdAt DESC LIMIT 1")
        val lastNum = if (cursor.moveToFirst()) {
            val numStr = cursor.getString(0).removePrefix("AV-")
            numStr.toIntOrNull() ?: 0
        } else 0
        cursor.close()
        return lastNum + 1
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ratio = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height, 1f)
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun File.deleteIfExists(): Boolean {
        return if (exists()) delete() else false
    }
}
