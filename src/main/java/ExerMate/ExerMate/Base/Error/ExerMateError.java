package ExerMate.ExerMate.Base.Error;

public class ExerMateError extends Exception {

    private String errorCode;

    private String errorMsg;

    public ExerMateError(ExceptionInterface exceptionInterface) {
        errorCode = exceptionInterface.getErrorCode();
        errorMsg = exceptionInterface.getErrorMessage();
    }

    public ExerMateError(String code, String msg) {
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