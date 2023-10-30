package top.z7workbench.butterfly.audios

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(AudioData::class)], version = 1, exportSchema = false)
abstract class AudioDatabase: RoomDatabase() {
    abstract fun audioDao(): AudioDao
}