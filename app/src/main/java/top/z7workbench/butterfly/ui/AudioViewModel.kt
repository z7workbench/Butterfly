package top.z7workbench.butterfly.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import top.z7workbench.butterfly.app
import top.z7workbench.butterfly.audios.AudioData

class AudioViewModel: ViewModel() {
    private val _currentAudioList: MutableStateFlow<List<AudioData>> =
        MutableStateFlow(emptyList())
    private var _player: Player? = null;

    val player: Player?
        get() = _player
    val currentAudioList: StateFlow<List<AudioData>>
        get() = _currentAudioList

    suspend fun reloadAudio(context: Context) {
        _currentAudioList.value = context.app.audioDatabase.audioDao().getAllAudio()
        reloadAudio()
    }

    fun reloadAudio() {
        val mediaItems = currentAudioList.value.map {
            MediaItem.Builder()
                .setMediaId(it.uri)
                .build()
        }
        player?.addMediaItems(mediaItems)
    }

    fun initPlayer(player: Player) {
        this._player = player
    }

    fun isPlaying() = player?.isPlaying == true

    fun getPercent() = player?.currentPosition?.toFloat() ?: (0f / (player?.duration ?: 1))
}