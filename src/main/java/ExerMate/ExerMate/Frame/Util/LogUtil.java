package ExerMate.ExerMate.Frame.Util;


import ExerMate.ExerMate.Base.Constant.GlobalConstant;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Error.CourseError;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.ExerMateApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogUtil {
    private static Logger allLogger = LoggerFactory.getLogger(ExerMateApplication.class);
    private static Logger timerLogger = LoggerFactory.getLogger(NameConstant.TIMER_LOG);


    public static void ERROR(String username, BizTypeEnum bizType, Object args, Throwable error) {
        StringBuilder sb = new StringBuilder();
        sb.append(username)
                .append(GlobalConstant.LOG_SPLIT)
                .append(bizType)
                .append(GlobalConstant.LOG_SPLIT)
                .append(args.toString())
                .append(GlobalConstant.LOG_SPLIT);
        if (error == null)
            sb.append("null");
        else if (error instanceof CourseError)
            sb.append(((CourseError)error).getErrorCode());
        else
            sb.append(error.toString());
        sb.append(GlobalConstant.LOG_SPLIT)
                .append(ParseUtil.getErrorStackInfo(error));
        allLogger.error(sb.toString());
    }


    public static void WARN(String username, BizTypeEnum bizType, Object args, CourseWarn state) {
        String sb = username + GlobalConstant.LOG_SPLIT +
                bizType + GlobalConstant.LOG_SPLIT +
                args.toString() + GlobalConstant.LOG_SPLIT +
                (state == null ? "null" : state.getErrorCode()) + GlobalConstant.LOG_SPLIT +
                ParseUtil.getWarnStackInfo(state);
        allLogger.warn(sb);
    }


    public static void INFO(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object obj:objects)
            sb.append(obj.toString()).append(GlobalConstant.LOG_SPLIT);
        allLogger.info(sb.toString());
    }


    public static void TIMER(String username, BizTypeEnum bizType, CommonInParams params) {
        String sb = username + GlobalConstant.LOG_SPLIT +
                bizType + GlobalConstant.LOG_SPLIT +
                params.toString();
        timerLogger.info(sb);
    }
}