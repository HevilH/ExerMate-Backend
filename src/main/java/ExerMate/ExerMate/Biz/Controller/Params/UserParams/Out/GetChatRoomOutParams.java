package ExerMate.ExerMate.Biz.Controller.Params.UserParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class GetChatRoomOutParams extends CommonOutParams{

    private String chatRoomID;

    private String chatRoomName;

    public void setChatRoomID(String chatRoomID){this.chatRoomID = chatRoomID;}

    public void setChatRoomName(String chatRoomName){this.chatRoomName = chatRoomName;}
}
