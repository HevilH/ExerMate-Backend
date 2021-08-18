
package ExerMate.ExerMate.Base.Annotation;

import ExerMate.ExerMate.Biz.BizTypeEnum;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizType {
    BizTypeEnum value();
}
