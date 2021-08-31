package ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out;

import ExerMate.ExerMate.Base.Error.ExerMateWarn;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;


public class SysWarnOutParams extends CommonOutParams {
    private String code;
    private String msg;

    public SysWarnOutParams(ExerMateWarn warn) {
        code = warn.getErrorCode();
        msg = warn.getErrorMsg();
        success = false;
    }
}