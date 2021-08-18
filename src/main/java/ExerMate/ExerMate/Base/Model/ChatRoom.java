package ExerMate.ExerMate.Base.Model;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ChatRoom")
public class ChatRoom {
    String id;
    String chatRoomID;
    String chatRoomName;
    String hostemail;
    String[] guestemails;

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }
    public void setChatRoomID(String chatRoomID) { this.chatRoomID = chatRoomID; }

    public String getChatRoomName() {
        return chatRoomName;
    }
    public void setChatRoomName(String chatRoomName) { this.chatRoomID = chatRoomName; }

    public String getHostEmail() {
        return hostemail;
    }
    public void setHostEmail(String hostemail) {
        this.hostemail = hostemail;
    }

    public String []getGuestemails() { return guestemails; }
    public void setGuestemails(String[] guestemails) {
        this.guestemails = guestemails;
    }

    public void initGuestemails() { this.guestemails = new String[0]; }
}
