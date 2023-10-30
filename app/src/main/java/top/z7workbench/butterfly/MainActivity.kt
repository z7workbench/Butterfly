package top.z7workbench.butterfly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import top.z7workbench.butterfly.audios.AudioData
import top.z7workbench.butterfly.audios.AudioUtil
import top.z7workbench.butterfly.ui.theme.ButterflyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ButterflyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainFrame()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFrame() {
    val context = LocalContext.current
    var audioList by remember {
        mutableStateOf(emptyList<AudioData>())
    }
    LaunchedEffect(Unit) {
        this.launch {
            AudioUtil.checkAndUpdateDatabase(context)
            audioList = context.app.audioDatabase.audioDao().getAllAudio()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                actions = {

                })
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Text(text = "get ${audioList.size} song(s)")
            }
        }
    }
}