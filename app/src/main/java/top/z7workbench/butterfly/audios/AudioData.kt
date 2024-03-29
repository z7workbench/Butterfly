package top.z7workbench.butterfly.audios

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "audio")
data class AudioData(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var name: String? = null,
    var singer: String? = null,
    var album: Long? = null,
    var path: String? = null,
    var duration: Long = 0,
    var size: Long = 0,
    var uri: String = ""
) {
    fun albumBitmap(context: Context): Bitmap? {
        if (album == null) {
            return null
        }
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            album ?: 0
        )
        return try {context.contentResolver.loadThumbnail(
                contentUri, Size(640, 480), null
            )
        } catch (e: Exception) {
            return null
        }
    }

    val trueUri: Uri
        get() = Uri.parse(uri)

    override fun equals(other: Any?): Boolean {
        if (other !is AudioData) return false
        return id == other.id && name == other.name && singer == other.singer
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (singer?.hashCode() ?: 0)
        result = 31 * result + (album?.hashCode() ?: 0)
        result = 31 * result + (path?.hashCode() ?: 0)
        result = 31 * result + duration.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + uri.hashCode()
        return result
    }
}
