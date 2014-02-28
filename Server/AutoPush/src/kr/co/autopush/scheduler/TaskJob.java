package kr.co.autopush.scheduler;


import java.util.List;

import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.db.MongoDB;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.DBObject;

public class TaskJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("-------------------------------------");
		System.out.println("TaskJob executed");
		if(MongoDAO.isTaskData()){
			MongoDAO.updateTaskTime();
			List<DBObject> list = MongoDAO.taskTimeCheck();
			//System.out.println(list.size());
			//Todo list in check
			if(list!=null&&list.size()>0){
				MongoDAO.insertQueue(list);
			}
		}
		System.out.println("TaskJob end : ");
		System.out.println("-------------------------------------\n");
		
		
		
	}

}
