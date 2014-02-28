package kr.co.autopush.scheduler;

import java.util.List;

import kr.co.autopush.db.MongoDAO;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.mongodb.DBObject;

public class DispatchJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("-------------------------------------");
		System.out.println("DispatchJob executed");
		List<DBObject> list = MongoDAO.checkQueue();
		System.out.println(list.size());
		if(list!=null&&list.size()!=0){
			SchedulerFactory schedFact = new StdSchedulerFactory();

			 Scheduler sched;
			try {
				sched = schedFact.getScheduler();
				for(int i=0;i<list.size();i++){
					
				
					JobDetail taskJob = org.quartz.JobBuilder.newJob(CasperRunJob.class)
							.usingJobData("id", list.get(i).get("_id").toString())
							.build();
					Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
							.startNow()
							.build();
					
					sched.scheduleJob(taskJob, trigger);
				
				}
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.out.println("DispatchJob end : ");
//		System.out.println("-------------------------------------\n");

		
		
	}
}
