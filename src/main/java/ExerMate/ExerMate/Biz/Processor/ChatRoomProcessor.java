package ExerMate.ExerMate.Biz.Processor;

import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Enum.UserType;
import ExerMate.ExerMate.Base.Model.ChatRoom;
import ExerMate.ExerMate.Base.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ChatRoomProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    public ChatRoom getChatRoomByID(String chatRoomID){
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.CHATROOMID).is(chatRoomID));
        return mongoTemplate.findOne(query, ChatRoom.class);
    }

    public void makeChatRoom(String hostemail, String chatRoomName){
        ChatRoom newChatRoom = new ChatRoom();
        Date date = new Date();
        newChatRoom.setChatRoomID(hostemail + date);
        newChatRoom.setHostEmail(hostemail);
        newChatRoom.setChatRoomName(chatRoomName);
        newChatRoom.initGuestemails();
        mongoTemplate.insert(newChatRoom, "ChatRoom");
    }

    public void addGuest(String chatRoomID, String guestemail){
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.CHATROOMID).is(chatRoomID));
        Update update = new Update();
        update.push("guestemails", guestemail);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
