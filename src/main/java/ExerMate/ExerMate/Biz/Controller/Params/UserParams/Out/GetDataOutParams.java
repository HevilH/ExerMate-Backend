package ExerMate.ExerMate.Biz.Controller.Params.UserParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class GetDataOutParams extends CommonOutParams {

    private String nickName;

    private String profileRoute;

    private String statusMsg;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfileRoute(String profileRoute) { this.profileRoute = profileRoute; }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}