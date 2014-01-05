package kr.co.autopush.algorithm;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FindFormTest {

	
//	public static String url = "http://home.mju.ac.kr/board/boardList.action?boardId=1991604&target=home&tab=board1991604&siteId=bongbong";
//	public static String url = "https://sso.mju.ac.kr/swift/login/login_eclass.jsp?RSP=eclass.mju.ac.kr&RelayState=/home/index.action?siteId=bongbong";
//	public static String url = "https://www.facebook.com/";
//	public static String url = "http://portal.ajou.ac.kr/public/portalLogin.jsp";
	public static String url = "http://builder.hufs.ac.kr/user/popupLogin.action?siteId=hufs&returnUrl=/user/indexFrame.action?framePath=div2_row_1.jsp&siteId=hufs&leftPage=&rightPage=08.html";
	
	public static void main(String[] args) {
	//test youngwook
		FindForm form = new FindForm(url);
		form.find();
	}

}
