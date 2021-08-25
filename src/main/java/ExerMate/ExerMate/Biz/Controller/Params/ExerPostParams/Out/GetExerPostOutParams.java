package ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class GetExerPostOutParams extends CommonOutParams {

    private String exerName;

    private Long uploadTime;

    private String hostemail;

    private String exerPlace;

    private String exerTime;

    private String contents;


    public void setExerName(String exerName) {
        this.exerName = exerName;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setHostemail(String hostemail) {
        this.hostemail = hostemail;
    }

    public void setExerPlace(String exerPlace) {
        this.exerPlace = exerPlace;
    }

    public void setExerTime(String exerTime) {
        this.exerTime = exerTime;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
