package ExerMate.ExerMate.Frame.Util;

import ExerMate.ExerMate.Base.Model.User;
import io.netty.channel.ChannelHandlerContext;


public class ThreadUtil {
    private static class ThreadParams {

        private User nowUser = null;

        private ChannelHandlerContext channelHandlerContext = null;

        private HttpSession httpSession;

        public User getNowUser() {
            return nowUser;
        }

        public void setNowUser(User nowUser) {
            this.nowUser = nowUser;
        }

        public ChannelHandlerContext getChannelHandlerContext() {
            return channelHandlerContext;
        }

        public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
            this.channelHandlerContext = channelHandlerContext;
        }

        public HttpSession getHttpSession() {
            return httpSession;
        }

        public void setHttpSession(HttpSession httpSession) {
            this.httpSession = httpSession;
        }
    }

    private static ThreadLocal<ThreadParams> paramsThreadLocal = new ThreadLocal<>();

    private static ThreadParams getThreadParams() {
        ThreadParams threadParams = paramsThreadLocal.get();
        if (threadParams == null) {
            threadParams = new ThreadParams();
            paramsThreadLocal.set(threadParams);
        }
        return threadParams;
    }
    public static String getUserID() {
        User user = getUser();
        if (user == null)
            return null;
        return user.getID();
    }

    public static User getUser() {
        return getThreadParams().getNowUser();
    }

    public static void setUser(User user) {
        getThreadParams().setNowUser(user);
    }

    public static void setCtx(ChannelHandlerContext ctx) {
        getThreadParams().setChannelHandlerContext(ctx);
    }

    public static ChannelHandlerContext getCtx() {
        return getThreadParams().getChannelHandlerContext();
    }

    public static void setHttpSession(HttpSession httpSession) {
        getThreadParams().setHttpSession(httpSession);
    }

    public static HttpSession getHttpSession() {
        return getThreadParams().getHttpSession();
    }

    public static void clean() {
        paramsThreadLocal.remove();
    }
}

