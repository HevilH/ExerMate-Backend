package ExerMate.ExerMate.Biz.Handler;

import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Dispatcher;
import ExerMate.ExerMate.Frame.Util.LogUtil;
import ExerMate.ExerMate.Frame.Util.ThreadUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TimerJobHandler implements Job {
    @Autowired
    Dispatcher dispatcher;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        CommonInParams params = new CommonInParams();
        try {
            ThreadUtil.clean();
            params = (CommonInParams) jobExecutionContext.getMergedJobDataMap().get(KeyConstant.PARAMS);
            LogUtil.TIMER(params.getUseremail(), params.getBizType(), params);
            dispatcher.dispatch(params);
        } catch (Exception e) {
            LogUtil.ERROR(params.getUseremail(), params.getBizType(), params, e);
        }
    }
}
