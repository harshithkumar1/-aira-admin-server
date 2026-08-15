package com.aira.vision.inspection.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportsDao {
    @Query("SELECT * FROM reports ORDER BY updatedAt DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE reportId = :reportId")
    suspend fun getReport(reportId: String): Report?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report)

    @Update
    suspend fun updateReport(report: Report)

    @Query("DELETE FROM reports WHERE reportId = :reportId")
    suspend fun deleteReport(reportId: String)

    @Query("SELECT * FROM dashboard_fields WHERE reportId = :reportId")
    fun getFieldsForReport(reportId: String): Flow<List<DashboardField>>

    @Query("SELECT * FROM dimension_data WHERE reportId = :reportId ORDER BY areaIndex")
    fun getDimensionsForReport(reportId: String): Flow<List<DimensionEntryWithReport>>

    @Query("SELECT * FROM uploaded_images WHERE reportId = :reportId")
    fun getImagesForReport(reportId: String): Flow<List<UploadedImageWithReport>>

    @Query("DELETE FROM dashboard_fields WHERE reportId = :reportId")
    suspend fun clearFieldsForReport(reportId: String)

    @Query("DELETE FROM dimension_data WHERE reportId = :reportId")
    suspend fun clearDimensionsForReport(reportId: String)

    @Query("DELETE FROM uploaded_images WHERE reportId = :reportId")
    suspend fun clearImagesForReport(reportId: String)
}
