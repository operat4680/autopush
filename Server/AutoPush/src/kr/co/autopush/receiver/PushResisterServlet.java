package kr.co.autopush.receiver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.autopush.bean.LoginData;
import kr.co.autopush.constant.Constant;
import kr.co.autopush.db.MongoDAO;
import kr.co.autopush.formfind.FindForm;
import kr.co.autopush.tabledetection.APTableDetector;
import kr.co.autopush.tabledetection.FrameFilter;
import kr.co.autopush.util.StreamGobbler;
import kr.co.autopush.util.UserException;

import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

@WebServlet("/PushResist")
public class PushResisterServlet extends HttpServlet {
	
	private String userId;
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			boolean flag=false;
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
		try {
			JSONObject json=null;
			List<String> command = new ArrayList<String>();
			StringBuilder param = new StringBuilder();
			String s;
			while((s = request.getReader().readLine())!=null)param.append(s);
			System.out.println(param.toString());
			if(param!=null){
				json = new JSONObject(param.toString());
				System.out.println("login : "+json.getBoolean("login"));
				userId = json.getString("userId");
			}
			
			if(json!=null){
					FrameFilter filter = new FrameFilter(json.getString("targetURL"));
					System.out.println(filter.getUrlListToJson());
					command.add(Constant.CASPERROOT);
					command.add(Constant.SLIMERENGINE);
					command.add(Constant.IGNORE);
					command.add(Constant.SCRIPTPATH);
//					command.add("\""+filter.getUrlListToJson().replace("\"", "\\\"")+"\"");
//					command.add(filter.getUrlListToJson().replace("\"", "\\\"").replace("%", "%%"));
					command.add(filter.getUrlListToJson().replace("\"", "\\\"-_-"));
//					command.add(filter.getUrlListToJson());
					command.add(Constant.RECEIVERURL);
					command.add("not");
					command.add(userId);
					if(json.getBoolean("login")){
						FindForm form = new FindForm(json.getString("formURL"));
						LoginData data = form.getLoginData();
						if(data.validate())throw new UserException("Login Form Not Found");
						if(data!=null){
							data.print();
							command.add(("\\\""+data.getLoginUrl()+"\\\""));
							command.add("\\\""+data.getFormPath()+"\\\"");
							command.add(data.getFormIdPath());
							command.add(data.getFormPasswdPath());
							command.add(data.getpPath());
							command.add(json.getString("id"));
							command.add(json.getString("pwd"));
						}
					}
//					System.out.println(command.toString());
					
					runProcess(command);
					
//					saveCaptureImage();
					
					if(testData()){
						flag=true;
					}
					else{
						flag=false;
					}
					flag=true;
			}
			
//			request.setAttribute("loginimg", "http://211.189.127.143/AutoPush/image/1.jpg");
//			RequestDispatcher dispatcher = request.getRequestDispatcher("index2.jsp");
//			dispatcher.forward(request, response);
			
			
		} 
		catch(UserException e){
			out.write("loginFail");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.write("error");
		}
		if(flag)out.write("success");
		out.flush();
		

	}
	public void saveCaptureImage() throws Exception{
		DBObject obj = MongoDAO.getTempData(userId);
//		APTableDetector detector = new APTableDetector(obj.toString());
//		String candidateList = detector.analyse();
		JSONObject json = new JSONObject();
		json.put("targetUrl", obj.get("targetUrl"));
		List<String> command = new ArrayList<String>();
		command.add(Constant.CASPERROOT);
		command.add(Constant.SLIMERENGINE);
		command.add(Constant.SCRIPTPATH);
		System.out.println("capture Path:"+json.toString());
		command.add(json.toString().replace("\"", "\\\"-_-"));
		command.add(Constant.RECEIVERURL);
//		command.add(candidateList.toString().replace("\"", "\\\"-_-").replace(" ","|"));
		command.add(userId);
		if(obj.get("loginURL")!=null){
				LoginData data = new LoginData(obj);
				data.print();
				command.add(("\\\""+data.getLoginUrl()+"\\\""));
				command.add("\\\""+data.getFormPath()+"\\\"");
				command.add(data.getFormIdPath());
				command.add(data.getFormPasswdPath());
				command.add(data.getId());
				command.add(data.getPasswd());
		}
		
		runProcess(command);
	}
	private void runProcess(List<String> command) throws Exception{
		long start = System.currentTimeMillis();
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command.toArray(new String[command.size()]));

		StreamGobbler error = new StreamGobbler(proc.getErrorStream());
		StreamGobbler console = new StreamGobbler(proc.getInputStream());
		error.run();
		console.run();
		
		int exitCode = proc.waitFor();
		proc.destroy();
		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 + "exit Code : "+ exitCode);
	}
	private boolean testData(){
		System.out.println("in");
		if(MongoDAO.getTempImage(userId).get("imgList")==null){
			return false;
		}
		return true;
	}

}
