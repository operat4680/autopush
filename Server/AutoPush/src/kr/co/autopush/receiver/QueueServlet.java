package kr.co.autopush.receiver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.autopush.bean.PushData;
import kr.co.autopush.changedetection.ChangeCheck;
import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.GcmPush;
import kr.co.autopush.util.JsonUtil;

import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
@WebServlet("/QueueServlet")
public class QueueServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			String queueId = request.getParameter("queueid");
			String op = request.getParameter("op");
			System.out.println("queueId"+queueId);
			System.out.println("type:"+op);
			if(op.equals("get")){
				BasicDBList list = MongoDAO.getTaskDataList(queueId);
				
				if(list.size()>0){
					DBObject resultObj = new BasicDBObject("taskList",list);
					out.write(resultObj.toString());
				}
				else{
					System.out.println("error");
					out.write("error");
				}
			}
			else if(op.equals("update")){
				try {
					JSONObject data = new JSONObject(request.getParameter("resultData"));
					DBObject dbObject = JsonUtil.tansferDBObject(data);
					BasicDBList tempList = (BasicDBList)dbObject.get("taskData");
					List<PushData> pushList = new ArrayList<PushData>();
					if(tempList!=null&&tempList.size()>0){
						BasicDBList list = new BasicDBList();
						BasicDBList errorList = new BasicDBList();
						for(int i=0;i<tempList.size();i++){
							BasicDBObject temp = (BasicDBObject)tempList.get(i);
							if(temp.get("imgData")==null){
								errorList.add(temp);
							}
							else{
								list.add(temp);
							}
						}
						
						if(errorList.size()>0){
							MongoDAO.updateTaskError(errorList);
						}
						
						for(int i=0;i<list.size();i++){
							BasicDBObject obj = (BasicDBObject)list.get(i);
							List<String> oldList = MongoDAO.getTextList(obj.get("id").toString());
							List<String> keyList = MongoDAO.getKeyList(obj.get("id").toString());
							ChangeCheck check = new ChangeCheck(obj.get("html").toString(),oldList,obj.get("tagPath").toString(),keyList);
							List<String> newText = check.checkChange();
							int status = check.getState();
							((BasicDBObject)list.get(i)).put("textList", newText);
							((BasicDBObject)list.get(i)).put("textstatus", status);
							if(newText!=null&&!newText.isEmpty()){
								
								//push resist
								if(status==1){
									PushData push = MongoDAO.getPushById(obj.getString("id").toString());
									if(push!=null){
										pushList.add(push);
									}
								}
							}
						}
						//change Detection
						
						GcmPush.sendPush(pushList);
						MongoDAO.timeReset(list);
						MongoDAO.deleteQueueById(queueId);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				out.write("parameter error");
			}
			
		
	
	}
}
