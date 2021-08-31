package ExerMate.ExerMate.Biz.Controller;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.TestParams.In.LogTestInParams;
import ExerMate.ExerMate.Frame.Util.LogUtil;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class TimerController {

    @BizType(BizTypeEnum.LOG_TEST)
    public void logTest(LogTestInParams inParams) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LogUtil.INFO("타이머 업무 시작시간：" + dateFormat.format(inParams.getStartTime()) + "；타이머 업무 실행시간：" + dateFormat.format(new Date()));
    }
}
