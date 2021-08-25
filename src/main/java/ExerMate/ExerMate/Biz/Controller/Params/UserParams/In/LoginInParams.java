package ExerMate.ExerMate.Biz.Controller.Params.UserParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;



@BizType(BizTypeEnum.USER_LOGIN)
public class LoginInParams extends CommonInParams {

    @Required
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

