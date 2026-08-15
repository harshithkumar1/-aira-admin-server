package com.aira.vision.inspection.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_fields")
data class DashboardField(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportId: String,
    val fieldKey: String,
    val fieldValue: String
)

@Entity(tableName = "uploaded_images")
data class UploadedImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportId: String,
    val itemId: String,
    val imagePath: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "dimension_data")
data class DimensionEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportId: String,
    val areaIndex: Int,
    val area: String,
    val brochure: String,
    val measured: String,
    val status: String,
    val comment: String
)

data class DimensionEntryWithReport(
    val id: Int,
    val reportId: String,
    val areaIndex: Int,
    val area: String,
    val brochure: String,
    val measured: String,
    val status: String,
    val comment: String
)

data class UploadedImageWithReport(
    val id: Int,
    val reportId: String,
    val itemId: String,
    val imagePath: String,
    val timestamp: Long
)
