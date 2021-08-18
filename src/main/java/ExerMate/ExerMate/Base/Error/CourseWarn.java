package ExerMate.ExerMate.Base.Error;

public class CourseWarn extends Exception {

    private String errorCode;
    private String errorMsg;

    public CourseWarn(ExceptionInterface exceptionInterface) {
        errorCode = exceptionInterface.getErrorCode();
        errorMsg = exceptionInterface.getErrorMessage();
    }

    public CourseWarn(String code, String msg) {
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