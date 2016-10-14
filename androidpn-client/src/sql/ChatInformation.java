package sql;

import org.litepal.crud.DataSupport;

/**
 * Created by Kyrie on 2016/6/21.
 * Email:kyrie_liu@sina.com
 * 聊天信息存储实体
 */
public class ChatInformation extends DataSupport {
    private long id;
    private String sendTime;
    private String targetAccountName;
    private String peopleName;
    private String messageContent;
    private String photoUrl;
    private boolean receive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTargetAccountName() {
        return targetAccountName;
    }

    public void setTargetAccountName(String targetAccountName) {
        this.targetAccountName = targetAccountName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }


    public void setPhotoUrl(String photoUrl) {

        this.photoUrl = photoUrl;
    }

    public boolean getReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    @Override
    public String toString() {
        return "ChatInformation{" +
                "id=" + id +
                ", sendTime='" + sendTime + '\'' +
                ", targetAccountName='" + targetAccountName + '\'' +
                ", peopleName='" + peopleName + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", receive=" + receive +
                '}';
    }
}
