package com.udemy.a7minutesworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyDao: HistoryEntity): Unit

    @Query("select * from `history-table`")
    fun fetchAllDates(): Flow<List<HistoryEntity>>
}