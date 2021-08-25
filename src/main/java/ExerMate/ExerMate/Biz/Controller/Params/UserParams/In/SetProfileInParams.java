package ExerMate.ExerMate.Biz.Controller.Params.UserParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import io.netty.handler.codec.http.multipart.FileUpload;

@BizType(BizTypeEnum.USER_SET_PROFILE)
public class SetProfileInParams extends CommonInParams {

    @Required
    private FileUpload file;


    public FileUpload getFile() { return file; }
}
