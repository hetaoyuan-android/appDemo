package com.example.test.ai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.BaseActivity;
import com.example.test.R;
import com.example.test.ai.bean.AiChatPicShowBean;
import com.example.test.ai.bean.DataText;
import com.example.test.utils.DbLevelUtil;
import com.example.test.utils.PermissionListener;
import com.example.test.utils.PermissionsUtil;
import com.example.test.utils.PicSelectUtil;
import com.example.test.widget.CustomToolbar;
import com.example.test.widget.ListBottomDialog;
import com.example.test.ai.adapter.AIChatAdapter;
import com.example.test.widget.LongPressRecordView;
import com.example.test.widget.RecordViewContans;
import com.genew.groupcontrol.ui.nuchat.adapter.AIChatPicShowAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class AiChatActivity extends BaseActivity {
    private RecyclerView rvAIChat;
    private final List<AiChatPicShowBean> picShowBeanList = new ArrayList<>();
    private AIChatPicShowAdapter aiChatPicShowAdapter;
    private final List<DataText> aiMsgList = new ArrayList<>();
    private AIChatAdapter aiChatAdapter;

    private int volumeImage = R.drawable.recorder_start_0;
    private int recordUiState = -1;
    private TextView txAudio;
    private String audioTip;
    private String udfConversationId = UUID.randomUUID().toString();
    private List<String> attachmentDialogContent = new ArrayList<>();
    private ListBottomDialog listBottomDialog;
    private TextView tvSearch;
    private CustomToolbar toolbar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_ai_chat;
    }

    @Override
    public void initView() {
        initToolBarView();
        iniView();
        initSignalR();
        initWelcomeUI();
        initSearchUI();

    }



    private void initSearchUI(){
        tvSearch = findViewById(R.id.tv_search_condition);
        tvSearch.setSelected(false);
        tvSearch.setOnClickListener(view -> tvSearch.setSelected(!tvSearch.isSelected()));
    }
    private void initWelcomeUI(){
        findViewById(R.id.tv_meeting).setOnClickListener(view -> sendDesignateTag(getString(R.string.conference_video_conference_create_confirm_title)));
    }

    private void initToolBarView() {
        toolbar = findViewById(R.id.new_toolbar);
        toolbar.setTitle(getString(R.string.str_ai_chat_toolbar_title));
        toolbar.setLeftNavigationIconVisible(true);
        toolbar.setLeftImageViewClickListener(this);
        toolbar.setRightIcon(R.drawable.ai_chat_title_right_news, view -> {
            welcomeGoneVisible(false);
            aiMsgList.clear();
            aiChatAdapter.notifyDataSetChanged();
            udfConversationId = "";
        });
        toolbar.setRightImageViewVisible(false);
    }

    private void iniView() {
        /// 需要一个uuid，全局的
        globalUUID = UUID.randomUUID().toString();
        initIdToolBar(R.id.status_bar_placeholder);
        initPicShow();
        initRVInput();
        initRecordUI();
        runOnUiThread(this::requestPermission);
    }

    private void initPicShow(){
        RecyclerView rvPicShow = findViewById(R.id.rv_pic_show);
        aiChatPicShowAdapter = new AIChatPicShowAdapter(this);
        aiChatPicShowAdapter.setClickCloseCallback( position -> {
            if (position >= 0 && position < picShowBeanList.size()) {
                picShowBeanList.remove((int)position);
                if (picShowBeanList.size() == 1) picShowBeanList.clear();
                aiChatPicShowAdapter.clearData();
                aiChatPicShowAdapter.addData(picShowBeanList);
            }
            return null;
        });
        aiChatPicShowAdapter.setClickAddCallback(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                if (isUploadPic(picShowBeanList)) {
                    selectAttachmentDialog();
                }
                return null;
            }
        });
        rvPicShow.setAdapter(aiChatPicShowAdapter);
    }

    private String globalUUID = "";

    private void initData(String text) {

        String aiText = "" + text;

        if (TextUtils.isEmpty(udfConversationId)) {
            udfConversationId = UUID.randomUUID().toString();
        }
        List<String> picList = AiChatPicShowBean.formatData(picShowBeanList);
        uiShowPic(picList);

    }

    private void uiShowPic(List<String> picList){
        if (!picList.isEmpty()) {
            DataText picBean = new DataText(DataText.TYPE_SELF_PIC,"");
            picBean.picListLocal.addAll(picList);
            aiMsgList.add(picBean);
            aiChatAdapter.notifyDataSetChanged();
            rvAIChat.smoothScrollToPosition(aiMsgList.size());

            //
            picShowBeanList.clear();
            aiChatPicShowAdapter.clearData();
        }
    }

    private void initSignalR(){





    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRVInput() {

        txAudio = findViewById(R.id.chat_audio);

        View rl_chat_editor = findViewById(R.id.rl_chat_editor);
        View btn_press_to_speak = findViewById(R.id.view_long_press_record);
        View btn_set_mode_voice = findViewById(R.id.btn_set_mode_voice);
        View btn_set_mode_keyboard = findViewById(R.id.btn_set_mode_keyboard);
        btn_set_mode_voice.setOnClickListener(view -> {
            btn_set_mode_voice.setVisibility(View.GONE);
            btn_set_mode_keyboard.setVisibility(View.VISIBLE);
            rl_chat_editor.setVisibility(View.GONE);
            btn_press_to_speak.setVisibility(View.VISIBLE);
        });
        btn_set_mode_keyboard.setOnClickListener(view -> {
            btn_set_mode_keyboard.setVisibility(View.GONE);
            btn_set_mode_voice.setVisibility(View.VISIBLE);
            rl_chat_editor.setVisibility(View.VISIBLE);
            btn_press_to_speak.setVisibility(View.GONE);
        });

        findViewById(R.id.btn_more).setOnClickListener(view -> {
            if (isUploadPic(picShowBeanList)) {
                selectAttachmentDialog();
            }
        });

        PicSelectUtil.INSTANCE.setCallbackFilePath(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String path) {
                rvAIChat.post(()->{
                    // 检查路径是否有效
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }
                    if (picShowBeanList.isEmpty()) {
                        // first add ,create addBean
                        picShowBeanList.add(new AiChatPicShowBean(path));
                        picShowBeanList.add(AiChatPicShowBean.getAddBean());
                    } else {
                        picShowBeanList.add(picShowBeanList.size()-1,new AiChatPicShowBean(path));
                    }
                    aiChatPicShowAdapter.clearData();
                    aiChatPicShowAdapter.addData(picShowBeanList);
                });
                return null;
            }
        });

        rvAIChat = findViewById(R.id.rv_aichat);
        aiChatAdapter = new AIChatAdapter(aiMsgList);
        aiChatAdapter.setItemChildViewClick(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer position) {
                //从新发送数据
                try {
                    String strText = aiMsgList.get(((int)position) - 1).text;
                    aiMsgList.remove(aiMsgList.size() -1);
                    aiChatAdapter.notifyDataSetChanged();
                    initData(strText);
                }catch (Exception  e) {
                    Log.d("TAG","refresh error");
                }
                return null;
            }
        });
        rvAIChat.setAdapter(aiChatAdapter);

        EditText etInput = findViewById(R.id.et_input);
        TextView tvSend = findViewById(R.id.tv_send);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String number = etInput.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    tvSend.setVisibility(View.VISIBLE);
                } else {
                    tvSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvSend.setOnClickListener(view -> {
            String inputStr = etInput.getText().toString().trim();
            sendTextMsg(inputStr);
            etInput.setText("");
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendTextMsg(String text){
        if (!TextUtils.isEmpty(text.trim())) {
            welcomeGoneVisible(true);
            initData(text);
            aiMsgList.add(new DataText(DataText.TYPE_SELF,"" + text));
            aiChatAdapter.notifyDataSetChanged();
            rvAIChat.smoothScrollToPosition(aiMsgList.size());
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //拍照
        super.onActivityResult(requestCode, resultCode, data);
        PicSelectUtil.INSTANCE.onActivityResult(this,requestCode,resultCode,data);
    }

    private void initRecordUI(){
        LongPressRecordView viewRecord = findViewById(R.id.view_long_press_record);
        viewRecord.setCallbackDb(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer level) {
                if (recordUiState == RecordViewContans.UI_STATE_REMOVE) return null;
                volumeImage = DbLevelUtil.INSTANCE.getDbLevelDrawable(level);
                txAudio.setBackgroundResource(volumeImage);
                return null;
            }
        });
        viewRecord.setCallbackUI(new Function2<Integer,String, Unit>() {
            @Override
            public Unit invoke(Integer code,String text) {
                recordUiState = code;

                switch (code) {
                    case RecordViewContans.UI_STATE_DOWN:
                        //按下按钮，模式UI
                        audioTip = getString(R.string.xmpp_cancel_voice_send);
                        txAudio.setText(audioTip);
                        txAudio.setBackgroundResource(R.drawable.recorder_start_0);
                        txAudio.setVisibility(View.VISIBLE);
                        break;
                    case RecordViewContans.UI_STATE_REMOVE:
                        //上划取消
                        txAudio.setText(getString(R.string.xmpp_cancel_voice_send));
                        txAudio.setBackgroundResource(
                                R.drawable.recorder_cancel);
                        break;
                    case RecordViewContans.UI_STATE_MOVE_BACK:
                        //滑回去继续显示录音
                        txAudio.setBackgroundResource(volumeImage);
                        txAudio.setText(audioTip);
                        break;
                    case RecordViewContans.UI_STATE_UP:
                        //停止录音
                        txAudio.setVisibility(View.GONE);
                        break;
                    case RecordViewContans.UI_STATE_COUNTDOWN:
                        audioTip = text;
                        txAudio.setText(audioTip);
                        break;
                    case RecordViewContans.UI_STATE_RECOGNIZE_RESULT:
                        sendTextMsg(text);
                        break;
                }
                return null;
            }
        });
    }

    private boolean isUploadPic(List picList){
        if (picList != null && !picList.isEmpty() && picList.size() >= 6) {

            return false;
        } else {
            return true;
        }
    }

    private void requestPermission(){
        PermissionsUtil.requestWriteStorageAndRecorderPermission(new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                LongPressRecordView viewRecord = findViewById(R.id.view_long_press_record);
                viewRecord.setRecordPermission(true);
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
//                String tStr = getString(R.string.record_audio_needs_write_storage_record_permission);
//                ToastUtils.showShort(tStr);
            }
        });
    }

    private void welcomeGoneVisible(boolean isGone){
        View welcomeView = findViewById(R.id.includ_welcome);
        if (isGone) {
            welcomeView.setVisibility(View.GONE);
            toolbar.setRightImageViewVisible(true);
        } else {
            welcomeView.setVisibility(View.VISIBLE);
            toolbar.setRightImageViewVisible(false);
        }
    }

    private void selectAttachmentDialog(){
        if (attachmentDialogContent.isEmpty()) {
            attachmentDialogContent.add(getString(R.string.str_ai_chat_dialog_camera));
            attachmentDialogContent.add(getString(R.string.str_ai_chat_dialog_photo));
        }
        if (listBottomDialog != null) listBottomDialog.dismiss();
        listBottomDialog = new ListBottomDialog(this, attachmentDialogContent, (adapter, view, position) -> {
            listBottomDialog.dismiss();
            if (position == 0) {/// 拍照
                if (isUploadPic(picShowBeanList)) {
                    PicSelectUtil.INSTANCE.gotoCamera(AiChatActivity.this);
                }
            } else if (position == 1) { /// 照片
                if (isUploadPic(picShowBeanList)) {
                    PicSelectUtil.INSTANCE.gotoPhoto(AiChatActivity.this);
                }
            }
        });
        listBottomDialog.setTitle(getString(R.string.str_ai_chat_dialog_title));
        listBottomDialog.showListDialog();
    }

    private void sendDesignateTag(String text){
        sendTextMsg(text);
    }


}
