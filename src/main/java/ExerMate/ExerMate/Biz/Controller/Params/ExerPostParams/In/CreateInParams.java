package ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;

@BizType(BizTypeEnum.EXERPOST_CREATE)
public class CreateInParams extends CommonInParams {

    @Required
    private String exerName;

    @Required
    private String exerPlace;

    @Required
    private Long exerTime;

    @Required
    private int maxNum;

    @Required
    private String contents;

    @Required
    private String chatRoomName;

    public String getExerName() {
        return exerName;
    }
    public void setExerName(String exerName) {
        this.exerName = exerName;
    }

    public String getExerPlace() {
        return exerPlace;
    }
    public void setExerPlace(String exerPlace) {
        this.exerPlace = exerPlace;
    }

    public Long getExerTime() {
        return exerTime;
    }
    public void setExerTime(Long exerTime) {
        this.exerTime = exerTime;
    }

    public int getMaxNum() {
        return maxNum;
    }
    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }
    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

}
