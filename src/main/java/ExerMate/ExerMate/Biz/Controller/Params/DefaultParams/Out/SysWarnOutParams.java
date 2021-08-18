package ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out;

import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 默认系统内部警告返回参数
 **/
public class SysWarnOutParams extends CommonOutParams {
    // 错误编码
    private String code;
    // 错误描述信息
    private String msg;

    public SysWarnOutParams(CourseWarn warn) {
        code = warn.getErrorCode();
        msg = warn.getErrorMsg();
        success = false;
    }
}