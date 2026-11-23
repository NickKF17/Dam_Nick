package com.example.room.domain

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "word_table")
data class Word (
    @PrimaryKey
    @ColumnInfo(name = "Word")
    val word: String
)
