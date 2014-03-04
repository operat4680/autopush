package kr.co.autopush.scheduler;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class PushSchedulerServlet extends HttpServlet{


	@Override
	public void init() {
		try{
		SchedulerFactory schedFact = new StdSchedulerFactory();
		 Scheduler sched = schedFact.getScheduler();

		 sched.start();
		
		
		JobDetail taskJob = org.quartz.JobBuilder.newJob(TaskJob.class)
				.withIdentity("taskJob", "group1")
				.build();
		Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
				.withIdentity("trigger", "group1")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(5)	
						.repeatForever())            
						.build();
		JobDetail dispatchJob = org.quartz.JobBuilder.newJob(DispatchJob.class)
				.withIdentity("dispatchJob", "group1")
				.build();
		Trigger trigger2 = org.quartz.TriggerBuilder.newTrigger()
				.withIdentity("trigger2", "group1")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(30)	
						.repeatForever())            
						.build();


		
		sched.scheduleJob(taskJob, trigger);
		sched.scheduleJob(dispatchJob, trigger2);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
