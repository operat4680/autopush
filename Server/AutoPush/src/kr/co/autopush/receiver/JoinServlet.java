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

import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.util.JsonUtil;

import org.json.JSONObject;
@WebServlet("/JoinServlet")
public class JoinServlet extends HttpServlet{
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			boolean flag=false;
			boolean error=false;
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			try {
				JSONObject json=null;
				StringBuilder param = new StringBuilder();
				String s;
				while((s = request.getReader().readLine())!=null)param.append(s);
				if(param!=null){
					json = new JSONObject(param.toString());
					flag = MongoDAO.insertUser(JsonUtil.tansferDBObject(json));
				}
			}catch(Exception e){
				e.printStackTrace();
				error=true;
			}finally{
				if(error){
					out.write("error");
				}
				
				if(!flag){
					out.write("duplicate");
				}
				
				else{
					out.write("success");
				}
			}
			
	}
}
