package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class SendImgOutParams extends CommonOutParams {
    private String bizType;

    private String useremail;

    private String chatRoomID;

    private String url;

    public void setBizType(String bizType){this.bizType = bizType;}

    public void setUseremail(String useremail){this.useremail = useremail;}

    public void setChatRoomID(String chatRoomID){this.chatRoomID = chatRoomID;}

    public void setUrl(String url){this.url = url;}

}
