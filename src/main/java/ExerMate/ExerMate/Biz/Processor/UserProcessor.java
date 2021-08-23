package ExerMate.ExerMate.Biz.Processor;

import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Enum.UserType;
import ExerMate.ExerMate.Base.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


/**
 * @描述 모든 유저의 원자적 트랜잭션을 처리함
 **/
@Component
public class UserProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /**유저이메일에 따라 유저를 찾음*/
    public User getUserByUseremail(String useremail) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USEREMAIL).is(useremail));
        return mongoTemplate.findOne(query, User.class);
    }

    /**새로운 유저 추가*/
    public void createUser(String useremail, String password, String nickName) {
        User newUser = new User();
        newUser.setUserEmail(useremail);
        newUser.setPassword(password);
        newUser.setNickName(nickName);
        newUser.setProfileRoute("");
        newUser.setStatusMsg("");
        newUser.initWalkRecord();
        newUser.initChatRooms();
        newUser.setUserType(UserType.NORMAL);
        mongoTemplate.insert(newUser, "User");
    }

    /**프로필 사진 추가*/
    public void setProfile(String useremail) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USEREMAIL).is(useremail));
        Update update = new Update();
        update.set("profileRoute", NameConstant.URL_RES_PATH + useremail + ".jpg");
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void setStatusMSG(String useremail, String StatusMsg) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USEREMAIL).is(useremail));
        Update update = new Update();
        update.set("StatusMsg", StatusMsg);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void addWalkNum(String useremail, User.WalkNum walkNum) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USEREMAIL).is(useremail));
        Update update = new Update();
        update.push("walkRecord", walkNum);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void joinChatRoom(String useremail, String chatRoomID, String chatRoomName) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USEREMAIL).is(useremail));
        Update update = new Update();
        User.ChatRoom chatRoom = new User.ChatRoom();
        chatRoom.setChatRoomID(chatRoomID);
        chatRoom.setChatRoomName(chatRoomName);
        update.push("chatRooms", chatRoom);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}