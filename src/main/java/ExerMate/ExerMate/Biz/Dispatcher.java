package ExerMate.ExerMate.Biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Error.CourseError;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Base.Error.SystemErrorEnum;
import ExerMate.ExerMate.Base.Model.User;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out.SysErrorOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out.SysWarnOutParams;
import ExerMate.ExerMate.Biz.Processor.UserProcessor;
import ExerMate.ExerMate.Frame.Util.LogUtil;
import ExerMate.ExerMate.Frame.Util.ThreadUtil;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Set;

/**
 * @描述 事件分发器，根据输入的参数来决定使用哪个类和函数来执行逻辑
 **/
@Component
public class Dispatcher {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserProcessor userProcessor;

    /**
     * 이벤트 디스패쳐
     * 모든 비즈니스는 Handler에서 처리후 Controller 내부에서 처리할때 디스패쳐를 꼭 걸쳐야한다
     * 目前用到分发器的有：①处理web的http请求；②处理socket长连接中客户端发送的请求；③处理定时任务中执行的请求；
     * 디스패쳐는 비즈니스의 매개변수에 따라 어떤 업무를 어떤 클래스의 어떤 함수에서 진행할지 결정한다
     * 这只是一个最简单的分发器，可以根据业务逻辑的复杂程度定制不同复杂度的分发器
     **/
    public String dispatch(CommonInParams params) {
        String useremail = params.getUseremail();
        BizTypeEnum bitType = params.getBizType();

        try {
            /** 비즈니스 타입 획득 */
            Class<?> exeCls = bitType.getControllerClass();
            if (exeCls == null)
                throw new CourseError(SystemErrorEnum.CONTROLLER_NOT_FIND);
            /** 클래스중의 비즈니스 실행함수 흭득 */
            Method exeMethod = getMethodByOptType(exeCls, bitType);
            if (exeMethod == null)
                throw new CourseError(SystemErrorEnum.METHOD_NOT_FIND);

            /** 매개변수가 맞는지 확인 */
            Class<?>[] methodParams = exeMethod.getParameterTypes();
            if (methodParams.length != 1 || !CommonInParams.class.isAssignableFrom(methodParams[0]))
                throw new CourseError(SystemErrorEnum.PARAMS_ERROR);

            /** 유저를 얻어서 쓰레드에 저장，이후 모든 처리에서 유저를 불러올 필요가 없음 */
            if (useremail != null && bitType != BizTypeEnum.USER_LOGIN && bitType != BizTypeEnum.USER_SIGNUP) {
                User user = userProcessor.getUserByUseremail(useremail);
                ThreadUtil.setUser(user);
            }

            /** 비즈니스 실행, 결과 리턴 */
            Object exeBean = applicationContext.getBean(exeCls);
            Object rlt = exeMethod.invoke(exeBean, params);
            if (rlt == null) /** 타이머 업무는 반환이 필요없음 */
                return new JSONObject().toString();
            else if (rlt instanceof List) { /** 반환되는 변수가 여러개일 경우 포장 */
                JSONArray retArr = new JSONArray();
                List<CommonOutParams> rlts = (List<CommonOutParams>)rlt;
                for (CommonOutParams obj : rlts)
                    retArr.add(obj.toJsonObject());
                return retArr.toString();
            } else if (rlt instanceof CommonOutParams) /** 아니면 바로 반환 */
                return rlt.toString();
            else /** 다른 종류의 변수를 반환하는 것을 허락하지 않음 */
                throw new CourseError(SystemErrorEnum.RETURN_PARAMS_ERROR);
        } catch (Exception e) {
            Throwable realError = e;
            boolean isWarning = false;

            /** 获取报错的类型，判断是警告还是真实错误 */
            if (e instanceof InvocationTargetException) {
                /** InvocationTargetException 代表业务内部逻辑执行有错误 */
                realError = ((InvocationTargetException)e).getTargetException();
                /** UndeclaredThrowableException 代表内部还有错误，获取更内层的错误 */
                if (realError instanceof UndeclaredThrowableException)
                    realError = ((UndeclaredThrowableException)realError).getUndeclaredThrowable();
            }
            if (realError instanceof CourseWarn) {
                isWarning = true;
            }
            if (isWarning) {
                CourseWarn courseWarn = (CourseWarn)realError;
                /** 记录警告日志，并返回警告的参数 */
                LogUtil.WARN(useremail, bitType, params, courseWarn);
                return new SysWarnOutParams(courseWarn).toString();
            } else {
                /** 记录错误日志并返回服务器内部错误 */
                LogUtil.ERROR(useremail, bitType, params, realError);
                return new SysErrorOutParams().toString();
            }
        }
    }

    /** 根据类名和操作类型获取执行业务的具体函数 */
    private Method getMethodByOptType(Class<?> cls, BizTypeEnum bitType) {
        Method method = null;
        Method[] methods = cls.getDeclaredMethods();
        for (Method m : methods) {
            /** 根据注解获取执行函数 */
            BizType cType = m.getAnnotation(BizType.class);
            if (cType != null && cType.value().equals(bitType)) {
                method = m;
                break;
            }
        }
        return method;
    }

    /** 根据操作类型获取入参类 */
    public Class<CommonInParams> getParamByBizType(BizTypeEnum bitType) {
        Reflections reflections = new Reflections(NameConstant.PACKAGE_NAME + ".Biz.Controller.Params");
        /** 根据注解获取入参类 */
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(BizType.class);
        for (Class<?> cls:classSet) {
            if (!CommonInParams.class.isAssignableFrom(cls))
                continue;
            Class<CommonInParams> paramCls = (Class<CommonInParams>)cls;
            BizType bizType1 = paramCls.getAnnotation(BizType.class);
            if (bizType1.value().equals(bitType))
                return paramCls;
        }
        /** 没有找到则认为使用默认入参类 */
        return CommonInParams.class;
    }
}