package top.z7workbench.butterfly.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import top.z7workbench.butterfly.R

@Composable
fun AdaptiveLayout(viewModel: AudioViewModel = viewModel()) {
//    TwoPane(
//        first = { /*TODO*/ },
//        second = { /*TODO*/ },
//        strategy = TwoPaneStrategy,
//        displayFeatures =
//    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeMainFrame(viewModel: AudioViewModel = viewModel()) {
    val context = LocalContext.current
    val state = viewModel.currentAudioList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                actions = {

                })
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(100.dp)) {

                Column {
                    Row {
                        Slider(value = 0f, onValueChange = {

                        }, modifier = Modifier.height(4.dp))

                        Text(text = "", modifier = Modifier.weight(1f))
                    }

                    Row {
                        val buttonModifier = Modifier.size(48.dp)
                        // Start button
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = null,
                            modifier = buttonModifier
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