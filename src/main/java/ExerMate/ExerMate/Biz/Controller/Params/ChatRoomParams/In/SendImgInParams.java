package ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In;

import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Annotation.Required;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import io.netty.handler.codec.http.multipart.FileUpload;

@BizType(BizTypeEnum.SEND_IMG)
public class SendImgInParams extends CommonInParams {

    @Required
    String chatRoomID;

    @Required
    private FileUpload file;

    public FileUpload getFile() {
        return file;
    }


    public String getChatRoomID() {
        return chatRoomID;
    }
    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

}
