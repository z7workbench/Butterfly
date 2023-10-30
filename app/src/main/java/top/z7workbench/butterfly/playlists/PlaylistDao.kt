package top.z7workbench.butterfly.playlists

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {
    @Query("select * from playlist")
    suspend fun getAllAudio(): List<PlaylistData>

    @Insert
    suspend fun insert(playlist: PlaylistData)

    @Update
    suspend fun update(playlist: PlaylistData)

    @Delete
    suspend fun delete(playlist: PlaylistData)
}