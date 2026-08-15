package com.aira.vision.inspection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.aira.vision.inspection.data.DashboardRepository
import com.aira.vision.inspection.data.Report
import com.aira.vision.inspection.ui.screens.*
import com.aira.vision.inspection.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = DashboardRepository(this)

        setContent {
            AiraVisionTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AiraBg) {
                    val scope = rememberCoroutineScope()
                    var currentScreen by remember { mutableStateOf("home") }
                    var currentReportId by remember { mutableStateOf<String?>(null) }
                    var currentClientName by remember { mutableStateOf("") }
                    var currentProject by remember { mutableStateOf("") }
                    var currentInspectedBy by remember { mutableStateOf("") }
                    var currentInspectionDate by remember { mutableStateOf("") }

                    suspend fun saveCurrentFields() {
                        val rid = currentReportId ?: return
                        repository.setField(rid, "client-name", currentClientName)
                        repository.setField(rid, "project", currentProject)
                        repository.setField(rid, "inspected-by", currentInspectedBy)
                        repository.setField(rid, "inspection-date", currentInspectionDate)
                        repository.updateReportFields(rid)
                    }

                    when (currentScreen) {
                        "home" -> HomeScreen(
                            repository = repository,
                            onNewReport = {
                                scope.launch {
                                    val num = repository.getNextReportNumber()
                                    val reportId = "report_${System.currentTimeMillis()}"
                                    val reportNumber = "AV-$num"
                                    val report = Report(
                                        reportId = reportId,
                                        reportNumber = reportNumber,
                                        clientName = "",
                                        project = "",
                                        inspectedBy = "",
                                        inspectionDate = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
                                    )
                                    repository.insertReport(report)
                                    repository.initializeReportDefaults(reportId)
                                    currentReportId = reportId
                                    currentClientName = ""
                                    currentProject = ""
                                    currentInspectedBy = ""
                                    currentInspectionDate = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
                                    currentScreen = "dashboard"
                                }
                            },
                            onOpenReport = { reportId ->
                                scope.launch {
                                    currentReportId = reportId
                                    currentClientName = repository.getField(reportId, "client-name") ?: ""
                                    currentProject = repository.getField(reportId, "project") ?: ""
                                    currentInspectedBy = repository.getField(reportId, "inspected-by") ?: ""
                                    currentInspectionDate = repository.getField(reportId, "inspection-date") ?: ""
                                    currentScreen = "dashboard"
                                }
                            }
                        )

                        "dashboard" -> DashboardScreen(
                            repository = repository,
                            reportId = currentReportId ?: "",
                            clientName = currentClientName,
                            project = currentProject,
                            inspectedBy = currentInspectedBy,
                            inspectionDate = currentInspectionDate,
                            onFieldChanged = { key, value ->
                                when (key) {
                                    "client-name" -> currentClientName = value
                                    "project" -> currentProject = value
                                    "inspected-by" -> currentInspectedBy = value
                                    "inspection-date" -> currentInspectionDate = value
                                }
                            },
                            onContinue = {
                                scope.launch {
                                    saveCurrentFields()
                                    currentScreen = "snaglist"
                                }
                            },
                            onBack = {
                                scope.launch {
                                    saveCurrentFields()
                                    currentScreen = "home"
                                }
                            }
                        )

                        "snaglist" -> SnagListScreen(
                            repository = repository,
                            reportId = currentReportId ?: "",
                            onBack = {
                                scope.launch {
                                    saveCurrentFields()
                                    currentScreen = "dashboard"
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
