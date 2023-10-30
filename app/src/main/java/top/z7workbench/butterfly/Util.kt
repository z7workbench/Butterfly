package top.z7workbench.butterfly

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

val Context.app: Butterfly
    get() = this.applicationContext as Butterfly
