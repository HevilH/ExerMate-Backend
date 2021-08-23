package ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class GetExerPostListOutParams extends CommonOutParams{

    public String exerPostID;

    public String exerName;

    public Long uploadTime;

    public String hostemail;

    public void setExerPostID(String exerPostID) {
        this.exerPostID = exerPostID;
    }

    public void setExerName(String exerName) {
        this.exerName = exerName;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setHostemail(String hostemail) {
        this.hostemail = hostemail;
    }
}
