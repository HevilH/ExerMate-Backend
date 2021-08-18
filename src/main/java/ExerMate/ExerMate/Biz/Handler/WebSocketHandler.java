package ExerMate.ExerMate.Biz.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Base.Error.SystemErrorEnum;
import ExerMate.ExerMate.Base.Error.UserWarnEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out.SysErrorOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out.SysWarnOutParams;
import ExerMate.ExerMate.Biz.Dispatcher;
import ExerMate.ExerMate.Frame.Util.LogUtil;
import ExerMate.ExerMate.Frame.Util.SocketUtil;
import ExerMate.ExerMate.Frame.Util.ThreadUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @描述 장기간 웹소켓 접속을 처리하기 위한 클래스
 **/
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    @Autowired
    Dispatcher dispatcher;

    /** 웹소켓 연결을 처리하기 위한 함수*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        BizTypeEnum bizTypeEnum = null;
        JSONObject jsonMsg = new JSONObject();
        String useremail = null;
        String retStr = new SysErrorOutParams().toString();
        try {
            /** 텍스트형식의 데이터만 처리*/
            if (!(msg instanceof TextWebSocketFrame)) {
                throw new CourseWarn(SystemErrorEnum.PARAMS_ERROR);
            }
            /** 매개변수를 얻고 비즈니스를 정함 */
            try {
                jsonMsg = JSON.parseObject(((TextWebSocketFrame)msg).text());
                String bizTypeStr = jsonMsg.getString(KeyConstant.BIZ_TYPE);
                bizTypeEnum = BizTypeEnum.valueOf(bizTypeStr);
                jsonMsg.put(KeyConstant.BIZ_TYPE, bizTypeEnum);
            } catch (Exception e) {
                throw new CourseWarn(SystemErrorEnum.PARAMS_TRANSFER_ERROR);
            }
            /** 执行业务的线程存在复用情况，需要清除以前的线程变量 */
            ThreadUtil.clean();
            /** 保存管道，方便以后对该用户发送消息 */
            ThreadUtil.setCtx(ctx);
            /** 根据操作类型获取参数 */
            Class<CommonInParams> clz = dispatcher.getParamByBizType(bizTypeEnum);
            CommonInParams params = clz.newInstance();
            params.fromJsonObject(jsonMsg);
            /** 如果不是登录，需要获取用户信息 */
            if (!bizTypeEnum.equals(BizTypeEnum.USER_LOGIN)) {
                useremail = SocketUtil.getSocketUser(ctx);
                params.setUseremail(useremail);
            } else {
                useremail = params.getUseremail();
                if (useremail == null)
                    throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);
            }
            /** 执行业务 */
            retStr = dispatcher.dispatch(params);
        } catch (Exception e) {
            /** 处理出错，记录警告日志或者错误日志 */
            if (e instanceof CourseWarn) {
                CourseWarn warn = (CourseWarn)e;
                retStr = new SysWarnOutParams(warn).toString();
                LogUtil.WARN(useremail, bizTypeEnum, jsonMsg.toString(), warn);
            } else {
                retStr = new SysErrorOutParams().toString();
                LogUtil.ERROR(useremail, bizTypeEnum, jsonMsg.toString(), e);
            }
        } finally {
            ctx.channel().write(new TextWebSocketFrame(retStr));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }
}
