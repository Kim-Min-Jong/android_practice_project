package com.udemy.a7minutesworkout

import androidx.room.*

@Entity(tableName = "history-table")
data class HistoryEntity(
    @PrimaryKey
    val date: String,

)