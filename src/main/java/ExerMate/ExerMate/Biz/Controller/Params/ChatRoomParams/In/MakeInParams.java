package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.CHATROOM_MAKE)
public class MakeInParams extends CommonInParams{

    @Required
    private String chatRoomName;

    public String getChatRoomName(){return chatRoomName;}
    public void setChatRoomName(String chatRoomName){this.chatRoomName = chatRoomName;}
}
