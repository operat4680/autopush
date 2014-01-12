package kr.co.autopush.receiver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.autopush.algorithm.FindForm;
import kr.co.autopush.bean.LoginData;
import kr.co.autopush.constant.Constant;

@WebServlet("/PushResist")
public class PushResisterServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			ProcessBuilder pb = null;
			List<String> command = new ArrayList<String>();
			if(request.getParameter("url")!=null&& !request.getParameter("url").equals("")){
					FindForm form = new FindForm(request.getParameter("url"));
					LoginData data = form.getLoginData();
					if(data!=null){
						command.add(Constant.CASPERROOT);
						command.add(Constant.SCRIPTPATH);
						command.add("\""+request.getParameter("url").replace("%", "%%")+"\"");
						command.add(Constant.IMAGEPATH);
						command.add(("\""+data.getLoginUrl()+"\"").replace("%", "%%"));
						command.add(data.getFormPath());
						command.add(data.getFormIdPath());
						command.add(data.getFormPasswdPath());
						command.add(request.getParameter("id"));
						command.add(request.getParameter("pwd"));
						System.out.println(command.toString());
						
						long start = System.currentTimeMillis();
						pb = new ProcessBuilder(command);
						Process proc = pb.start();
						InputStream errorOutput = new BufferedInputStream(proc.getErrorStream(), 10000);
						InputStream consoleOutput = new BufferedInputStream(proc.getInputStream(), 10000);
						int exitCode = proc.waitFor();
						int ch;
						System.out.println("Errors:");
						while ((ch = errorOutput.read() ) != -1) {
						    System.out.print((char) ch);
						}
			
						System.out.println("Output:");
						while ((ch = consoleOutput.read()) != -1) {
						    System.out.print((char) ch);
							
						}
						proc.destroy();
						long end = System.currentTimeMillis();
						System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
					}
			}
			request.setAttribute("loginimg", "http://211.189.127.143/AutoPush/image/1.jpg");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
