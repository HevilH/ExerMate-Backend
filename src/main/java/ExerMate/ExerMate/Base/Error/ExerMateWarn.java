package ExerMate.ExerMate.Base.Error;

public class ExerMateWarn extends Exception {

    private String errorCode;
    private String errorMsg;

    public ExerMateWarn(ExceptionInterface exceptionInterface) {
        errorCode = exceptionInterface.getErrorCode();
        errorMsg = exceptionInterface.getErrorMessage();
    }

    public ExerMateWarn(String code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
}