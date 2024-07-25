package com.example.vecros

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView

@androidx.media3.common.util.UnstableApi
class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var rtspUrlInput: EditText
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        rtspUrlInput = findViewById(R.id.rtsp_url_input)
        startButton = findViewById(R.id.start_button)

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        startButton.setOnClickListener {
            val rtspUrl = rtspUrlInput.text.toString()
            if (rtspUrl.isNotEmpty()) {
                playVideo(rtspUrl)
            } else {
                Toast.makeText(this, "Please enter a valid RTSP URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playVideo(rtspUrl: String) {
        val dataSourceFactory = DefaultDataSource.Factory(this)

        val mediaItem = MediaItem.fromUri(rtspUrl)

        // Create a RtspMediaSource instance using the factory
        val mediaSource: MediaSource = RtspMediaSource.Factory()
            .createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true

        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}
