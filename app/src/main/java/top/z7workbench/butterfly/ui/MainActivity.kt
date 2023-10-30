package top.z7workbench.butterfly.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.z7workbench.butterfly.R
import top.z7workbench.butterfly.app
import top.z7workbench.butterfly.audios.AudioData
import top.z7workbench.butterfly.audios.AudioUtil
import top.z7workbench.butterfly.ui.theme.ButterflyTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AudioViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    AudioUtil.checkAndUpdateDatabase(this@MainActivity)
                    viewModel.reloadAudio(context = this@MainActivity)
                }
            } else {
                // TODO
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ButterflyTheme {
                // A surface container using the 'background' color from the theme
                LaunchedEffect(Unit) {
                    checkAudioPermission(
                        context = this@MainActivity,
                        doSuccessful = {
                            this.launch {
                                viewModel.reloadAudio(context = this@MainActivity)
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
                    MainFrame()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFrame(viewModel: AudioViewModel = viewModel()) {
    val context = LocalContext.current
    val state = viewModel.currentAudioList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                actions = {

                })
        }
    ) {
        LazyColumn(contentPadding = it) {
            item {
                Text(text = "get ${state.value.size} song(s)")
            }
            items(state.value, key = { i -> i.id }) {
                ListItem(headlineContent = {
                    Column {
                        Text(text = it.name ?: "", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Row {
                            Text(text = it.singer ?: "", color = Color.Gray)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = it.duration.toString(), color = Color.Gray)
                        }
                    }
                }, leadingContent = {
                    AsyncImage(
                        model = it.albumBitmap(context),
                        contentDescription = it.name,
//                        placeholder = painterResource(
//                            id = R.mipmap.ic_launcher
//                        ),
                        modifier = Modifier.size(48.dp)
                    )
                })
            }
        }
    }
}