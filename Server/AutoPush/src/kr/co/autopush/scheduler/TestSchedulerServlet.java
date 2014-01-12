package kr.co.autopush.scheduler;
import javax.servlet.http.HttpServlet;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class TestSchedulerServlet extends HttpServlet{

	@Override
	public void init() {
		// TODO Auto-generated method stub
//		try{
//		SchedulerFactory schedFact = new StdSchedulerFactory();
//
//		 Scheduler sched = schedFact.getScheduler();
//
//		 sched.start();
//		
//		
//		JobDetail job = org.quartz.JobBuilder.newJob(TestJob.class)
//				.withIdentity("job", "group1")
//				.build();
//		
//		
//		Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
//				.withIdentity("trigger", "group1")
//				.startNow()
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
//						.withIntervalInSeconds(3)	
//						.repeatForever())            
//						.build();
//
//		job.getJobDataMap().put("test", "job");
//
//		
//		sched.scheduleJob(job, trigger);
//		} catch (SchedulerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
}
