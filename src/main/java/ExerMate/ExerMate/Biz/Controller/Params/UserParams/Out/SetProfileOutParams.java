package ExerMate.ExerMate.Biz.Controller.Params.UserParams.Out;

import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;

public class SetProfileOutParams extends CommonOutParams {

    private Long fileSize;

    private String filename;

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}