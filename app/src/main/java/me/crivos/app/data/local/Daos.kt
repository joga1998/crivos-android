package me.crivos.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards ORDER BY updatedAt DESC")
    fun getAllCards(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE due <= :todayDate OR due = ''")
    fun getDueCards(todayDate: String): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<FlashcardEntity>)

    @Update
    suspend fun updateCard(card: FlashcardEntity)
}

@Dao
interface CronoDao {
    @Query("SELECT * FROM cronograma_tarefas WHERE dataAgendada = :date")
    fun getTasksByDate(date: String): Flow<List<CronoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<CronoEntity>)

    @Update
    suspend fun updateTask(task: CronoEntity)
}
