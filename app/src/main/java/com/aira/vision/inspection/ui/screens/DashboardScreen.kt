package com.aira.vision.inspection.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aira.vision.inspection.data.DashboardRepository
import com.aira.vision.inspection.data.Report
import com.aira.vision.inspection.ui.theme.*

@Composable
fun DashboardScreen(
    repository: DashboardRepository,
    reportId: String,
    clientName: String,
    project: String,
    inspectedBy: String,
    inspectionDate: String,
    onFieldChanged: (String, String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var report by remember { mutableStateOf<Report?>(null) }

    LaunchedEffect(reportId) {
        report = repository.getReport(reportId)
    }

    Column(modifier = Modifier.fillMaxSize().background(AiraBg)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(AiraNavy, AiraNavyLight)))
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(report?.reportNumber ?: "", color = AiraOrange, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("AIRA VISION", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Info cards
        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            InfoCard("Client Name *", clientName, "Enter client name", AiraBlue) { onFieldChanged("client-name", it) }
            InfoCard("Project", project, "Enter project name", AiraOrange) { onFieldChanged("project", it) }
            InfoCard("Inspected By", inspectedBy, "Enter inspector name", AiraCyan) { onFieldChanged("inspected-by", it) }
            InfoCard("Inspection Date", inspectionDate, "Select date", Color(0xFF2ECC71)) { onFieldChanged("inspection-date", it) }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Continue button
        Button(
            onClick = {
                if (clientName.isBlank()) {
                    Toast.makeText(context, "Please enter Client Name to continue", Toast.LENGTH_SHORT).show()
                } else {
                    onContinue()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp).height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(AiraOrange, AiraOrangeLight))),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Continue", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    label: String,
    value: String,
    placeholder: String,
    color: Color,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AiraCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(label, color = color, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text(placeholder, color = AiraTextSecondary, fontSize = 14.sp) },
                textStyle = LocalTextStyle.current.copy(color = AiraTextPrimary, fontSize = 14.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = color,
                    unfocusedBorderColor = AiraBorder,
                    focusedContainerColor = AiraInputBg,
                    unfocusedContainerColor = AiraInputBg
                )
            )
        }
    }
}
