package com.example.test.ai.bean;

import java.util.ArrayList;
import java.util.List;

public class AiChatPicShowBean {
    public static final int TYPE_PIC = 0;
    public static final int TYPE_ADD = 1;

    public int viewType = TYPE_PIC;
    public String picUrl = "";

    public AiChatPicShowBean() {
    }

    public AiChatPicShowBean(String picUrl) {
        this.picUrl = picUrl;
    }

    public static AiChatPicShowBean getAddBean(){
        AiChatPicShowBean bean = new AiChatPicShowBean();
        bean.viewType = TYPE_ADD;
        return bean;
    }

    public static List<String> formatData(List<AiChatPicShowBean> beanList){
        List<String> strList = new ArrayList<>();
        if (beanList != null && !beanList.isEmpty()) {
            beanList.forEach(item -> {
                if (item.viewType == TYPE_PIC) {
                    strList.add(item.picUrl);
                }
            });
        }
        return strList;
    }

}
