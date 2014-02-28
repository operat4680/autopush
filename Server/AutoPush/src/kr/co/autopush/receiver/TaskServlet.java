package kr.co.autopush.receiver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBObject;

import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.JsonUtil;
@WebServlet("/TaskServlet")
public class TaskServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder param = new StringBuilder();
		String s;
		while((s = request.getReader().readLine())!=null)param.append(s);
		if(param!=null&&!param.equals("")){
			try {
				JSONObject obj = new JSONObject(param.toString());
				
				String type = obj.get("type").toString();
				
				if(type.equals("get")){
					String json=MongoDAO.getTaskListById(obj.get("userId").toString()).toString();
					out.write(json);
					out.flush();
				}
				else if(type.equals("task")){
					DBObject result = MongoDAO.getTaskDataById(obj.get("id").toString());
					out.write(result.toString());
					out.flush();
					
				}
				else if(type.equals("delete")){
					MongoDAO.deleteTaskById(obj.get("taskId").toString());
					out.write("success");
					out.flush();
				}
				else if(type.equals("update")){
					obj.remove("type");
					MongoDAO.updateTask(JsonUtil.tansferDBObject(obj));
					out.write("update");
					out.flush();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
