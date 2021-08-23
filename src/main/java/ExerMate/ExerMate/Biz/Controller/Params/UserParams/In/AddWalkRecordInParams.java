package ExerMate.ExerMate.Biz.Controller.Params.UserParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.USER_ADD_WALKRECORD)
public class AddWalkRecordInParams extends CommonInParams {
    @Required
    private int walkNum;

    @Required
    private Long date;

    public int getWalkNum() {
        return walkNum;
    }

    public void setWalkNum(int walkNum) {
        this.walkNum = walkNum;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long Date) {
        this.date = date;
    }
}
