package com.udemy.a7minutesworkout

import androidx.room.*

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyDao: HistoryEntity)

}