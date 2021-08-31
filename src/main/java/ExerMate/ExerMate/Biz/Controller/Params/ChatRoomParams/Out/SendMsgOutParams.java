package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class SendMsgOutParams extends CommonOutParams {

    private String bizType;

    private String useremail;

    private String chatRoomID;

    private String text;

    private Long time;

    public void setBizType(String bizType){this.bizType = bizType;}

    public void setUseremail(String useremail){this.useremail = useremail;}

    public void setChatRoomID(String chatRoomID){this.chatRoomID = chatRoomID;}

    public void setText(String text){this.text = text;}

    public void setTime(Long time){this.time = time;}
}
