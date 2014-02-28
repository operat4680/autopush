package kr.co.autopush.algorithm;
import java.io.IOException;

import kr.co.autopush.bean.LoginData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FindFormTest {

	
//	public static String url = "http://home.mju.ac.kr/board/boardList.action?boardId=1991604&target=home&tab=board1991604&siteId=bongbong";
//	public static String url = "https://sso.mju.ac.kr/swift/login/login_eclass.jsp?RSP=eclass.mju.ac.kr&RelayState=/home/index.action?siteId=bongbong";
//	public static String url = "https://www.facebook.com/";
//	public static String url = "http://www.daum.net";
//	public static String url = "https://www.tumblr.com/login";
//	public static String url = "https://github.com/login";
//	public static String url = "http://prena.co.kr/main.htm";
//	public static String url = "https://twitter.com/";
//	public static String url = "http://gall.dcinside.com/board/lists/?id=toy&s_type=search_all&s_keyword=%ED%95%9C%EA%B8%80";
//	public static String url = "http://portal.ajou.ac.kr/public/portalLogin.jsp";
//	public static String url = "http://builder.hufs.ac.kr/user/popupLogin.action?siteId=hufs&returnUrl=/user/indexFrame.action?framePath=div2_row_1.jsp&siteId=hufs&leftPage=&rightPage=08.html";
	public static String url = "http://www.dotal.or.kr/";
	public static void main(String[] args) {
	
		FindForm form = new FindForm(url);
		LoginData data = form.getLoginData();
		data.print();
	}

}
