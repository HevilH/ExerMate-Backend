package ExerMate.ExerMate.Biz.Controller;

import ExerMate.ExerMate.Base.Annotation.NeedLogin;
import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Base.Model.User;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Model.ChatRoom;
import ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.Out.SendMsgOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In.GetUserListInParams;
import ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.Out.GetUserListOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.ChatRoomParams.In.SendMsgInParams;
import ExerMate.ExerMate.Biz.Processor.UserProcessor;
import ExerMate.ExerMate.Biz.Processor.ChatRoomProcessor;
import ExerMate.ExerMate.Frame.Util.RedisUtil;
import ExerMate.ExerMate.Frame.Util.SocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class ChatRoomController {

    @Autowired
    ChatRoomProcessor chatRoomProcessor;
    @Autowired
    UserProcessor userProcessor;

    @Autowired
    RedisUtil redisUtil;

    @NeedLogin
    @BizType(BizTypeEnum.CHATROOM_GET_USERLIST)
    public List<CommonOutParams> chatRoomGetUserList(GetUserListInParams inParams) throws Exception{
        List<CommonOutParams> retParams = new ArrayList<>();
        String chatRoomID = inParams.getChatRoomID();
        ChatRoom chatRoom = chatRoomProcessor.getChatRoomByID(chatRoomID);

        User user = userProcessor.getUserByUseremail(chatRoom.getHostEmail());
        GetUserListOutParams outParams = new GetUserListOutParams();
        outParams.setUseremail(user.getUserEmail());
        outParams.setNickName(user.getNickName());
        outParams.setProfileRoute(user.getProfileRoute());
        outParams.setSuccess(true);
        retParams.add(outParams);
        String [] guestemails = chatRoom.getGuestemails();
        for (int i = 0; i < guestemails.length; i++){
            outParams = new GetUserListOutParams();
            user = userProcessor.getUserByUseremail(guestemails[i]);
            outParams.setUseremail(user.getUserEmail());
            outParams.setNickName(user.getNickName());
            outParams.setProfileRoute(user.getProfileRoute());
            outParams.setSuccess(true);
            retParams.add(outParams);
        }
        return retParams;
    }

    @BizType(BizTypeEnum.SEND_MSG)
    public CommonOutParams sendMSG(SendMsgInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        SendMsgOutParams outParams = new SendMsgOutParams();
        outParams.setChatRoomID(inParams.getChatRoomID());
        outParams.setBizType("SEND_MSG");
        outParams.setUseremail(useremail);
        outParams.setText(inParams.getText());
        outParams.setSuccess(true);
        Set<String> chatRoomUsers = redisUtil.getSet(inParams.getChatRoomID());
        for(String chatRoomUser : chatRoomUsers){
            if(!useremail.equals(chatRoomUser))
                SocketUtil.sendMessageToUser(chatRoomUser, outParams);
        }
        return new CommonOutParams(true);
    }

}
