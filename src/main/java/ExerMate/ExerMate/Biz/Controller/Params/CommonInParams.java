package ExerMate.ExerMate.Biz.Controller.Params;

import ExerMate.ExerMate.Biz.BizTypeEnum;


/**
 * 통용적인 입력매개변수, 모든 입력 매개변수는 이 클래스를 상속
 **/
public class CommonInParams extends CommonParams {
    /** 리퀘스트를 보낸 유저가 이미 로그인 된 상태면 시스템에서 Http Session이나 WebSocket의 장기간 연결이 함께 자동으로 채워짐 */
    protected String useremail;

    /** 비즈니스 종류，서버는 이 변수에 따라 비즈니스를 진행 */
    protected BizTypeEnum bizType;

    public BizTypeEnum getBizType() {
        return bizType;
    }

    public void setBizType(BizTypeEnum bizType) {
        this.bizType = bizType;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    @Override
    protected void beforeTransfer() {}
}
