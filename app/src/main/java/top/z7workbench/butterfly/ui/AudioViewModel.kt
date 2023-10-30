package top.z7workbench.butterfly.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import top.z7workbench.butterfly.app
import top.z7workbench.butterfly.audios.AudioData

class AudioViewModel: ViewModel() {
    private val _currentAudioList: MutableStateFlow<List<AudioData>> =
        MutableStateFlow(emptyList())
    val currentAudioList: StateFlow<List<AudioData>>
        get() = _currentAudioList

    suspend fun reloadAudio(context: Context) {
        _currentAudioList.value = context.app.audioDatabase.audioDao().getAllAudio()
    }
}