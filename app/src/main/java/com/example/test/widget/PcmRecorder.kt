package com.example.test.widget

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission

import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

class PcmRecorder {

    // 音频参数配置
    private val sampleRate = 16000 // 采样率 (16000Hz 适合语音识别，音乐常用 44100Hz)
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO // 单声道
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT // 16位深度

    private var audioRecord: AudioRecord? = null
    private var isRecording = AtomicBoolean(false)
    private var recordingThread: Thread? = null

    // 计算缓冲区大小 (必须是有效值，否则 AudioRecord 初始化会失败)
    private val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private var outputPath = ""
    private var callbackDbLevel : ((Int)->Unit)? = null

    /**
     * 开始录音
     * @param outputFile 输出的 PCM 文件路径
     */
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startRecording(outputFile: File) : Boolean {
        if (isRecording.get()) return isRecording.get()

        try {
            // 1. 初始化 AudioRecord
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC, // 音源：麦克风
                sampleRate,                    // 采样率
                channelConfig,                 // 声道
                audioFormat,                   // 格式
                minBufferSize * 2              // 缓冲区大小 (建议稍微大一点以防溢出)
            )

            // 检查状态是否成功
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                throw IllegalStateException("AudioRecord 初始化失败")
            }

            // 2. 启动录制线程
            isRecording.set(true)
            outputPath = outputFile.path
            recordingThread = Thread {
                writeAudioDataToFile(outputFile)
            }
            recordingThread?.start()

            // 3. 开始录制
            audioRecord?.startRecording()

        } catch (e: Exception) {
            e.printStackTrace()
            stopRecording()
        }

        return isRecording.get()
    }

    /**
     * 停止录音
     */
    fun stopRecording(recordResult : ((String) -> Unit)? = {}) {
        if (!isRecording.get()) return

        // 停止录制
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            audioRecord = null
        }

        Thread{
            try {
                // 等待线程结束
                recordingThread?.join()
                recordResult?.invoke(outputPath)
            }catch (e: Exception) {
                e.printStackTrace()
            }finally {
                recordingThread = null
                isRecording.set(false)
            }
        }.start()

    }

    /**
     * 后台线程：循环读取音频数据并写入文件
     */
    private fun writeAudioDataToFile(file: File) {
        val buffer = ByteArray(minBufferSize * 2)
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(file)

            while (isRecording.get()) {
                // 读取音频数据到 buffer
                // read 返回读取到的字节数，如果出错返回负数
                val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: -1

                if (readSize > 0) {
                    //计算分贝
                    dbCalculate(readSize,buffer)
                    // 写入文件
                    fos.write(buffer, 0, readSize)
                } else if (readSize < 0) {
                    // 遇到错误，退出循环
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // 确保流关闭
            try {
                fos?.flush()
                fos?.close()
            } catch (e: Exception) { /* ignore */ }
        }
    }

    private fun dbCalculate(read : Int,buffer : ByteArray){

        if (read > 0) {
            // 1. 调用工具类处理数据
            val info: AudioDataUtil.AudioInfo = AudioDataUtil().getAudioInfo(buffer, read)

            // 2. 获取分贝值 (用于显示文字)
            val db: Double = info.db


            // 3. 获取振幅值 (用于模拟你之前的逻辑，显示波形或进度条)
            val amplitude: Int = info.amplitude


            // 如果你需要像之前那样映射到 0-8 的等级：
            val level = min(8.0, (amplitude * 8 / 32768).toDouble()).toInt()

            callbackDbLevel?.invoke(level)
            // 4. 更新 UI (记得切回主线程)
//            tvDb.setText(String.format("%.1f dB", db))
        }
    }

    // 获取当前录音配置信息 (可用于生成 WAV 头)
    fun getSampleRate() = sampleRate
    fun getChannelCount() = if (channelConfig == AudioFormat.CHANNEL_IN_MONO) 1 else 2
    fun getBitsPerSample() = 16
    fun setCallbackDb(callback : (Int)->Unit){
        this.callbackDbLevel = callback
    }
}