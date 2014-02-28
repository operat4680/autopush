package kr.co.autopush.receiver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
@WebServlet("/InsertData")
public class InsertData extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			StringBuilder param = new StringBuilder();
			String s;
			while((s = request.getReader().readLine())!=null)param.append(s);
			if(param.toString()!=null&&!param.toString().equals("")){
				JSONObject obj = new JSONObject(param.toString());
				MongoDAO.insertTask(JsonUtil.tansferDBObject(obj));
			}
			
			
			out.print("success");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
}
