package ExerMate.ExerMate.Frame.Util;

import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Biz.Controller.Params.CommonInParams;
import ExerMate.ExerMate.Biz.Handler.TimerJobHandler;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulerUtil {

    @Autowired
    private Scheduler scheduler;

    public void deleteJob(String id) throws SchedulerException {
        scheduler.deleteJob(new JobKey(id));
    }

    public void addScheduleJob(CommonInParams params, int timeDelay, String id) throws Exception {
        addScheduleJob(params, timeDelay, 2000, 0, id);
    }

    public void addScheduleJob(CommonInParams params, Date date, String id) throws Exception {
        addScheduleJob(params, date, 2000, 0, id);
    }

    public void addScheduleJob(CommonInParams params, int timeDelay, int interval, int nums, String id) throws Exception {
        addScheduleJob(params, new Date(System.currentTimeMillis() + timeDelay), interval, nums, id);
    }

    public void addScheduleJob(CommonInParams params, Date startTime, int interval, int nums, String id) throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(TimerJobHandler.class).withIdentity(id).build();
        jobDetail.getJobDataMap().put(KeyConstant.PARAMS, params);

        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().startAt(startTime);

        if (nums > 0) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(nums);
            triggerBuilder.withSchedule(scheduleBuilder);
        } else if (nums == -1) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(10000);
            triggerBuilder.withSchedule(scheduleBuilder);
        }

        scheduler.scheduleJob(jobDetail, triggerBuilder.build());
        scheduler.start();
    }
}

