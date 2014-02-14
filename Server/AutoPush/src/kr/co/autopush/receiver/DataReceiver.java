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

import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.FileWriteUtil;
import kr.co.autopush.util.JsonUtil;








import com.mongodb.DBObject;
@WebServlet("/DataReceiver")
public class DataReceiver extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println("DataReceiver--------------------");
		String data = request.getParameter("jsondata");
		String update = request.getParameter("img");
		
		if(data!=null||update!=null){
			JSONObject json=null;
			try {
				
				if(data!=null){
					json = new JSONObject(data);
					DBObject dataObj= JsonUtil.tansferDBObject(json);
					System.out.println("insertData");
					MongoDAO.insertTempData(dataObj);
				}
				if(update!=null){
//					System.out.println(update.length());
//					FileWriteUtil.write(update);
					json = new JSONObject(update);
					DBObject dataObj= JsonUtil.tansferDBObject(json);
					System.out.println("updateData");
					MongoDAO.updateTempData(dataObj);
					
				}
				out.print("success");
				out.flush();
				System.out.println("exit");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		System.out.println("---------------------------");
	}

	
	
}
