package com.aira.vision.inspection.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey val reportId: String,
    val reportNumber: String,
    val clientName: String,
    val project: String,
    val inspectedBy: String,
    val inspectionDate: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
