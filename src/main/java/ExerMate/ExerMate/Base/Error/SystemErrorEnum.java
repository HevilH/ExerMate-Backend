package ExerMate.ExerMate.Base.Error;

public enum SystemErrorEnum implements ExceptionInterface {
    CONTROLLER_NOT_FIND("SysError001", "실행할 비즈니스 클래스를 찾을 수 없습니다"),

    METHOD_NOT_FIND("SysError002", "실행할 비즈니스 함수를 찾을 수 없습니다"),

    PARAMS_ERROR("SysError003", "매개변수 갯수나 형식이 다릅니다"),

    PARAMS_TRANSFER_ERROR("SysError004", "매개변수 전환이 안됩니다"),

    RETURN_PARAMS_ERROR("SysError005", "비즈니스 리턴 변수에 에러가 있습니다"),

    BIZ_TYPE_NOT_EXIST("SysError006", "비즈니스 타입이 존재하지 않습니다")
    ;

    SystemErrorEnum(String code, String msg) {
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