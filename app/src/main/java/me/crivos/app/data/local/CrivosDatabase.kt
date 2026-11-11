package me.crivos.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FlashcardEntity::class, CronoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CrivosDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun cronoDao(): CronoDao
}
