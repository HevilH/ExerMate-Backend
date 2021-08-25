package ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;


public class SysErrorOutParams extends CommonOutParams {
    // 错误编码
    private String code;
    // 错误描述信息
    private String msg;

    public SysErrorOutParams() {
        code = "500";
        msg = "服务器内部错误";
        success = false;
    }
}
