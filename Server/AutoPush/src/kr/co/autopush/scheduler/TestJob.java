package kr.co.autopush.scheduler;

import kr.co.autopush.constant.Constant;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		JobDetail temp = context.getJobDetail();
		System.out.println(temp.getJobDataMap().get("test")+ " "+ temp.getKey());
	}

}
