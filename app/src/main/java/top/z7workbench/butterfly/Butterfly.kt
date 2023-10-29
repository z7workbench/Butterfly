package top.z7workbench.butterfly

import android.app.Application
import androidx.room.Room
import top.z7workbench.butterfly.model.AudioDatabase

class Butterfly: Application() {
    val audioDatabase: AudioDatabase
        get() = _audioDatabase
    private lateinit var _audioDatabase: AudioDatabase

    override fun onCreate() {
        super.onCreate()
        _audioDatabase = Room.databaseBuilder(this, AudioDatabase::class.java, "audio.db")
            .build()
    }
}