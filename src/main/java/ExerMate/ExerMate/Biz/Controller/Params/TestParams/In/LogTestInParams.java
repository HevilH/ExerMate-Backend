package ExerMate.ExerMate.Biz.Controller.Params.TestParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

import java.util.Date;

/**
 * @描述 登录权限测试返回类型
 **/
@BizType(BizTypeEnum.LOG_TEST)
public class LogTestInParams extends CommonInParams {
    // 定时任务开始时间
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
