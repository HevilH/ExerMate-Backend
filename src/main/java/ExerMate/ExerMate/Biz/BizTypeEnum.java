package ExerMate.ExerMate.Biz;

import ExerMate.ExerMate.Biz.Controller.TimerController;
import ExerMate.ExerMate.Biz.Controller.UserController;
import ExerMate.ExerMate.Biz.Controller.ExerPostController;
import ExerMate.ExerMate.Biz.Controller.ChatRoomController;


public enum BizTypeEnum {
    /** http 비즈니스 타입*/
    USER_SIGNUP(UserController.class, "/user/signup", "회원가입"),

    USER_LOGIN(UserController.class, "/auth/login", "로그인"),
    USER_LOGOUT(UserController.class, "/auth/logout", "로그아웃"),

    USER_SET_STATUSMSG(UserController.class, "/user/set-status-msg", "상태메시지 변경"),
    USER_SET_PROFILE(UserController.class, "/user/set-profile", "프로필사진 변경"),
    USER_ADD_WALKRECORD(UserController.class, "/user/add-walk-record", "걸음기록 추가"),
    USER_JOIN_CHATROOM(UserController.class, "/user/join-chat-room", "채팅방 참가"),
    USER_EXIT_CHATROOM(UserController.class, "/user/exit-chat-room", "채팅방 탈퇴"),

    USER_GET_DATA(UserController.class, "/user/get-data", "유저 정보조회"),
    USER_GET_WALKRECORD(UserController.class, "/user/get-walk-records", "걸음기록 조회"),
    USER_GET_CHATROOM(UserController.class, "/user/get-chat-room", "가입한 채팅방 조회"),

    EXERPOST_CREATE(ExerPostController.class, "/exerpost/create", "홍보글 생성"),
    EXERPOST_MODIFY(ExerPostController.class, "/exerpost/modify", "홍보글 변경"),
    EXERPOST_GET_EXERPOST(ExerPostController.class, "/exerpost/get-exer-post", "홍보글 정보조회"),
    EXERPOST_GET_EXERPOST_LIST(ExerPostController.class, "/exerpost/get-exer-post-list", "홍보글 리스트 조회"),

    CHATROOM_GET_USERLIST(ChatRoomController.class, "/chatroom/get-user-list", "채팅방 유저리스트 조회"),

    /** websocket 비즈니스 타입*/
    SEND_MSG(ChatRoomController.class, null, "메시지 보내기"),
    SEND_IMG(ChatRoomController.class, "/chatroom/send-img", "이미지 보내기"),

    LOG_TEST(TimerController.class, null, "타이머 테스트"),
    ;

    BizTypeEnum(Class<?> controller, String httpPath, String description) {
        this.controller = controller;
        this.description = description;
        this.httpPath = httpPath;
    }



    Class<?> controller;

    String httpPath;

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
