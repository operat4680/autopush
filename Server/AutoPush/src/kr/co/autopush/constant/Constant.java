package kr.co.autopush.constant;

public class Constant {
	public static final String CASPERROOT = "D:/casperjs/bin/casperjs.exe";
//	public static final String SLIMERENGINE = "--engine=slimerjs";
	public static final String IGNORE = "--ignore-ssl-errors=yes";
	public static final String SLIMERENGINE = "";
	public static final String SERVERIP = "211.189.127.143";
	public static final String DBID = "show4680";
	public static final String DBPASSWD = "qufl717";
	public static final String RECEIVERURL = "http://211.189.127.143/AutoPush/DataReceiver";
	public static final String JOBRECEIVERURL = "http://211.189.127.143/AutoPush/QueueServlet";
	
	
	//Queue Constant
	public static final int QUEUEMAXSIZE = 20;
	
	//test eclipse
//	public static final String SCRIPTPATH = "D:/github/Server/AutoPush/WebContent/WEB-INF/PushScript/pushscript.js";
//	public static final String JOBSCRIPTPATH = "D:/github/Server/AutoPush/WebContent/WEB-INF/PushScript/jobscript.js";
	
	
	//web server
	
	public static final String SCRIPTPATH = "D:/Tomcat7.0/webapps/AutoPush/WEB-INF/PushScript/pushscript.js";
	public static final String JOBSCRIPTPATH = "D:/Tomcat7.0/webapps/AutoPush/WEB-INF/PushScript/jobscript.js";
//	
}
