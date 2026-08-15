package com.aira.vision.inspection.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setField(field: DashboardField)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFields(fields: List<DashboardField>)

    @Query("DELETE FROM dashboard_fields WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: String)
}

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setImage(image: UploadedImage)

    @Query("DELETE FROM uploaded_images WHERE reportId = :reportId AND itemId = :itemId")
    suspend fun deleteImage(reportId: String, itemId: String)

    @Query("DELETE FROM uploaded_images WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: String)
}

@Dao
interface DimensionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setDimensions(dimensions: List<DimensionEntry>)

    @Query("DELETE FROM dimension_data WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: String)
}
