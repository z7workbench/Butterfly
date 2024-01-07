package top.z7workbench.butterfly

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.Room
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.z7workbench.butterfly.audios.AudioDatabase
import top.z7workbench.butterfly.playback.ButterflyMediaService
import top.z7workbench.butterfly.ui.AudioViewModel
import top.z7workbench.butterfly.ui.ButterflyStore

@UnstableApi
class Butterfly : Application() {
    val audioDatabase: AudioDatabase
        get() = _audioDatabase
    private lateinit var _audioDatabase: AudioDatabase
    private lateinit var _mediaController: ListenableFuture<MediaController>
    private lateinit var _viewModel: AudioViewModel
    val viewModel: AudioViewModel
        get() = _viewModel

    override fun onCreate() {
        super.onCreate()
        _audioDatabase = Room.databaseBuilder(this, AudioDatabase::class.java, "audio.db")
            .build()
    }
}