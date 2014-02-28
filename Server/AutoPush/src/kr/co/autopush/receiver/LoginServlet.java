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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			int flag=-1;
			String id=null;
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			try {
				JSONObject json=null;
				StringBuilder param = new StringBuilder();
				String s;
				while((s = request.getReader().readLine())!=null)param.append(s);
				if(param!=null){
					json = new JSONObject(param.toString());
					String type = json.getString("type");
					json.remove("type");
					if(type.equals("check")){
						id = MongoDAO.checkUser(JsonUtil.tansferDBObject(json));
						if(id!=null&&!id.equals("")){
							flag = 1;
						}
						else{
							json.remove("gcmId");
							id = MongoDAO.checkGcmKey(JsonUtil.tansferDBObject(json));
							if(id!=null&&!id.equals("")){
								flag = 0;
							}
						}
					}
					else if(type.equals("update")){
						id = MongoDAO.updateGcmKey(JsonUtil.tansferDBObject(json));
						if(id!=null&&!id.equals("")){
							flag = 1;
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					
					JSONObject result = new JSONObject();
					if(flag==1){
						result.put("message", "success");
						result.put("id", id);
						out.write(result.toString());
					}
					
					if(flag==0){
						result.put("message", "gcm");
						result.put("id", id);
						out.write(result.toString());
					}
					
					else{
						result.put("message", "error");
						out.write(result.toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	}
}
