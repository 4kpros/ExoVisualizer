package com.prosabdev.exovisualizer

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.*
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.*

class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPlayer()
    }

    private val fftAudioProcessor = FFTAudioProcessor()

    private fun initPlayer() {
        // We need to create a renderers factory to inject our own audio processor at the end of the list
        val context = this
        val renderersFactory = object : DefaultRenderersFactory(context) {
            override fun buildAudioRenderers(
                context: Context,
                extensionRendererMode: Int,
                mediaCodecSelector: MediaCodecSelector,
                enableDecoderFallback: Boolean,
                audioSink: AudioSink,
                eventHandler: Handler,
                eventListener: AudioRendererEventListener,
                out: ArrayList<Renderer>
            ) {
                out.add(
                    MediaCodecAudioRenderer(
                        context,
                        mediaCodecSelector,
                        enableDecoderFallback,
                        eventHandler,
                        eventListener,
                        DefaultAudioSink(
                            AudioCapabilities.getCapabilities(context),
                            arrayOf(fftAudioProcessor)
                        )
                    )
                )

                super.buildAudioRenderers(
                    context,
                    extensionRendererMode,
                    mediaCodecSelector,
                    enableDecoderFallback,
                    audioSink,
                    eventHandler,
                    eventListener,
                    out
                )
            }
        }
        renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        player = ExoPlayer.Builder(context, renderersFactory)
            .build()

        val visualizer = findViewById<ExoVisualizer>(R.id.visualizer)
        visualizer.processor = fftAudioProcessor

        // Online radio:
        // val uri = Uri.parse("https://docs.google.com/uc?export=download&id=1Hp4IKZPs1uXiw1phPmZ2pZVYrURZ5F39")
//        val uri = Uri.parse("https://docs.google.com/uc?export=download&id=10D3K1QZzp33BONrHW6btzVIgT66eCwtn")
//        val uri = Uri.parse("https://docs.google.com/uc?export=download&id=1x33kjWj8geqLadCFZ1G0hUW_4DDkqIzm")

//        val uri = Uri.parse("http://listen.livestreamingservice.com/181-xsoundtrax_128k.mp3")
        // 1 kHz test sound:
//         val uri = Uri.parse("https://www.mediacollege.com/audio/tone/files/1kHz_44100Hz_16bit_05sec.mp3")
        // 10 kHz test sound:
        // val uri = Uri.parse("https://www.mediacollege.com/audio/tone/files/10kHz_44100Hz_16bit_05sec.mp3")
        // Sweep from 20 to 20 kHz
        val uri = Uri.parse("https://www.churchsoundcheck.com/CSC_sweep_20-20k.wav")
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        defaultHttpDataSourceFactory.setUserAgent("ExoVisualizer")

        val mediaSource = ProgressiveMediaSource.Factory(
            defaultHttpDataSourceFactory
        ).createMediaSource(MediaItem.Builder().setUri(uri).build())
        player?.playWhenReady = true
        player?.setMediaSource(mediaSource)
        player?.prepare()
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
    }
}
