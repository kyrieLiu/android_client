package org.androidpn.client;

import android.util.Log;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by 刘隐 on 2016/6/13.
 */
public class SetMessageIQ extends IQ {

    private String targetUser;
    private String content;
    private String sendUser;

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    @Override
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("setmessage").append(" xmlns=\"")
                .append("androidpn:iq:setmessage").append("\">");
        if (sendUser!=null){
            buf.append("<senduser>").append(sendUser).append("</senduser>");
        }
        if (content != null) {
            buf.append("<content>").append(content).append("</content>");
        }
        if (targetUser!=null){
            buf.append("<targetuser>").append(targetUser).append("</targetuser>");
        }
        buf.append("</").append("setmessage").append("> ");
        Log.i("info","buf:  "+buf.toString());
        return buf.toString();
    }
}
