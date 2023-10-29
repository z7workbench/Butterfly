package top.z7workbench.butterfly.model

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import top.z7workbench.butterfly.app
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object AudioUtil {
    private const val TAG = "BTF.AudioUtil"

    private suspend fun getAudio(context: Context): List<AudioData> {
        val cursor = suspendCoroutine {
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.IS_MUSIC
            )
            it.resume(cursor)
        }
        if (cursor == null || cursor.count == 0) {
            return emptyList()
        } else {
            val list = arrayListOf<AudioData>()
            cursor.moveToFirst()
            do {
                val data = suspendCoroutine {
                    var dataMayNull: AudioData? = null
                    try {
                        val id =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                        dataMayNull = AudioData(
                            id.toInt(),
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                                .toLong(),
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                                .toLong(),
                            Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
                        )
                    } catch (e: IllegalArgumentException) {
                        Log.e(TAG, "getAudio exception", e)
                    } catch (e: NullPointerException) {
                        Log.e(TAG, "getAudio exception", e)
                    }
                    it.resume(dataMayNull)
                }
                if (data != null) {
                    list.add(data)
                }
            } while (cursor.moveToNext())
            return list
        }
    }

    private fun getAlbum(context: Context, id: Long): Bitmap? {
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            id
        )
        return try {
            context.contentResolver.loadThumbnail(
                contentUri, Size(640, 480), null
            )
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun checkAndUpdateDatabase(context: Context) {
        val dao = context.app.audioDatabase.audioDao()
        val listFromDatabase = dao.getAllAudio().toMutableList()
        val listFromMediaStore = getAudio(context).toMutableList()
        if (listFromDatabase.isEmpty()) {
            listFromMediaStore.forEach {
                dao.insert(it)
            }
        } else {
            val needUpdate = arrayListOf<AudioData>()
            val iterator = listFromMediaStore.iterator()
            while (iterator.hasNext()) {
                val data = iterator.next()
                var flag = true
                for (i in listFromDatabase) {
                    if (data == i) {
                        flag = false
                    }
                }
                if (!flag) {
                    needUpdate.add(data)
                    listFromMediaStore.remove(data)
                    listFromDatabase.remove(data)
                }
            }
            listFromDatabase.forEach {
                dao.delete(it)
            }
            needUpdate.forEach {
                dao.update(it)
            }
            listFromMediaStore.forEach {
                dao.insert(it)
            }
        }
    }
}