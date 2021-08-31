package ExerMate.ExerMate.Frame.Util;

import com.alibaba.fastjson.JSONArray;
import ExerMate.ExerMate.Biz.Controller.Params.CommonOutParams;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;


public class SocketUtil {

    private static ConcurrentHashMap<String, ChannelHandlerContext> socketMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<ChannelHandlerContext, String> ctxToUser = new ConcurrentHashMap<>();


    public static void setUserSocket(String username, ChannelHandlerContext ctx) {
        ChannelHandlerContext preCtx = socketMap.get(username);
        if (preCtx != null)
            ctxToUser.remove(preCtx);
        socketMap.put(username, ctx);
        ctxToUser.put(ctx, username);
    }

    public static String getSocketUser(ChannelHandlerContext ctx) {
        return ctxToUser.get(ctx);
    }

    public static void removeSocket(ChannelHandlerContext ctx) {
        String username = ctxToUser.get(ctx);
        ctxToUser.remove(ctx);
        if (username != null) {
            socketMap.remove(username);
        }
    }

    public static void sendMessageToUser(String username, CommonOutParams msgs) throws Exception {
        ChannelHandlerContext ctx = socketMap.get(username);
        if (ctx != null && ctx.channel().isActive()) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(msgs.toString()));
        }
    }


}
