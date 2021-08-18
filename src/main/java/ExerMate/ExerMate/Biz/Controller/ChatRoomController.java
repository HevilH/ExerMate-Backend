package ExerMate.ExerMate.Biz.Controller;

import ExerMate.ExerMate.Base.Annotation.NeedLogin;
import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Base.Error.UserWarnEnum;
import ExerMate.ExerMate.Base.Model.User;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In.EnterInParams;
import ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In.MakeInParams;
import ExerMate.ExerMate.Biz.Processor.ChatRoomProcessor;
import ExerMate.ExerMate.Frame.Util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomController {

    @Autowired
    ChatRoomProcessor chatRoomProcessor;

    @NeedLogin
    @BizType(BizTypeEnum.CHATROOM_MAKE)
    public CommonOutParams chatRoomMake(MakeInParams inParams) throws Exception{
        String hostemail = inParams.getUseremail();
        String chatRoomName = inParams.getChatRoomName();
        chatRoomProcessor.makeChatRoom(hostemail, chatRoomName);
        return new CommonOutParams(true);
    }

    @NeedLogin
    @BizType(BizTypeEnum.CHATROOM_ENTER)
    public CommonOutParams chatRoomEnter(EnterInParams inParams) throws Exception{
        String guestemail = inParams.getUseremail();
        String chatRoomID = inParams.getChatRoomID();
        chatRoomProcessor.addGuest(chatRoomID, guestemail);
        return new CommonOutParams(true);
    }

    @NeedLogin
    @BizType(BizTypeEnum.SEND_MSG)
    public CommonOutParams chatRoomEnter(CommonInParams inParams) throws Exception{
        return new CommonOutParams(true);
    }
}
