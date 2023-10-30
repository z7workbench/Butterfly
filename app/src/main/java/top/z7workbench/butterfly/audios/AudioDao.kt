package top.z7workbench.butterfly.audios

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AudioDao {
    @Query("select * from audio")
    suspend fun getAllAudio(): List<AudioData>

    @Insert
    suspend fun insert(audio: AudioData)

    @Update
    suspend fun update(audio: AudioData)

    @Delete
    suspend fun delete(audio: AudioData)
}