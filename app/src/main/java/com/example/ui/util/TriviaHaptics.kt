package com.example.ui.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/**
 * High-performance tactile haptic feedback provider for NBA sports trivia interactions.
 * Delivers distinct, tactile vibration signatures for correct and incorrect answers.
 */
object TriviaHaptics {

    private fun getVibrator(context: Context): Vibrator? {
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
                    ?: (context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        }.getOrNull()
    }

    /**
     * Subtle, crisp vibration signature for correct swish/shot answers.
     * Delivers a single snappy 45ms tap (or predefined click effect).
     */
    fun vibrateCorrect(context: Context, hapticFeedback: HapticFeedback? = null) {
        runCatching {
            hapticFeedback?.performHapticFeedback(HapticFeedbackType.LongPress)

            val vibrator = getVibrator(context) ?: return@runCatching
            if (!vibrator.hasVibrator()) return@runCatching

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                vibrator.vibrate(effect)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(45L, 160)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(45L)
            }
        }
    }

    /**
     * Distinct multi-pulse vibration pattern for incorrect/missed shots.
     * Delivers a rapid double-buzz warning pattern (75ms tap, 60ms gap, 130ms firm buzz).
     */
    fun vibrateIncorrect(context: Context, hapticFeedback: HapticFeedback? = null) {
        runCatching {
            hapticFeedback?.performHapticFeedback(HapticFeedbackType.LongPress)

            val vibrator = getVibrator(context) ?: return@runCatching
            if (!vibrator.hasVibrator()) return@runCatching

            val timings = longArrayOf(0, 75, 60, 130)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val amplitudes = intArrayOf(0, 180, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(timings, -1)
            }
        }
    }
}
