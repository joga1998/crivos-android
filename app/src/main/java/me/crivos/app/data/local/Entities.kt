package me.crivos.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val materia: String,
    val pergunta: String,
    val resposta: String,
    val ef: Double = 2.5,
    val n: Int = 0,
    val iv: Int = 0,
    val due: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "cronograma_tarefas")
data class CronoEntity(
    @PrimaryKey val id: String,
    val materia: String,
    val topico: String,
    val duracaoMinutos: Int,
    val concluido: Boolean = false,
    val dataAgendada: String
)
