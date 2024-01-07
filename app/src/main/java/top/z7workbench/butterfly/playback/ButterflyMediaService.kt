package top.z7workbench.butterfly.playback

import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

@UnstableApi
class ButterflyMediaService : MediaLibraryService() {
    private val player: Player by lazy {
        ExoPlayer.Builder(applicationContext)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .setRenderersFactory(
                DefaultRenderersFactory(this).apply {
                    this.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
                }
            )
            .build()
    }

    private lateinit var notification: PlayerNotificationManager
    private lateinit var session: MediaLibrarySession
    override fun onCreate() {
        super.onCreate()

        session = MediaLibrarySession.Builder(this, player,
            object: MediaLibrarySession.Callback {
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: MutableList<MediaItem>
                ): ListenableFuture<MutableList<MediaItem>> {
                    val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
                    return Futures.immediateFuture(updatedMediaItems)
                }
            }).build()

        notification = PlayerNotificationManager.Builder(this, NOTIFICATION_ID, CHANNEL)
            .build()
        notification.setPlayer(player)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = session

    override fun onDestroy() {
        session.release()
        notification.setPlayer(null)
        player.release()
        super.onDestroy()
    }

    companion object {
        const val CHANNEL = "playback_service"
        const val NOTIFICATION_ID = 1
    }
}