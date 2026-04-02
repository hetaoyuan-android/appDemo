package com.example.test.ai.bean;

/**
 * @author yanteng
 * @date 2026-03-20 10:04
 * @description
 */
public class Message {

    public final static String ChatStreamText = "ChatStreamText";
    public final static String RequestRoutes = "RequestRoutes";

    public final static String StartVideoConference = "StartVideoConference";
    public final static String Call = "Call";

    public Object traceId;
    public String commandCode;
    /**
     * data是一个对象，对象中的属性个数是可变的，服务端会根据 commandCode 字段返回不同的data。
     * 该处的data客户端会把data当做json字符串处理，根据不同的 commandCode， data会解析成不同的数据类
     */
    public DataText data;
}
