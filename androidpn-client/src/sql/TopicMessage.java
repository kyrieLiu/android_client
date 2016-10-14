package sql;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ls1213 on 2016/8/11.
 */

public class TopicMessage extends DataSupport implements Serializable{
    private long id;
    private String topicId;
    private String topicSrc;
    private String topicTitle;
    private String topicTime;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopicSrc() {
        return topicSrc;
    }

    public void setTopicSrc(String topicSrc) {
        this.topicSrc = topicSrc;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicTime() {
        return topicTime;
    }

    public void setTopicTime(String topicTime) {
        this.topicTime = topicTime;
    }
}
