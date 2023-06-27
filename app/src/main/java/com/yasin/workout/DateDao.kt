package com.yasin.workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert
    suspend fun insert(dateEntity: DateEntity)

    @Query("SELECT * FROM `date-table`")
    fun fetchTable(): Flow<List<DateEntity>>
}