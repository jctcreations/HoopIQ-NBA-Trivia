package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Background Generic Audio Synthesizer:
 * Generates an energetic, rhythmic courtside sports ambiance soundtrack directly
 * using AudioTrack synthesis (warm bassline, arena kick pulses, and electronic synth chords)
 * without requiring external sound files or media player dependencies.
 */
object StadiumAudioPlayer {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var audioJob: Job? = null
    private var audioTrack: AudioTrack? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private const val SAMPLE_RATE = 22050
    // NBA Arena Courtside Groove scale frequencies (F minor pentatonic: F, Ab, Bb, C, Eb)
    private val BASS_NOTES = doubleArrayOf(87.31, 87.31, 103.83, 116.54, 130.81, 116.54, 103.83, 87.31)
    private val CHORD_ROOTS = doubleArrayOf(174.61, 207.65, 233.08, 261.63)

    fun togglePlayback() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun play() {
        if (_isPlaying.value) return
        _isPlaying.value = true

        audioJob?.cancel()
        audioJob = scope.launch {
            try {
                val minBufSize = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.play()

                var step = 0
                val samplesPerBeat = (SAMPLE_RATE * 0.22).toInt() // ~136 BPM energetic courtside groove
                val buffer = ShortArray(samplesPerBeat)

                while (isActive && _isPlaying.value) {
                    val bassFreq = BASS_NOTES[step % BASS_NOTES.size]
                    val chordFreq = CHORD_ROOTS[(step / 2) % CHORD_ROOTS.size]
                    val isKickBeat = (step % 2 == 0)

                    for (i in 0 until samplesPerBeat) {
                        val t = i.toDouble() / SAMPLE_RATE
                        // Arena Bass Wave
                        val bassWave = sin(2.0 * Math.PI * bassFreq * t) * 0.35
                        // Arena Synth Pad / Arp Wave
                        val synthWave = sin(2.0 * Math.PI * chordFreq * t) * 0.20
                        // Kick / courtside thump pulse on downbeats
                        val decay = (1.0 - (i.toDouble() / samplesPerBeat)).coerceIn(0.0, 1.0)
                        val kick = if (isKickBeat) (sin(2.0 * Math.PI * 55.0 * t) * decay * 0.40) else 0.0

                        val mixed = (bassWave + synthWave + kick).coerceIn(-1.0, 1.0)
                        buffer[i] = (mixed * 32767).toInt().toShort()
                    }

                    track.write(buffer, 0, samplesPerBeat)
                    step++
                }
            } catch (e: Exception) {
                Log.w("StadiumAudioPlayer", "Audio track error: ${e.message}")
            } finally {
                cleanUpTrack()
            }
        }
    }

    fun pause() {
        _isPlaying.value = false
        audioJob?.cancel()
        cleanUpTrack()
    }

    private fun cleanUpTrack() {
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.release()
            audioTrack = null
        } catch (_: Exception) {}
    }
}
