package ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;


public class SysErrorOutParams extends CommonOutParams {
    private String code;
    private String msg;

    public SysErrorOutParams() {
        code = "500";
        msg = "서버에 문제가 생겼습니다";
        success = false;
    }
}
