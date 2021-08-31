package ExerMate.ExerMate.Base.Model;

import ExerMate.ExerMate.Base.Enum.UserType;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("User")
public class User {

    public static class WalkNum {
        /** 存储的时间 */
        int walkNum;

        Long date;

        public int getWalkNum() { return walkNum; }

        public void setWalkNum(int walkNum) { this.walkNum = walkNum; }

        public Long getDate() { return date; }

        public void setDate(Long date) { this.date = date; }
    }

    public static class JoinedChatRoom {

        String chatRoomID;

        String chatRoomName;

        public String getChatRoomID() { return chatRoomID; }

        public void setChatRoomID(String chatRoomID) { this.chatRoomID = chatRoomID; }

        public String getChatRoomName() { return chatRoomName; }

        public void setChatRoomName(String chatRoomName) { this.chatRoomName = chatRoomName; }
    }

    String id;
    String useremail;
    String password;
    String nickName;
    String profileRoute;
    String statusMsg;
    WalkNum [] walkRecord;
    JoinedChatRoom [] chatRooms;
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

    public JoinedChatRoom[] getChatRooms() { return chatRooms; }
    public void setChatRooms(JoinedChatRoom[] chatRooms) {
        this.chatRooms = chatRooms;
    }
    public void initChatRooms(){this.chatRooms = new JoinedChatRoom[0];}

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }


}