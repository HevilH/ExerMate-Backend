package ExerMate.ExerMate.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("ExerPost")
public class ExerPost {
    String id;
    String exerPostID;
    String useremail;
    String chatRoomID;
    String uploadTime;
    String meetingPlace;
    String meetingTime;
    int maxNum;
    String contents;

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getExerPostId() {
        return exerPostID;
    }
    public void setExerPostID(String exerPostID) {
        this.exerPostID = id;
    }

    public String getUserEmail() { return useremail; }
    public void setUserEmail(String useremail) {
        this.useremail = useremail;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }
    public void setChatRoomID(String exerPostID) {
        this.chatRoomID = id;
    }

    public String getUploadTime() {
        return uploadTime;
    }
    public void setPassword(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }
    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getMeetingTime() { return meetingTime; }
    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public int getMaxNum() {
        return maxNum;
    }
    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getContents() { return contents; }
    public void setContents(String contents) {
        this.contents = contents;
    }
}
