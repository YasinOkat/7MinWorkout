package com.yasin.workout

import android.app.Application

class DateApp: Application() {
    val db by lazy{
        DateDatabase.getInstance(this)
    }
}