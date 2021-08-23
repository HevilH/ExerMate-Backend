package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class GetUserListOutParams extends CommonOutParams{

    private String useremail;

    private String nickName;

    private String profileRoute;

    public void setUseremail(String useremail){this.useremail = useremail;}

    public void setNickName(String nickName){this.nickName = nickName;}

    public void setProfileRoute(String profileRoute){this.profileRoute =profileRoute;}
}
