package top.z7workbench.butterfly.ui

import android.content.ComponentName
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import coil.compose.AsyncImage
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch
import top.z7workbench.butterfly.R
import top.z7workbench.butterfly.app
import top.z7workbench.butterfly.playback.ButterflyMediaService

@Composable
fun AdaptiveLayout(viewModel: AudioViewModel = viewModel()) {
//    TwoPane(
//        first = { /*TODO*/ },
//        second = { /*TODO*/ },
//        strategy = TwoPaneStrategy,
//        displayFeatures =
//    )
}

@UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeMainFrame(model: AudioViewModel = viewModel(viewModelStoreOwner = ButterflyStore)) {
    val context = LocalContext.current
    val state = model.currentAudioList.collectAsState()
    LaunchedEffect(key1 = Unit) {
        val controller = MediaController.Builder(
            context.app,
            SessionToken(context.app, ComponentName(context.app, ButterflyMediaService::class.java))
        ).buildAsync()
        controller.addListener({
            model.initPlayer(player = controller.get())
            model.player?.clearMediaItems()
            model.reloadAudio()
        }, MoreExecutors.directExecutor())

        this.launch {
            model.reloadAudio(context)
        }
    }

    Scaffold(
        topBar = {
//            TopAppBar(
//                title = { Text(text = "") },
//                actions = {
//
//                })
        },
        bottomBar = { BottomBar(model = model) }
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

@Composable
fun BottomBar(model: AudioViewModel) {

    BottomAppBar(modifier = Modifier.height(100.dp)) {
        val percent = remember {
            mutableFloatStateOf(0f)
        }
        SideEffect {
            model.player?.addListener(object : Listener {
                override fun onTracksChanged(tracks: Tracks) {
                    super.onTracksChanged(tracks)
                    percent.floatValue = 0f
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
//                    percent.floatValue = model.getPercent()
                    percent.floatValue = newPosition.positionMs.toFloat() / newPosition.contentPositionMs
                }
            })
        }

        Column {
            Row {
                Slider(value = percent.floatValue, onValueChange = {

                }, modifier = Modifier.height(4.dp))

                Text(text = "", modifier = Modifier.weight(1f))
            }

            Row {
                val buttonModifier = Modifier.size(52.dp)
                // Start button
                Icon(
                    painter = painterResource(
                        id = if (model.isPlaying()) R.drawable.ic_pause
                        else R.drawable.ic_play
                    ),
                    contentDescription = null,
                    modifier = buttonModifier.clickable {
                        Log.d("qwerty", model.player?.currentPosition.toString())
                        Log.d("qwerty", model.player?.duration.toString())
                        if (!model.isPlaying()) {
                            model.player?.play()
                        } else {
                            model.player?.pause()
                        }
                    }
                )
                // Stop button
                Icon(
                    painter = painterResource(id = R.drawable.ic_stop),
                    contentDescription = null,
                    modifier = buttonModifier
                )
            }
        }
    }
}