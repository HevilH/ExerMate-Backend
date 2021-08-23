package ExerMate.ExerMate.Biz.Controller;

import ExerMate.ExerMate.Base.Annotation.NeedLogin;
import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Model.ExerPost;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.In.CreateInParams;
import ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.In.GetExerPostInParams;
import ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.In.ModifyInParams;
import ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.Out.GetExerPostListOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.ExerPostParams.Out.GetExerPostOutParams;
import ExerMate.ExerMate.Biz.Processor.UserProcessor;
import ExerMate.ExerMate.Biz.Processor.ExerPostProcessor;
import ExerMate.ExerMate.Biz.Processor.ChatRoomProcessor;
import ExerMate.ExerMate.Frame.Util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Component
public class ExerPostController {

    @Autowired
    UserProcessor userProcessor;
    @Autowired
    ExerPostProcessor exerPostProcessor;
    @Autowired
    ChatRoomProcessor chatRoomProcessor;

    @Autowired
    RedisUtil redisUtil;

    /** POST */

    @NeedLogin
    @BizType(BizTypeEnum.EXERPOST_CREATE)
    public CommonOutParams exerPostCreate(CreateInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        Long uploadTime = System.currentTimeMillis();
        String exerPostID = useremail +  String.valueOf(uploadTime);
        chatRoomProcessor.createChatRoom(useremail, exerPostID, inParams.getChatRoomName());
        exerPostProcessor.createExerPost(useremail, exerPostID, inParams.getExerName(), uploadTime, inParams.getExerPlace(), inParams.getExerTime(), inParams.getMaxNum(), inParams.getContents());
        userProcessor.joinChatRoom(useremail, exerPostID, inParams.getChatRoomName());
        redisUtil.addToSet(exerPostID, useremail);
        return new CommonOutParams(true);
    }

    @NeedLogin
    @BizType(BizTypeEnum.EXERPOST_MODIFY)
    public CommonOutParams exerPostModify(ModifyInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        exerPostProcessor.modifyExerPost(inParams.getExerPostID(), inParams.getExerName(), inParams.getExerPlace(), inParams.getExerTime(), inParams.getMaxNum(), inParams.getContents());
        return new CommonOutParams(true);
    }

    /** GET */

    @NeedLogin
    @BizType(BizTypeEnum.EXERPOST_GET_EXERPOST)
    public CommonOutParams exerPostGetExerPost(GetExerPostInParams inParams) throws Exception {
        GetExerPostOutParams outParams = new GetExerPostOutParams();
        String exerPostID = inParams.getExerPostID();
        ExerPost exerPost = exerPostProcessor.getExerPostByID(exerPostID);
        outParams.setExerName(exerPost.getExerName());
        outParams.setUploadTime(exerPost.getUploadTime());
        outParams.setHostemail(exerPost.getUserEmail());
        outParams.setExerPlace(exerPost.getExerPlace());
        outParams.setExerTime(exerPost.getExerTime());
        outParams.setContents(exerPost.getContents());
        outParams.setSuccess(true);
        return outParams;
    }

    @NeedLogin
    @BizType(BizTypeEnum.EXERPOST_GET_EXERPOST_LIST)
    public List<CommonOutParams> exerGetExerPostList(CommonInParams  inParams) throws Exception {
        List<CommonOutParams> retParams = new ArrayList<>();
        List<ExerPost> exerPosts = exerPostProcessor.getAllExerPost();
       for(ExerPost exerPost : exerPosts){
            GetExerPostListOutParams outParams = new GetExerPostListOutParams();
            outParams.setExerPostID(exerPost.getExerPostId());
            outParams.setExerName(exerPost.getExerName());
            outParams.setUploadTime(exerPost.getUploadTime());
            outParams.setHostemail(exerPost.getUserEmail());
            outParams.setSuccess(true);
            retParams.add(outParams);
        }
       return retParams;
    }
}
