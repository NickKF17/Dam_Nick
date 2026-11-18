package com.example.room.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.room.domain.Word

@Dao
interface WordDAO {
    @Query("Select * From word_table order by word ASC ")
    fun getAlphabetizedWords(): List<Word>

    @Insert
    suspend fun insert(word: Word)

@Query("Delete From word_table")
   suspend fun deleteAll()
}