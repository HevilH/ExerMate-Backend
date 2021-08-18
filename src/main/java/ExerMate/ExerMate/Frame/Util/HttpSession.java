package ExerMate.ExerMate.Frame.Util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSession {
    private static final Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    private String sessionID;
    private String useremail;

    public HttpSession(){
        sessionID = UUID.randomUUID().toString();
    }

    public String getSessionID() { return sessionID; }

    public String getUseremail() {return useremail;}

    public void setUseremail(String useremail) {this.useremail = useremail;}

    public static boolean sessionExist(String sessionID) { return sessionMap.containsKey(sessionID);}

    public static HttpSession newSession(){
        HttpSession session = new HttpSession();
        String sessionID = session.getSessionID();
        sessionMap.put(sessionID, session);
        return session;
    }

    public static HttpSession getSession(String sessionID){ return sessionMap.get(sessionID);}

    public static void removeSession(String sessionID){ sessionMap.remove(sessionID);}

}
