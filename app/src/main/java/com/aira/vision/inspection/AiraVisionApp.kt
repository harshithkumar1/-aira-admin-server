package com.aira.vision.inspection

import android.app.Application
import com.aira.vision.inspection.data.AppDatabase

class AiraVisionApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}
