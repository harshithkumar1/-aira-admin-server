package com.aira.vision.inspection.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aira.vision.inspection.R
import com.aira.vision.inspection.data.DashboardRepository
import com.aira.vision.inspection.data.Report
import com.aira.vision.inspection.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    repository: DashboardRepository,
    onOpenReport: (String) -> Unit,
    onNewReport: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf<Report?>(null) }

    LaunchedEffect(Unit) {
        repository.getAllReports().collect { reports = it }
    }

    Column(modifier = Modifier.fillMaxSize().background(AiraBg)) {
        // Header with AIRA logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(AiraNavy, AiraNavyLight)))
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.aira_logo),
                    contentDescription = "AIRA Logo",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("AIRA VISION", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 1.sp)
                    Text("HOME INSPECTION REPORTS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, letterSpacing = 2.sp)
                }
            }
        }

        // New Report button
        Button(
            onClick = onNewReport,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp).height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(AiraOrange, AiraOrangeLight))),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("New Inspection Report", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        }

        // Reports list
        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = AiraBorder, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No reports yet", color = AiraTextSecondary, fontSize = 16.sp)
                    Text("Tap 'New Inspection Report' to start", color = AiraTextSecondary, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(reports, key = { it.reportId }) { report ->
                    ReportCard(
                        report = report,
                        onClick = { onOpenReport(report.reportId) },
                        onDelete = { showDeleteDialog = report }
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteDialog?.let { report ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Report?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete the report for '${report.clientName}' and all its photos.") },
            confirmButton = {
                Button(
                    onClick = {
                        val reportToDelete = showDeleteDialog
                        showDeleteDialog = null
                        scope.launch {
                            reportToDelete?.let { repository.deleteReport(it.reportId) }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AiraRed)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ReportCard(
    report: Report,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AiraCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, AiraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Report icon
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(AiraNavy),
                contentAlignment = Alignment.Center
            ) {
                Text(report.reportNumber.removePrefix("AV-"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(report.clientName, color = AiraTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(2.dp))
                Text(report.project, color = AiraTextSecondary, fontSize = 12.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                val dateStr = try {
                    SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(report.createdAt))
                } catch (e: Exception) { "" }
                Text("${report.reportNumber}  |  $dateStr", color = AiraBlue, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
            }
        }
    }
}
