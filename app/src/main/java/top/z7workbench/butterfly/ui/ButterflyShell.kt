package top.z7workbench.butterfly.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.z7workbench.butterfly.audios.AudioUtil
import top.z7workbench.butterfly.playback.ButterflyMediaService
import top.z7workbench.butterfly.ui.theme.ButterflyTheme

@UnstableApi
class ButterflyShell : ComponentActivity() {
    private val viewModel by viewModels<AudioViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    AudioUtil.checkAndUpdateDatabase(this@ButterflyShell)
                    viewModel.reloadAudio(context = this@ButterflyShell)
                }
            } else {
                // TODO
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, ButterflyMediaService::class.java))
        setContent {
            ButterflyTheme {
                // A surface container using the 'background' color from the theme
                LaunchedEffect(Unit) {
                    checkAudioPermission(
                        context = this@ButterflyShell,
                        doSuccessful = {
                            this.launch {
                                viewModel.reloadAudio(context = this@ButterflyShell)
                            }
                        },
                        doFailure = {
                            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                        }
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LandscapeMainFrame()
                }
            }
        }
    }

    private fun checkAudioPermission(
        context: Context,
        doSuccessful: () -> Unit = {},
        doFailure: () -> Unit = {}
    ) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) !=
            PackageManager.PERMISSION_DENIED
        ) {
            doSuccessful()
        } else {
            doFailure()
        }
    }
}
