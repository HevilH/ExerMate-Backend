package ExerMate.ExerMate.Biz.Controller.Params.UserParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class GetWalkRecordOutParams extends CommonOutParams {

    private int walkNum;

    private Long date;

    public void setWalkNum(int walkNum) {
        this.walkNum = walkNum;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
