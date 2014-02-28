package kr.co.autopush.receiver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.JsonUtil;

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
			
			if(op.equals("get")){
				BasicDBList list = MongoDAO.getTaskDataList(queueId);
				if(list.size()>0){
					DBObject resultObj = new BasicDBObject("taskList",list);
					out.write(resultObj.toString());
				}
				else{
					out.write("error");
				}
			}
			else if(op.equals("update")){
				try {
//					System.out.println("wow length: !"+request.getParameter("resultData").length());
					JSONObject data = new JSONObject(request.getParameter("resultData"));
					DBObject dbObject = JsonUtil.tansferDBObject(data);
					BasicDBList tempList = (BasicDBList)dbObject.get("taskData");
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
						//change Detection
						
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
