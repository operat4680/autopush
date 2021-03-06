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
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {

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
		if(param!=null){
			JSONObject obj;
			String json="";
			try {
				obj = new JSONObject(param.toString());
				json=MongoDAO.getTempImage(obj.getString("userId")).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.write(json);
			out.flush();
		}
	}

	
}
