package ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.EXERPOST_GET_EXERPOST)
public class GetExerPostInParams extends CommonInParams {

    @Required
    private String exerPostID;

    public String getExerPostID() {
        return exerPostID;
    }
    public void setExerPostID(String exerPostID) {
        this.exerPostID = exerPostID;
    }
}
