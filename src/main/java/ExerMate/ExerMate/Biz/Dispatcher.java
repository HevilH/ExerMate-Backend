package ExerMate.ExerMate.Biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Error.ExerMateError;
import ExerMate.ExerMate.Base.Error.ExerMateWarn;
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


@Component
public class Dispatcher {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserProcessor userProcessor;


    public String dispatch(CommonInParams params) {
        String useremail = params.getUseremail();
        BizTypeEnum bitType = params.getBizType();

        try {
            Class<?> exeCls = bitType.getControllerClass();
            if (exeCls == null)
                throw new ExerMateError(SystemErrorEnum.CONTROLLER_NOT_FIND);
            Method exeMethod = getMethodByOptType(exeCls, bitType);
            if (exeMethod == null)
                throw new ExerMateError(SystemErrorEnum.METHOD_NOT_FIND);

            Class<?>[] methodParams = exeMethod.getParameterTypes();
            if (methodParams.length != 1 || !CommonInParams.class.isAssignableFrom(methodParams[0]))
                throw new ExerMateError(SystemErrorEnum.PARAMS_ERROR);

            if (useremail != null && bitType != BizTypeEnum.USER_LOGIN && bitType != BizTypeEnum.USER_SIGNUP) {
                User user = userProcessor.getUserByUseremail(useremail);
                ThreadUtil.setUser(user);
            }

            Object exeBean = applicationContext.getBean(exeCls);
            Object rlt = exeMethod.invoke(exeBean, params);
            if (rlt == null)
                return new JSONObject().toString();
            else if (rlt instanceof List) {
                JSONObject ret = new JSONObject();
                JSONArray retArr = new JSONArray();
                List<CommonOutParams> rlts = (List<CommonOutParams>)rlt;
                for (CommonOutParams obj : rlts)
                    retArr.add(obj.toJsonObject());
                ret.put("result", retArr);
                return ret.toString();
            } else if (rlt instanceof CommonOutParams)
                return rlt.toString();
            else
                throw new ExerMateError(SystemErrorEnum.RETURN_PARAMS_ERROR);
        } catch (Exception e) {
            Throwable realError = e;
            boolean isWarning = false;


            if (e instanceof InvocationTargetException) {
                realError = ((InvocationTargetException)e).getTargetException();
                if (realError instanceof UndeclaredThrowableException)
                    realError = ((UndeclaredThrowableException)realError).getUndeclaredThrowable();
            }
            if (realError instanceof ExerMateWarn) {
                isWarning = true;
            }
            if (isWarning) {
                ExerMateWarn exerMateWarn = (ExerMateWarn)realError;
                LogUtil.WARN(useremail, bitType, params, exerMateWarn);
                return new SysWarnOutParams(exerMateWarn).toString();
            } else {
                LogUtil.ERROR(useremail, bitType, params, realError);
                return new SysErrorOutParams().toString();
            }
        }
    }


    private Method getMethodByOptType(Class<?> cls, BizTypeEnum bitType) {
        Method method = null;
        Method[] methods = cls.getDeclaredMethods();
        for (Method m : methods) {
            BizType cType = m.getAnnotation(BizType.class);
            if (cType != null && cType.value().equals(bitType)) {
                method = m;
                break;
            }
        }
        return method;
    }


    public Class<CommonInParams> getParamByBizType(BizTypeEnum bitType) {
        Reflections reflections = new Reflections(NameConstant.PACKAGE_NAME + ".Biz.Controller.Params");
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(BizType.class);
        for (Class<?> cls:classSet) {
            if (!CommonInParams.class.isAssignableFrom(cls))
                continue;
            Class<CommonInParams> paramCls = (Class<CommonInParams>)cls;
            BizType bizType1 = paramCls.getAnnotation(BizType.class);
            if (bizType1.value().equals(bitType))
                return paramCls;
        }

        return CommonInParams.class;
    }
}