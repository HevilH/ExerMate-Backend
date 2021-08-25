package ExerMate.ExerMate.Biz.Controller.Params;


public class CommonOutParams extends CommonParams {

    protected boolean success;

    protected Long time;

    public CommonOutParams() {
        this.success = true;
    }

    public CommonOutParams(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    protected void beforeTransfer() {
        if (time == null)
            time = System.currentTimeMillis();
    }
}
