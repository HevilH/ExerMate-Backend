package ExerMate.ExerMate.Biz.Controller.Params.UserParams.In;


import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;



@BizType(BizTypeEnum.USER_SET_STATUSMSG)
public class SetStatusMSGInParams extends CommonInParams {

    @Required
    private String statusMsg;

    public String getStatusMSG() {
        return statusMsg;
    }
}