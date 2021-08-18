package ExerMate.ExerMate.Biz.Controller;

import ExerMate.ExerMate.Base.Annotation.NeedLogin;
import ExerMate.ExerMate.Base.Annotation.BizType;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Base.Error.UserWarnEnum;
import ExerMate.ExerMate.Base.Model.User;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.UserParams.Out.SetProfileOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.UserParams.In.LoginInParams;
import ExerMate.ExerMate.Biz.Controller.Params.UserParams.In.SignupInParams;
import ExerMate.ExerMate.Biz.Controller.Params.UserParams.In.SetProfileInParams;
import ExerMate.ExerMate.Biz.Controller.Params.UserParams.In.SetStatusMSGInParams;
import ExerMate.ExerMate.Biz.Controller.Params.UserParams.In.AddWalkRecordInParams;
import ExerMate.ExerMate.Biz.Processor.UserProcessor;
import ExerMate.ExerMate.Frame.Util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 유저 컨트롤러, 유저의 관한 비즈니스를 처리
 **/
@Component
public class UserController {

    @Autowired
    UserProcessor userProcessor;

    /** 유저 로그인 비즈니스 */
    @BizType(BizTypeEnum.USER_LOGIN)
    public CommonOutParams userLogin(LoginInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        if (useremail == null)
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);
        User user = userProcessor.getUserByUseremail(useremail);
        if (user == null || !user.getPassword().equals(inParams.getPassword()))
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);

        /** 로그인성공, 로그인 상태를 기록*/
        ChannelHandlerContext ctx =  ThreadUtil.getCtx();
        /** ctx가 웹소켓의 상태를 저장할 수 없으면 http소켓의 상태를 저장 */
        if (ctx != null)
            SocketUtil.setUserSocket(useremail, ctx);
        else {
            HttpSession httpSession = ThreadUtil.getHttpSession();
            if (httpSession != null) {
                httpSession.setUseremail(useremail);
            }
        }
        return new CommonOutParams(true);
    }

    /** 유저 회원가입 비즈니스*/
    @BizType(BizTypeEnum.USER_SIGNUP)
    public CommonOutParams userSignup(SignupInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        if (useremail == null)
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);

        User user = userProcessor.getUserByUseremail(useremail);
        if(user != null)
            throw new CourseWarn(UserWarnEnum.EXISTED_EMAIL);
        userProcessor.addUser(useremail, inParams.getPassword(), inParams.getNickName());
        return new CommonOutParams(true);
    }

    @NeedLogin
    @BizType(BizTypeEnum.USER_SET_PROFILE)
    public CommonOutParams userSetProfile(SetProfileInParams inParams) throws Exception {
        SetProfileOutParams outParams = new SetProfileOutParams();
        String useremail = inParams.getUseremail();
        FileUpload file = inParams.getFile();
        File rawfile = new File(NameConstant.RES_PATH + useremail + ".jpg");
        rawfile.createNewFile();
        FileChannel inputChannel = new FileInputStream(file.getFile()).getChannel();
        FileChannel outputChannel = new FileOutputStream(rawfile).getChannel();
        outputChannel.transferFrom(inputChannel,0 , inputChannel.size());
        if (file == null)
            outParams.setSuccess(false);
        else {
            outParams.setSuccess(true);
            outParams.setFilename(file.getFilename());
            outParams.setFileSize(file.getFile().length());
        }
        User nowUser = ThreadUtil.getUser();
        if(nowUser.getProfileRoute().equals(""))
            userProcessor.setProfile(useremail);
        return outParams;
    }

    @NeedLogin
    @BizType(BizTypeEnum.USER_SET_STATUSMSG)
    public CommonOutParams userSetStatusMSG(SetStatusMSGInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        String statusMsg = inParams.getStatusMSG();
        userProcessor.setStatusMSG(useremail, statusMsg);
        return new CommonOutParams(true);
    }

    @NeedLogin
    @BizType(BizTypeEnum.USER_ADD_WALKRECORD)
    public CommonOutParams userSetProfile(AddWalkRecordInParams inParams) throws Exception {
        String useremail = inParams.getUseremail();
        User.WalkNum walkNum = new User.WalkNum();
        walkNum.setWalkNum(inParams.getWalkNum());
        walkNum.setDate(inParams.getDate());
        userProcessor.addWalkNum(useremail, walkNum);
        return new CommonOutParams(true);
    }
}