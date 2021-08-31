package ExerMate.ExerMate.Base.Error;

public enum UserWarnEnum implements ExceptionInterface {
    LOGIN_FAILED("UserWarn001", "이메일이 틀립니다"),

    NEED_LOGIN("UserWarn002", "로그인하지 않았거나 너무 오래되어 로그아웃된 상태입니다"),

    PERMISSION_DENIED("UserWarn003", "방문 권한이 없습니다"),

    EXISTED_EMAIL("UserWarn004", "이미 존재하는 이메일입니다"),

    JOINED_CHATROOM("UserWarn004", "이미 참가한 채팅방입니다")
    ;

    UserWarnEnum(String code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    private String errorCode;
    private String errorMsg;
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMsg;
    }
}
