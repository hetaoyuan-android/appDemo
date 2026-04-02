package com.example.test.ai.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanteng
 * @date 2026-03-20 10:04
 * @description
 */
public class DataText {

    public static final int TYPE_SELF = 0;
    public static final int TYPE_SERVICE = 1;
    public static final int TYPE_SELF_PIC = 3;
    public int msgTypeLocal = TYPE_SERVICE;//这个字段是客户端自己展示recycleview使用
    public final static float START_WORK_FLOW_TYPE = 0.0F;
    public final static float MIDDLE_WORK_FLOW_TYPE = 1.0F;
    public final static float END_WORK_FLOW_TYPE = 2.0F;

    public String text = "";
    public String chatName;
    public String udfConversationId;
    public float workflowType;

    public List<String> picListLocal = new ArrayList<>();//客户端显示UI的字段

    public DataText() {
    }

    public DataText(int msgTypeLocal, String text) {
        this.msgTypeLocal = msgTypeLocal;
        this.text = text;
    }

    public DataText appendText(DataText bean){
        msgTypeLocal = bean.msgTypeLocal;
        text += bean.text;
        chatName = bean.chatName;
        udfConversationId = bean.udfConversationId;
        workflowType = bean.workflowType;
        return this;
    }

    @Override
    public String toString() {
        return "Data{" +
                "msgTypeLocal=" + msgTypeLocal +
                ", text='" + text + '\'' +
                ", chatName='" + chatName + '\'' +
                ", udfConversationId='" + udfConversationId + '\'' +
                ", workflowType=" + workflowType +
                '}';
    }
}
