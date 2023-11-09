package top.z7workbench.butterfly

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

val Context.app: Butterfly
    get() = this.applicationContext as Butterfly

@Composable
fun displayWidth(): Int {
    val configuration = LocalConfiguration.current
    Log.d("BTF.Util", "displayWidth() = " + configuration.screenWidthDp)
    return configuration.screenWidthDp
}

@Composable
fun displayHeight(): Int {
    val configuration = LocalConfiguration.current
    Log.d("BTF.Util", "displayHeight() = " + configuration.screenHeightDp)
    return configuration.screenHeightDp
}

@Composable
fun displayOrientation(): Int {
    val configuration = LocalConfiguration.current
    Log.d("BTF.Util", "displayOrientation() = " + configuration.orientation)
    return configuration.orientation
}

@Composable
fun displayRatio(): Float {
    return displayWidth().toFloat() / displayHeight()
}