package ExerMate.ExerMate.Biz.Controller.Params.UserParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.USER_JOIN_CHATROOM)
public class JoinChatRoomInParams extends CommonInParams{

    @Required
    private String chatRoomID;



    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

}
