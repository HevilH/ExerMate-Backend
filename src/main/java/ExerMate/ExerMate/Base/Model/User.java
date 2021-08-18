package ExerMate.ExerMate.Base.Model;

import ExerMate.ExerMate.Base.Enum.UserType;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("User")
public class User {

    public static class WalkNum {
        /** 存储的时间 */
        String walkNum;

        String date;

        public String getWalkNum() { return walkNum; }

        public void setWalkNum(String walkNum) { this.walkNum = walkNum; }

        public String getDate() { return date; }

        public void setDate(String date) { this.date = date; }
    }

    String id;
    String useremail;
    String password;
    String nickName;
    String profileRoute;
    String statusMsg;
    WalkNum [] walkRecord;
    UserType userType;


    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return useremail;
    }
    public void setUserEmail(String useremail) {
        this.useremail = useremail;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfileRoute() {
        return profileRoute;
    }
    public void setProfileRoute(String profileRoute) {
        this.profileRoute = profileRoute;
    }

    public String getStatusMsg() {
        return statusMsg;
    }
    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public WalkNum[] getWalkRecord() { return walkRecord; }
    public void setWalkRecord(WalkNum[] walkRecord) {
        this.walkRecord = walkRecord;
    }

    public void initWalkRecord(){this.walkRecord = new WalkNum[0];}
    public UserType getUserType() { return userType; }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


}