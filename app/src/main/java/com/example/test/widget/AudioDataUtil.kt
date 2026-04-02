package com.example.test.widget

import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt


/**
 * @author yanteng
 * @date 2026-03-27 16:41
 * @description
 */
class AudioDataUtil {

    /**
     * 处理 PCM 数据，计算分贝和振幅
     *
     * @param buffer   AudioRecord.read() 读取到的 byte 数组
     * @param readSize 实际读取到的字节数
     * @return AudioInfo 对象，包含分贝(dB)和最大振幅(amplitude)
     */
    fun getAudioInfo(buffer: ByteArray, readSize: Int): AudioInfo {
        if (readSize <= 0) {
            return AudioInfo(0, -100.0)
        }

        // 1. 计算均方根 (RMS) 和 最大振幅
        // 注意：AudioRecord 通常使用 ENCODING_PCM_16BIT，即 2个字节 = 1个采样点
        // 所以采样点数量 = readSize / 2
        val sampleCount = readSize / 2
        var sum = 0.0
        var maxAmplitude: Short = 0

        for (i in 0..<sampleCount) {
            // --- 关键：将 byte[] 转为 short (16bit) ---
            // Android 默认是小端序 (Little-Endian)
            // 低位字节在前，高位字节在后
            val sample =
                ((buffer[2 * i + 1].toInt() shl 8) or (buffer[2 * i].toInt() and 0xff)).toShort()

            // 累加平方值 (用于计算 RMS)
            sum += (sample * sample).toDouble()

            // 寻找最大振幅 (用于模拟 getMaxAmplitude 的效果)
            val absSample = abs(sample.toDouble()).toInt().toShort()
            if (absSample > maxAmplitude) {
                maxAmplitude = absSample
            }
        }

        // 2. 计算 RMS (均方根值)
        // RMS 代表声音的平均能量，比单纯的峰值更稳定
        val rms = sqrt(sum / sampleCount)

        // 3. 计算分贝 (dB)
        // 公式: 20 * log10(RMS / MaxAmplitude)
        // 16bit 音频的最大理论振幅是 32768 (2^15)
        var db = 0.0
        db = if (rms > 0) {
            20 * log10(rms / 32768.0)
        } else {
            -100.0 // 静音时的底值
        }

        return AudioInfo(maxAmplitude.toInt(), db)
    }

    /**
     * 简单的数据载体
     */
    class AudioInfo(// 最大振幅 (0 ~ 32768)
        var amplitude: Int, // 分贝值 (通常 -100.0 ~ 0.0)
        var db: Double
    ) {
        init {
            this.db = db
        }

        override fun toString(): String {
            return String.format(Locale.getDefault(), "振幅: %d, 分贝: %.2f dB", amplitude, db)
        }
    }
}