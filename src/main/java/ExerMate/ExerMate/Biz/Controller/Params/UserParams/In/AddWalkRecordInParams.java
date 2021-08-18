package ExerMate.ExerMate.Biz.Controller.Params.UserParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.USER_ADD_WALKRECORD)
public class AddWalkRecordInParams extends CommonInParams {
    @Required
    private String walkNum;

    @Required
    private String date;

    public String getWalkNum() {
        return walkNum;
    }

    public void setWalkNum(String walkNum) {
        this.walkNum = walkNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String Date) {
        this.date = date;
    }
}
