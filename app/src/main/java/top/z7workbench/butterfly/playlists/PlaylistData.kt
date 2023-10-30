package top.z7workbench.butterfly.playlists

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "playlist")
data class PlaylistData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val desc: String,
    val songs: String
) {
    val songList: List<Int>
        get() = Json.decodeFromString<List<Int>>(songs)

    companion object {
        private const val TAG = "BTF.PlaylistData"

        fun toJsonString(songs: List<Int>) = try {
            Json.encodeToString(songs)
        } catch (e: Exception) {
            Log.e(TAG, "toJsonString: error", e)
            ""
        }
    }
}
