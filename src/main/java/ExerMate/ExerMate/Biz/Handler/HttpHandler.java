package ExerMate.ExerMate.Biz.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Error.CourseWarn;
import ExerMate.ExerMate.Base.Error.SystemErrorEnum;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out.SysErrorOutParams;
import ExerMate.ExerMate.Biz.Controller.Params.DefaultParams.Out.SysWarnOutParams;
import ExerMate.ExerMate.Biz.Dispatcher;
import ExerMate.ExerMate.Frame.Util.HttpSession;
import ExerMate.ExerMate.Frame.Util.LogUtil;
import ExerMate.ExerMate.Frame.Util.ParseUtil;
import ExerMate.ExerMate.Frame.Util.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * http리퀘스트를 처리하는 클래스，시스템은 http 리퀘스트를 받을 때마다 channelRead0 메소드를 호출
 **/
@Component
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Autowired
    private Dispatcher dispatcher;

    /** 이전에 http session이 존재했는지 검사*/
    private ThreadLocal<Boolean> hasPreSession = new ThreadLocal<>();

    /** http 요청을 처리하는 실제 함수 */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) {
        JSONObject requestParams = new JSONObject();
        BizTypeEnum bizTypeEnum = null;
        try {
            /** 업무를 수행하는 스레드의 중복사요을 막기 위해 이전 스레드 변수를 제거 */
            ThreadUtil.clean();
            hasPreSession.remove();
            /** session이 있을 경우 session을 획득, 없으면 session 새로 만듦 */
            getSession(request);
            /** 매개변수 리스트를 파싱 */
            requestParams = getRequestParams(request);

            /** 동작유형을 흭득 */
            try {
                String path = requestParams.getString(KeyConstant.PATH);
                bizTypeEnum = getBizTypeByPath(path);
                if (bizTypeEnum == null)
                    throw new CourseWarn(SystemErrorEnum.BIZ_TYPE_NOT_EXIST);
                requestParams.put(KeyConstant.BIZ_TYPE, bizTypeEnum);
            } catch (Exception e) {
                throw new CourseWarn(SystemErrorEnum.BIZ_TYPE_NOT_EXIST);
            }

            /** 동작유형에 따른 매개변수 흭득 */
            Class<CommonInParams> clz = dispatcher.getParamByBizType(bizTypeEnum);
            CommonInParams params = clz.newInstance();
            params.fromJsonObject(requestParams);

            /** 캐시에 있는 세션중의 유저명 정보 흭득 */
            if (hasPreSession.get() && !bizTypeEnum.equals(BizTypeEnum.USER_LOGIN) && !bizTypeEnum.equals(BizTypeEnum.USER_SIGNUP)) {
                params.setUseremail(ThreadUtil.getHttpSession().getUseremail());
            }

            /** dispatcher를 이용해 업무 실행 및 결가 리턴 */
            String retStr;
            retStr = dispatcher.dispatch(params);
            writeResponse(channelHandlerContext, retStr, request);
        } catch (Exception e) {
            String retStr;
            if (e instanceof CourseWarn) {
                CourseWarn courseWarn = (CourseWarn)e;
                /** 오류로그 기록*/
                LogUtil.WARN(null, bizTypeEnum, ParseUtil.getJSONString(requestParams), courseWarn);
                /**  오류 내용 리턴*/
                retStr = new SysWarnOutParams(courseWarn).toString();
            } else {
                /** 에러처리, 로그기록 */
                LogUtil.ERROR(null, bizTypeEnum, ParseUtil.getJSONString(requestParams), e);
                /** 返回客户端INTERNAL_SERVER_ERROR，即服务器内部错误 */
                retStr = new SysErrorOutParams().toString();
            }
            /** 将返回结果写入管道 */
            writeResponse(channelHandlerContext, retStr, request);
        }
    }

    /** 解析http请求的参数 */
    private JSONObject getRequestParams(FullHttpRequest request) throws IOException {
        JSONObject params = new JSONObject();

        /** 解析请求URI中的参数 */
        String uri = request.uri();
        String[] uriParams = uri.split("\\?");
        /** 保存请求的路径 */
        params.put(KeyConstant.PATH, uriParams[0]);
        /** 如果uri中存在参数，保存参数列表 */
        if (uriParams.length > 1) {
            String[] paramList = uriParams[1].split("&");
            for (String param:paramList) {
                String[] keyVal = param.split("=");
                if (keyVal.length < 2)
                    continue;
                params.put(keyVal[0], keyVal[1]);
            }
        }
        /** 解析请求体中的参数 */
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(DefaultHttpDataFactory.MAXSIZE), request);
        if(request.content().isReadable()) {
            String jsonStr = request.content().toString(CharsetUtil.UTF_8);
            try {
                params.putAll(JSON.parseObject(jsonStr));
            } catch (Exception e) {

            }
        }
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : httpPostData) {
            Object obj = params.get(data.getName());
            /** 如果之前存了对象，说明传了数组，转化为数组 */
            if (obj != null) {
                if (obj instanceof JSONArray)
                    ((JSONArray) obj).add(data);
                else {
                    JSONArray objArr = new JSONArray();
                    objArr.add(obj);
                    objArr.add(data);
                    params.put(data.getName(), objArr);
                }
                continue;
            }
            /** 普通属性直接赋值就可以了 */
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MixedAttribute attribute = (MixedAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                /** 文件参数需要特殊处理 */
                FileUpload fileUpload = (FileUpload)data;
                params.put(data.getName(), fileUpload);
            }
        }
        return params;
    }

    /** 根据httpPath获取对应的业务 */
    private BizTypeEnum getBizTypeByPath(String httpPath) {
        BizTypeEnum[] bizTypeEnums = BizTypeEnum.values();
        BizTypeEnum ret = null;
        for (BizTypeEnum bizTypeEnum:bizTypeEnums) {
            if (httpPath.equals(bizTypeEnum.getHttpPath())) {
                ret = bizTypeEnum;
                break;
            }
        }
        return ret;
    }

    /** 根据请求的cookie获取HttpSession */
    private void getSession(FullHttpRequest msg) {
        boolean hasPre = false;
        HttpSession httpSession = null;
        String cookieStr = msg.headers().get(HttpHeaderNames.COOKIE);
        if (cookieStr != null && !cookieStr.equals("")) {
            Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (Cookie cookie:cookieSet) {
                if (cookie.name().equals(NameConstant.HTTP_SESSION_NAME))
                /** 获取之前的session */
                    if (HttpSession.sessionExist(cookie.value())) {
                        httpSession = HttpSession.getSession(cookie.value());
                        hasPre = true;
                        break;
                    }
            }
        }
        hasPreSession.set(hasPre);
        /** 不存在session需要新建session */
        if (httpSession == null)
            httpSession = HttpSession.newSession();
        /** 将session存到线程变量中 */
        ThreadUtil.setHttpSession(httpSession);
    }

    /** 将内容写入返回管道中 */
    private void writeResponse(ChannelHandlerContext ctx, String content, FullHttpRequest request) {
        /** 将字符串写入response中 */
        ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, NameConstant.DEFAULT_CONTENT);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "content-type");
        String clientIP = request.headers().get("Origin");
        if (clientIP != null)
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, clientIP);

        /** 如果之前不存在session，需要设置一下session */
        if (!hasPreSession.get()) {
            Cookie cookie = new DefaultCookie(NameConstant.HTTP_SESSION_NAME, ThreadUtil.getHttpSession().getSessionID());
            cookie.setPath("/");
            String encodeCookie = ServerCookieEncoder.STRICT.encode(cookie);
            response.headers().set(HttpHeaderNames.SET_COOKIE,encodeCookie);
        }

        /** 写入管道中 */
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

