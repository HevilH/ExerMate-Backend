package ExerMate.ExerMate.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("ExerPost")
public class ExerPost {
    String id;
    String exerPostID;
    String exerName;
    String useremail;
    Long uploadTime;
    String exerPlace;
    String exerTime;
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
        this.exerPostID = exerPostID;
    }

    public String getUserEmail() { return useremail; }
    public void setUserEmail(String useremail) {
        this.useremail = useremail;
    }

    public String getExerName() {
        return exerName;
    }
    public void setExerName(String exerName) {
        this.exerName = exerName;
    }

    public Long getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getExerPlace() {
        return exerPlace;
    }
    public void setExerPlace(String exerPlace) {
        this.exerPlace = exerPlace;
    }

    public String getExerTime() { return exerTime; }
    public void setExerTime(String exerTime) {
        this.exerTime = exerTime;
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
