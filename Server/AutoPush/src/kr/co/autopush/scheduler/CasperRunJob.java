package kr.co.autopush.scheduler;


import java.util.ArrayList;
import java.util.List;

import kr.co.autopush.constant.Constant;
import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.StreamGobbler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CasperRunJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			List<String> command = new ArrayList<String>();
			JobDetail temp = context.getJobDetail();
			String id=temp.getJobDataMap().getString("id");
			command.add(Constant.CASPERROOT);
			command.add(Constant.SLIMERENGINE);
			command.add(Constant.IGNORE);
			command.add(Constant.JOBSCRIPTPATH);
			command.add("\\\""+"http://211.189.127.143/AutoPush/QueueServlet"+"\\\"");
//			command.add("http://211.189.127.143/AutoPush/QueueServlet");
			command.add("\\\""+ id +"\\\"");
			System.out.println(id);
			
			long start = System.currentTimeMillis();
			Runtime rt = Runtime.getRuntime();
			Process proc;
			proc = rt.exec(command.toArray(new String[command.size()]));
			StreamGobbler error = new StreamGobbler(proc.getErrorStream());
			StreamGobbler console = new StreamGobbler(proc.getInputStream());
			error.run();
			console.run();
			
			int exitCode = proc.waitFor();
			proc.destroy();
			long end = System.currentTimeMillis();
			System.out.println( "실행 시간 : " + ( end - start )/1000.0 + "exit Code : "+ exitCode);
			MongoDAO.deleteQueueById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
