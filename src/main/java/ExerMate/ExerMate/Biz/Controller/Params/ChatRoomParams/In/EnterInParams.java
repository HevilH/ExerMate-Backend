package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.CHATROOM_ENTER)
public class EnterInParams extends CommonInParams{

    @Required
    private String chatRoomID;

    public String getChatRoomID(){return chatRoomID;}
    public void setChatRoomID(String chatRoomID){this.chatRoomID = chatRoomID;}

}
