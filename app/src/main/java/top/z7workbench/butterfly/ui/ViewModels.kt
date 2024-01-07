package top.z7workbench.butterfly.ui

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

object ButterflyStore: ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}