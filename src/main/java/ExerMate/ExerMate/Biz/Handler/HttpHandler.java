package ExerMate.ExerMate.Biz.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Base.Constant.NameConstant;
import ExerMate.ExerMate.Biz.BizTypeEnum;
import ExerMate.ExerMate.Base.Error.ExerMateWarn;
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
 * http??????????????? ???????????? ???????????????????????? http ??????????????? ?????? ????????? channelRead0 ???????????? ??????
 **/
@Component
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Autowired
    private Dispatcher dispatcher;

    /** ????????? http session??? ??????????????? ??????*/
    private ThreadLocal<Boolean> hasPreSession = new ThreadLocal<>();

    /** http ????????? ???????????? ?????? ?????? */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) {
        JSONObject requestParams = new JSONObject();
        BizTypeEnum bizTypeEnum = null;
        try {
            /** ????????? ???????????? ???????????? ??????????????? ?????? ?????? ?????? ????????? ????????? ?????? */
            ThreadUtil.clean();
            hasPreSession.remove();
            /** session??? ?????? ?????? session??? ??????, ????????? session ?????? ?????? */
            getSession(request);
            /** ???????????? ???????????? ?????? */
            requestParams = getRequestParams(request);

            /** ??????????????? ?????? */
            try {
                String path = requestParams.getString(KeyConstant.PATH);
                bizTypeEnum = getBizTypeByPath(path);
                if (bizTypeEnum == null)
                    throw new ExerMateWarn(SystemErrorEnum.BIZ_TYPE_NOT_EXIST);
                requestParams.put(KeyConstant.BIZ_TYPE, bizTypeEnum);
            } catch (Exception e) {
                throw new ExerMateWarn(SystemErrorEnum.BIZ_TYPE_NOT_EXIST);
            }

            /** ??????????????? ?????? ???????????? ?????? */
            Class<CommonInParams> clz = dispatcher.getParamByBizType(bizTypeEnum);
            CommonInParams params = clz.newInstance();
            params.fromJsonObject(requestParams);

            /** ????????? ?????? ???????????? ????????? ?????? ?????? */
            if (hasPreSession.get() && !bizTypeEnum.equals(BizTypeEnum.USER_LOGIN) && !bizTypeEnum.equals(BizTypeEnum.USER_SIGNUP)) {
                params.setUseremail(ThreadUtil.getHttpSession().getUseremail());
            }

            /** dispatcher??? ????????? ?????? ?????? ??? ?????? ?????? */
            String retStr;
            retStr = dispatcher.dispatch(params);
            writeResponse(channelHandlerContext, retStr, request);
        } catch (Exception e) {
            String retStr;
            if (e instanceof ExerMateWarn) {
                ExerMateWarn exerMateWarn = (ExerMateWarn)e;
                /** ???????????? ??????*/
                LogUtil.WARN(null, bizTypeEnum, ParseUtil.getJSONString(requestParams), exerMateWarn);
                /**  ?????? ?????? ??????*/
                retStr = new SysWarnOutParams(exerMateWarn).toString();
            } else {
                /** ????????????, ???????????? */
                LogUtil.ERROR(null, bizTypeEnum, ParseUtil.getJSONString(requestParams), e);
                retStr = new SysErrorOutParams().toString();
            }
            writeResponse(channelHandlerContext, retStr, request);
        }
    }


    private JSONObject getRequestParams(FullHttpRequest request) throws IOException {
        JSONObject params = new JSONObject();


        String uri = request.uri();
        String[] uriParams = uri.split("\\?");
        params.put(KeyConstant.PATH, uriParams[0]);
        if (uriParams.length > 1) {
            String[] paramList = uriParams[1].split("&");
            for (String param:paramList) {
                String[] keyVal = param.split("=");
                if (keyVal.length < 2)
                    continue;
                params.put(keyVal[0], keyVal[1]);
            }
        }

        if(request.content().isReadable()) {
            String jsonStr = request.content().toString(CharsetUtil.UTF_8);
            try {
                params.putAll(JSON.parseObject(jsonStr));
            } catch (Exception e) {

            }
        }
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(DefaultHttpDataFactory.MAXSIZE), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : httpPostData) {
            Object obj = params.get(data.getName());

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

            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MixedAttribute attribute = (MixedAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {

                FileUpload fileUpload = (FileUpload)data;
                params.put(data.getName(), fileUpload);
            }
        }
        return params;
    }


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

    private void getSession(FullHttpRequest msg) {
        boolean hasPre = false;
        HttpSession httpSession = null;
        String cookieStr = msg.headers().get(HttpHeaderNames.COOKIE);
        if (cookieStr != null && !cookieStr.equals("")) {
            Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (Cookie cookie:cookieSet) {
                if (cookie.name().equals(NameConstant.HTTP_SESSION_NAME))
                    if (HttpSession.sessionExist(cookie.value())) {
                        httpSession = HttpSession.getSession(cookie.value());
                        hasPre = true;
                        break;
                    }
            }
        }
        hasPreSession.set(hasPre);

        if (httpSession == null)
            httpSession = HttpSession.newSession();

        ThreadUtil.setHttpSession(httpSession);
    }


    private void writeResponse(ChannelHandlerContext ctx, String content, FullHttpRequest request) {

        ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, NameConstant.DEFAULT_CONTENT);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "content-type");
        String clientIP = request.headers().get("Origin");
        if (clientIP != null)
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, clientIP);

        if (!hasPreSession.get()) {
            Cookie cookie = new DefaultCookie(NameConstant.HTTP_SESSION_NAME, ThreadUtil.getHttpSession().getSessionID());
            cookie.setPath("/");
            String encodeCookie = ServerCookieEncoder.STRICT.encode(cookie);
            response.headers().set(HttpHeaderNames.SET_COOKIE,encodeCookie);
        }


        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

