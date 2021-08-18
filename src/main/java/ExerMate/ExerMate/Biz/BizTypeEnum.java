package ExerMate.ExerMate.Biz;

import ExerMate.ExerMate.Biz.Controller.TimerController;
import ExerMate.ExerMate.Biz.Controller.UserController;
import ExerMate.ExerMate.Biz.Controller.ExerPostController;
import ExerMate.ExerMate.Biz.Controller.ChatRoomController;


public enum BizTypeEnum {
    /** http 비즈니스 타입*/
    USER_SIGNUP(UserController.class, "/user/signup", "회원가입"),

    USER_LOGIN(UserController.class, "/user/login", "로그인"),
    USER_LOGOUT(UserController.class, "/user/logout", "로그아웃"),

    USER_SET_STATUSMSG(UserController.class, "/user/setStatusmsg", "상태메시지"),
    USER_SET_PROFILE(UserController.class, "/user/setProfile", "프로필사진"),
    USER_ADD_WALKRECORD(UserController.class, "/user/addWalkrecord", "걸음기록 추가"),
    USER_GET_DATA(UserController.class, "/user/getData", "전체정보"),

    EXERPOST_MAKE(ExerPostController.class, "/exerpost/make", "홍보글 만들기"),
    EXERPOST_ENTER(ExerPostController.class, "/exerpost/enter", "홍보글 들어가기"),

    CHATROOM_MAKE(ChatRoomController.class, "/chatroom/make", "채팅방 만들기"),
    CHATROOM_ENTER(ChatRoomController.class, "/chatroom/enter", "채팅방 입장"),

    /** websocket 비즈니스 타입*/
    SEND_MSG(ChatRoomController.class, null, "메시지 보내기"),
    /** 定时任务业务测试 */
    LOG_TEST(TimerController.class, null, "定时日志测试"),
    ;

    BizTypeEnum(Class<?> controller, String httpPath, String description) {
        this.controller = controller;
        this.description = description;
        this.httpPath = httpPath;
    }


    /** 执行业务具体的类 */
    Class<?> controller;
    /** 业务对应的http请求路径 */
    String httpPath;
    /** 业务描述 */
    String description;

    public Class<?> getControllerClass() {
        return controller;
    }

    public String getDescription() {
        return description;
    }

    public String getHttpPath() {
        return httpPath;
    }
}
