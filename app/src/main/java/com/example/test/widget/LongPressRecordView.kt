package com.example.test.widget

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresPermission
import com.example.test.R

import java.io.File

class LongPressRecordView : FrameLayout {

    private val TAG = LongPressRecordView::class.java.name

    constructor(context: Context?) : super(context!!) {
        mContext = context
        initView()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(
        context!!, attrs
    ) {
        mContext = context
        initView()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(
        context!!, attrs, defStyleAttr
    ) {
        mContext = context
        initView()
    }

    private var mContext: Context? = null
    private var viewSpeak: View? = null
    private var isSendAudio = true
    private var recorder: PcmRecorder = PcmRecorder()
    private var isRecordPermission = false//是否已经有录音权限
    private var permissionRequesting = true//权限请求中，用来处理长按录音的时候频繁调用权限请求
    private var pcmFile: File? = null
//    private var voskASR : VoskASR? = null

    private var clickTime = 0L
    private var countDownTimer : CountDownTimer? = null
    private var callbackUI : ((code : Int,text : String)->Unit)? = null
    private var callbackDb : ((Int)->Unit)? = null

    @SuppressLint("MissingInflatedId")
    private fun initView() {
        val childView =
            LayoutInflater.from(context).inflate(R.layout.view_long_press_record, this, true)
        viewSpeak = childView.findViewById(R.id.btn_press_to_speak)
        initData()
    }

    fun setCallbackUI(callback : (code : Int,text : String)->Unit){
        this.callbackUI = callback
    }
    fun setCallbackDb(callback : (db : Int)->Unit){
        this.callbackDb = callback
    }
    fun setRecordPermission(b:Boolean){
        this.isRecordPermission = b
    }



    private fun initData() {
        recorder.setCallbackDb { item -> callbackDb?.invoke(item) }
//        voskASR = mContext?.let { VoskASR(it) }
        viewSpeak?.setOnClickListener(null)
        viewSpeak?.setOnTouchListener(audioTouchListener)
    }


    @SuppressLint("ClickableViewAccessibility", "MissingPermission")
    private val audioTouchListener =
        OnTouchListener { _, motionEvent ->
            if (!isRecordPermission) {
                if (permissionRequesting) {
                    permissionRequesting = false
                    //正在请求，禁止多次掉用
                    requestPermission()
                }
                return@OnTouchListener true
            }
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    callbackUI?.invoke(RecordViewContans.UI_STATE_DOWN,"")
                    clickTime = System.currentTimeMillis()
                    isSendAudio = true
                    startCountdownWithTimer() // 开始倒计时
                    startRecord() // 开始录音
                }

                MotionEvent.ACTION_MOVE -> {
                    if (motionEvent.y < 0) {
                        callbackUI?.invoke(RecordViewContans.UI_STATE_REMOVE,"")
                        //取消
                        isSendAudio = false
                    } else {
                        callbackUI?.invoke(RecordViewContans.UI_STATE_MOVE_BACK,"")
                        //正常录音
                        isSendAudio = true
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    //停止录音。录音时间过短直接停止录音不发送数据
                    if (System.currentTimeMillis() - clickTime < 1500){
                        isSendAudio = false
                    }
                    cancelCountdownWithTimer()
                    stopRecord()
                }
            }
            true
        }


    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startRecord() {
        pcmFile =
            File(
                context.getExternalFilesDir(null),
                "long_press_record_" + System.currentTimeMillis() + ".pcm"
            )
        val result = recorder.startRecording(pcmFile!!)
        Log.d(TAG,"录音结果：$result")
    }


    private fun stopRecord() {
        callbackUI?.invoke(RecordViewContans.UI_STATE_UP,"")
        Log.d(TAG,"录制完成：${pcmFile}")
        recorder.stopRecording {
            if (!isSendAudio) {
                Log.d(TAG,"用户取消录音")
                deleteRecordFile()
                return@stopRecording
            }
            startRecognize()
        }
    }

    private fun deleteRecordFile(){
        try {
            if (pcmFile?.delete() == true) {

            }
        }catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun startRecognize() {
//        viewSpeak?.post {
//            Log.d(TAG,"开始识别音频")
//            voskASR?.startRecognize(pcmFile!!,true) { str ->
//                run {
//                    if (str.trim().isEmpty()) {
//                        val errorText = viewSpeak?.context?.getString(R.string.ai_chat_vosk_asr_error) ?:""
//                        ToastUtils.showShort(errorText)
//                    } else {
//                        viewSpeak?.post { callbackUI?.invoke(RecordViewContans.UI_STATE_RECOGNIZE_RESULT,str) }
////                        ToastUtils.showShort("结果：$str")
//                    }
//
//                }
//            }
//        }
    }


    /**
     * 定时器，语音最长录制60秒，达到60秒自动结束录制发送数据
     */
    private fun startCountdownWithTimer() {
        cancelCountdownWithTimer()

        val totalTime = 60 * 1000L // 60秒，单位毫秒
        val interval = 1000L   // 1秒，单位毫秒

        countDownTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                // 每隔 1 秒回调一次
                val secondsLeft = millisUntilFinished / 1000
                if (secondsLeft <= 0) return
                if (secondsLeft <= 3) {
                    val str = String.format(mContext?.getString(R.string.xmpp_can_still_say) ?:"", secondsLeft.toInt())
                    callbackUI?.invoke(RecordViewContans.UI_STATE_COUNTDOWN,str)
                }
            }

            override fun onFinish() {
                // 倒计时结束回调
                stopRecord()
            }
        }

        countDownTimer?.start()

        // 注意：如果在页面销毁时需要停止倒计时，请调用 timer.cancel()
    }

    private fun cancelCountdownWithTimer(){
        countDownTimer?.cancel()
    }

    private fun requestPermission(){
//        PermissionsUtil.requestWriteStorageAndRecorderPermission(object : PermissionListener {
//            override fun permissionGranted(permission: Array<String>) {
//                isRecordPermission = true
//                permissionRequesting = true
//                Utils.getApp().getString(com.genew.base.R.string.record_audio_fail)
//            }
//
//            override fun permissionDenied(permission: Array<String>) {
//                isRecordPermission = false
//                permissionRequesting = true
//                val tStr = Utils.getApp().getString(R.string.record_audio_needs_write_storage_record_permission)
//                ToastUtils.showShort(tStr)
//            }
//        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //页面销毁
        cancelCountdownWithTimer()
//        voskASR?.destroy()
    }

}