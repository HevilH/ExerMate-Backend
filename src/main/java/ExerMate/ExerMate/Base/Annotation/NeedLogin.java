package ExerMate.ExerMate.Base.Annotation;

import ExerMate.ExerMate.Base.Enum.UserType;
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedLogin {
    UserType[] value() default {};
}
