package ExerMate.ExerMate.Frame.Aspect;

import ExerMate.ExerMate.Base.Annotation.NeedLogin;
import ExerMate.ExerMate.Base.Enum.UserType;
import ExerMate.ExerMate.Base.Error.ExerMateWarn;
import ExerMate.ExerMate.Base.Error.UserWarnEnum;
import ExerMate.ExerMate.Base.Model.User;
import ExerMate.ExerMate.Frame.Util.ThreadUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAspect {
    @Around("@annotation(ExerMate.ExerMate.Base.Annotation.NeedLogin)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        User user = ThreadUtil.getUser();
        if (user == null)
            throw new ExerMateWarn(UserWarnEnum.NEED_LOGIN);
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        NeedLogin need_login = signature.getMethod().getAnnotation(NeedLogin.class);
        UserType[] userTypes = need_login.value();

        if (userTypes.length != 0) {
            boolean canAccess = false;
            for(UserType userType:userTypes){
                if(userType.equals(user.getUserType())){
                    canAccess = true;
                    break;
                }
            }
            if(!canAccess)
                throw new ExerMateWarn(UserWarnEnum.PERMISSION_DENIED);
        }
        return pjp.proceed();
    }

}
