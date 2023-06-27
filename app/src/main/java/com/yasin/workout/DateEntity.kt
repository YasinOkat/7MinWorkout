package com.yasin.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date-table")
data class DateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String = ""
)
