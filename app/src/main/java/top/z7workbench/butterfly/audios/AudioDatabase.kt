package top.z7workbench.butterfly.audios

import androidx.room.Database
import androidx.room.RoomDatabase
import top.z7workbench.butterfly.playlists.PlaylistDao
import top.z7workbench.butterfly.playlists.PlaylistData

@Database(
    entities = [
        AudioData::class,
        PlaylistData::class
    ], version = 1, exportSchema = false
)
abstract class AudioDatabase : RoomDatabase() {
    abstract fun audioDao(): AudioDao
    abstract fun playlistDao(): PlaylistDao
}