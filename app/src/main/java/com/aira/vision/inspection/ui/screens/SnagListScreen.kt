package com.aira.vision.inspection.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.aira.vision.inspection.data.*
import com.aira.vision.inspection.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SnagListScreen(
    repository: DashboardRepository,
    reportId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sections = remember { SnagDataProvider.SNAG_DATA }
    val totalItems = remember { SnagDataProvider.totalItems }

    var uploadedImages by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var expandedSections by remember { mutableStateOf(setOf<String>()) }
    var imageViewerUri by remember { mutableStateOf<String?>(null) }

    // Dashboard fields for PDF
    var clientName by remember { mutableStateOf("") }
    var project by remember { mutableStateOf("") }
    var inspectedBy by remember { mutableStateOf("") }
    var inspectionDate by remember { mutableStateOf("") }

    var dimensionData by remember {
        mutableStateOf(SnagDataProvider.DIMENSION_DATA.map { dim ->
            EditableDimension(dim.area, dim.brochure, dim.measured, dim.status)
        })
    }

    LaunchedEffect(reportId) {
        // Load fields per report
        clientName = repository.getField(reportId, "client-name") ?: ""
        project = repository.getField(reportId, "project") ?: ""
        inspectedBy = repository.getField(reportId, "inspected-by") ?: ""
        inspectionDate = repository.getField(reportId, "inspection-date") ?: ""

        // Load existing images for this report
        val existingImages = repository.getImagesList(reportId)
        uploadedImages = existingImages.associate { it.itemId to it.imagePath }

        // Load existing dimensions for this report
        repository.getDimensionsForReport(reportId).collect { dims ->
            if (dims.isNotEmpty()) {
                dimensionData = dims.map { EditableDimension(it.area, it.brochure, it.measured, it.status) }
            }
        }
    }

    // Camera photo state
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var pendingItemId by remember { mutableStateOf<String?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var dialogItemId by remember { mutableStateOf<String?>(null) }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null && pendingItemId != null) {
            scope.launch {
                try {
                    val path = repository.saveImage(context, reportId, pendingItemId!!, pendingCameraUri!!)
                    uploadedImages = uploadedImages + (pendingItemId!! to path)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save photo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && pendingPhotoUri != null && pendingItemId != null) {
            pendingCameraUri = pendingPhotoUri
            cameraLauncher.launch(pendingPhotoUri!!)
        } else {
            Toast.makeText(context, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && dialogItemId != null) {
            scope.launch {
                try {
                    val path = repository.saveImage(context, reportId, dialogItemId!!, uri)
                    uploadedImages = uploadedImages + (dialogItemId!! to path)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save photo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val uploadedCount = uploadedImages.size
    val progress = if (totalItems > 0) uploadedCount.toFloat() / totalItems else 0f

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(AiraBg)) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(AiraNavy, AiraNavyLight)))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Detailed Snag List", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(project.ifEmpty { "Loading..." }, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                    }
                }
            }

            // Progress bar
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = AiraGreen, trackColor = AiraBorder
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("$uploadedCount of $totalItems issues documented", color = AiraTextSecondary, fontSize = 12.sp)
                    Text("Upload photos as proof", color = AiraTextSecondary, fontSize = 12.sp)
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(sections) { section ->
                    SnagSectionCard(
                        section = section,
                        isExpanded = section.id in expandedSections,
                        onToggle = {
                            expandedSections = if (section.id in expandedSections) expandedSections - section.id
                            else expandedSections + section.id
                        },
                        uploadedImages = uploadedImages,
                        onImageUploadClick = { itemId ->
                            dialogItemId = itemId
                            showImageSourceDialog = true
                        },
                        onImageDeleted = { itemId ->
                            scope.launch {
                                repository.deleteImage(reportId, itemId)
                                uploadedImages = uploadedImages - itemId
                            }
                        },
                        onImageClick = { path -> imageViewerUri = path }
                    )
                }

                // Room Dimensions
                item {
                    DimensionSectionCard(
                        dimensions = dimensionData,
                        onDimensionChanged = { index, updated ->
                            dimensionData = dimensionData.toMutableList().apply { set(index, updated) }
                            // Save dimensions to DB
                            scope.launch {
                                val dimEntries = dimensionData.mapIndexed { i, d ->
                                    DimensionEntry(
                                        reportId = reportId,
                                        areaIndex = i,
                                        area = d.area,
                                        brochure = d.brochure,
                                        measured = d.measured,
                                        status = d.status,
                                        comment = ""
                                    )
                                }
                                repository.saveDimensions(reportId, dimEntries)
                            }
                        }
                    )
                }

                // Generate PDF button
                item {
                    Button(
                        onClick = {
                            val dimData = dimensionData.map { d ->
                                val statusLabel = when (d.status) {
                                    "warn" -> "Discrepancy - Smaller than brochure"
                                    else -> "Satisfactory"
                                }
                                DimensionData(d.area, d.brochure, d.measured, d.status, statusLabel)
                            }
                            val pdfData = PdfGenerator.PdfData(
                                clientName = clientName,
                                project = project,
                                inspectedBy = inspectedBy,
                                inspectionDate = inspectionDate,
                                dimensions = dimData,
                                uploadedImages = uploadedImages,
                                sections = sections
                            )
                            scope.launch {
                                try {
                                    val result = PdfGenerator.generate(context, pdfData)
                                    if (result != null) {
                                        Toast.makeText(context, "PDF saved to: ${result.path}", Toast.LENGTH_LONG).show()
                                        val viewUri = result.uri ?: FileProvider.getUriForFile(
                                            context, "${context.packageName}.fileprovider", result.file!!
                                        )
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(viewUri, "application/pdf")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Open PDF with"))
                                    } else {
                                        Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(AiraNavy, AiraNavyLight))),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Generate Inspection PDF", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }
        }

        // Image viewer overlay
        imageViewerUri?.let { path ->
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)).clickable { imageViewerUri = null },
                contentAlignment = Alignment.Center
            ) {
                val bitmap = remember(path) {
                    try {
                        val file = File(path)
                        if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
                    } catch (e: Exception) { null }
                }
                bitmap?.let {
                    Image(bitmap = it.asImageBitmap(), contentDescription = "Full size image",
                        modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit)
                }
            }
        }

        // Image source dialog (Camera / Gallery)
        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Add Photo", fontWeight = FontWeight.Bold) },
                text = { Text("Choose how to add the photo proof") },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Camera button
                        OutlinedButton(
                            onClick = {
                                showImageSourceDialog = false
                                pendingItemId = dialogItemId
                                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                val imageDir = File(context.filesDir, "camera_photos")
                                imageDir.mkdirs()
                                val photoFile = File(imageDir, "IMG_${timeStamp}.jpg")
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
                                pendingPhotoUri = uri

                                // Check camera permission
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    pendingCameraUri = uri
                                    cameraLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Camera")
                        }

                        // Gallery button
                        Button(
                            onClick = {
                                showImageSourceDialog = false
                                galleryLauncher.launch("image/*")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AiraBlue)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Gallery")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImageSourceDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

data class EditableDimension(
    val area: String,
    val brochure: String,
    val measured: String,
    val status: String
)

@Composable
private fun DimensionSectionCard(
    dimensions: List<EditableDimension>,
    onDimensionChanged: (Int, EditableDimension) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AiraCardBg),
        border = BorderStroke(1.dp, AiraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded }
                    .background(if (isExpanded) Brush.linearGradient(listOf(AiraNavy, AiraNavyLight)) else Brush.linearGradient(listOf(AiraBg, AiraSurface)))
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("\uD83D\uDCCF", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Room Dimensions", color = if (isExpanded) Color.White else AiraTextPrimary,
                    fontWeight = FontWeight.SemiBold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                Surface(shape = RoundedCornerShape(16.dp), color = AiraPurple) {
                    Text("${dimensions.size}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null, tint = if (isExpanded) Color.White else AiraTextSecondary, modifier = Modifier.size(22.dp))
            }

            if (isExpanded) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().background(AiraBg).padding(horizontal = 8.dp, vertical = 8.dp)) {
                        Text("Area", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, modifier = Modifier.weight(0.28f))
                        Text("Brochure", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, modifier = Modifier.weight(0.24f))
                        Text("Measured", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, modifier = Modifier.weight(0.24f))
                        Text("Status", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, modifier = Modifier.weight(0.24f), textAlign = TextAlign.Center)
                    }

                    dimensions.forEachIndexed { index, dim ->
                        Row(
                            modifier = Modifier.fillMaxWidth().border(BorderStroke(0.5.dp, AiraBorder.copy(alpha = 0.5f)))
                                .padding(horizontal = 4.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(dim.area, color = AiraTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(0.28f).padding(start = 4.dp))

                            OutlinedTextField(value = dim.brochure, onValueChange = { onDimensionChanged(index, dim.copy(brochure = it)) },
                                modifier = Modifier.weight(0.24f).height(48.dp), textStyle = LocalTextStyle.current.copy(fontSize = 11.sp),
                                singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AiraBlue, unfocusedBorderColor = AiraBorder))

                            OutlinedTextField(value = dim.measured, onValueChange = { onDimensionChanged(index, dim.copy(measured = it)) },
                                modifier = Modifier.weight(0.24f).height(48.dp), textStyle = LocalTextStyle.current.copy(fontSize = 11.sp),
                                singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AiraBlue, unfocusedBorderColor = AiraBorder))

                            var expanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.weight(0.24f), contentAlignment = Alignment.Center) {
                                Surface(onClick = { expanded = true }, shape = RoundedCornerShape(6.dp),
                                    color = if (dim.status == "warn") AiraRed.copy(alpha = 0.1f) else AiraGreen.copy(alpha = 0.1f),
                                    border = BorderStroke(1.dp, if (dim.status == "warn") AiraRed.copy(alpha = 0.3f) else AiraGreen.copy(alpha = 0.3f))) {
                                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text(if (dim.status == "warn") "Discrepancy" else "Satisfactory",
                                            color = if (dim.status == "warn") AiraRed else AiraGreen, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null,
                                            tint = if (dim.status == "warn") AiraRed else AiraGreen, modifier = Modifier.size(14.dp))
                                    }
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(text = { Text("Satisfactory", color = AiraGreen) },
                                        onClick = { onDimensionChanged(index, dim.copy(status = "ok")); expanded = false })
                                    DropdownMenuItem(text = { Text("Discrepancy", color = AiraRed) },
                                        onClick = { onDimensionChanged(index, dim.copy(status = "warn")); expanded = false })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SnagSectionCard(
    section: SnagSection,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    uploadedImages: Map<String, String>,
    onImageUploadClick: (String) -> Unit,
    onImageDeleted: (String) -> Unit,
    onImageClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AiraCardBg),
        border = BorderStroke(1.dp, AiraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onToggle() }
                    .background(if (isExpanded) Brush.linearGradient(listOf(AiraNavy, AiraNavyLight)) else Brush.linearGradient(listOf(AiraBg, AiraSurface)))
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(section.icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(section.title, color = if (isExpanded) Color.White else AiraTextPrimary,
                    fontWeight = FontWeight.SemiBold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                Surface(shape = RoundedCornerShape(16.dp), color = if (isExpanded) AiraOrange.copy(alpha = 0.8f) else AiraOrange) {
                    Text("${section.items.size}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null, tint = if (isExpanded) Color.White else AiraTextSecondary, modifier = Modifier.size(22.dp))
            }

            if (isExpanded) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().background(AiraBg).padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("S.No", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, modifier = Modifier.width(40.dp))
                        Text("Issue Description", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, modifier = Modifier.weight(1f))
                        Text("Proof", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp,
                            modifier = Modifier.width(80.dp), textAlign = TextAlign.Center)
                    }

                    section.items.forEach { item ->
                        val itemId = "${section.id}-${item.sno}"
                        val hasImage = uploadedImages.containsKey(itemId)

                        Row(modifier = Modifier.fillMaxWidth().border(BorderStroke(0.5.dp, AiraBorder.copy(alpha = 0.5f)))
                            .padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("${item.sno}", color = AiraTextSecondary, fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                            Text(item.description, color = AiraTextPrimary, fontSize = 13.sp, lineHeight = 18.sp, modifier = Modifier.weight(1f))

                            if (hasImage) {
                                val bitmap = remember(itemId) {
                                    try { BitmapFactory.decodeFile(uploadedImages[itemId]) } catch (e: Exception) { null }
                                }
                                Box(modifier = Modifier.size(80.dp)) {
                                    bitmap?.let {
                                        Image(bitmap = it.asImageBitmap(), contentDescription = "Proof photo",
                                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(6.dp))
                                                .clickable { uploadedImages[itemId]?.let { onImageClick(it) } },
                                            contentScale = ContentScale.Crop)
                                    }
                                    IconButton(onClick = { onImageDeleted(itemId) },
                                        modifier = Modifier.align(Alignment.TopEnd).size(22.dp)) {
                                        Box(modifier = Modifier.size(18.dp).background(AiraRed, RoundedCornerShape(9.dp)),
                                            contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                }
                            } else {
                                Surface(onClick = { onImageUploadClick(itemId) }, shape = RoundedCornerShape(8.dp),
                                    color = AiraBlue.copy(alpha = 0.1f), border = BorderStroke(1.dp, AiraBlue.copy(alpha = 0.4f))) {
                                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AiraBlue, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Upload", color = AiraBlue, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
