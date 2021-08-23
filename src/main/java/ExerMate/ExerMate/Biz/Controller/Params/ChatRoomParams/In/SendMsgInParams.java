package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.SEND_MSG)
public class SendMsgInParams extends CommonInParams{

    @Required
    String chatRoomID;

    @Required
    String text;

    @Required
    Long time;

    public String getChatRoomID() {
        return chatRoomID;
    }
    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) { this.text = text; }

    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
}
