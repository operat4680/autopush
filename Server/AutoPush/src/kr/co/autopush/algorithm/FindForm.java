package kr.co.autopush.algorithm;

import java.io.IOException;
import java.util.ArrayList;

import kr.co.autopush.bean.LoginData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindForm {

	private ArrayList<String> urlList;
	public String url;

	public FindForm(String url) {
		this.url = url;
		urlList = new ArrayList<String>();
	}

	public LoginData getLoginData() {
		Document doc = null;
		String formUrl = this.url;
		LoginData data = new LoginData();
		try {
			doc = Jsoup.connect(url).get();
			// System.out.println(doc.html());
			System.out.println(doc.html());
			Elements elements = doc.getElementsByAttributeValue("type",
					"password");
			if (elements.isEmpty()) {
				elements = doc.select("iframe");
				Elements elements2 = doc.select("frame");
				if (!elements.isEmpty()||!elements2.isEmpty()){
					insertUrlList(elements);
					insertUrlList(elements2);
					for (String iurl : urlList) {
						System.out.println(iurl);
						doc = Jsoup.connect(iurl).get();
						elements = doc.getElementsByAttributeValue("type",
								"password");
						if (!elements.isEmpty()) {
							formUrl = iurl;
							break;
						}
					}
				}

			}
			if (elements.isEmpty()) {
				return null;
			}

			getChildNodeInfo(elements.get(0));
			System.out.println(elements.get(0).attr("name"));
			getChildNodeInfo(findTagByPasswd(elements.get(0)));
			System.out.println((findTagByPasswd(elements.get(0))).attr("name"));
			getChildNodeInfo(findFormByPasswd(elements.get(0)));
			System.out.println(findFormByPasswd(elements.get(0)).attr("name"));
			System.out.println("form name : "
					+ findFormByPasswd(elements.get(0)).attr("name"));

			data.setLoginUrl(formUrl);
			data.setFormPath(getPath(findFormByPasswd(elements.get(0))));
			data.setFormIdPath(getResultName(findTagByPasswd(elements.get(0))));
			data.setFormPasswdPath(getResultName(elements.get(0)));

			return data;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void insertUrlList(Elements list) {
		for (Element e : list) {
			Document doc = null;
			String iurl = e.attr("src");
			String subUrl = "";
			if (!iurl.startsWith("http")) {
				String[] subList = url.split("/");
				subUrl = subList[0] + "//" + subList[2];
			}
			if(!iurl.startsWith("/")){
				iurl = "/"+iurl;
			}
			if(iurl.startsWith("./")){
				iurl.replace("./", "/");
			}
			urlList.add(subUrl + iurl);
			System.out.println(subUrl + iurl);
			try{
				doc = Jsoup.connect(subUrl + iurl).get();
			}catch(Exception et){
				System.out.println("connect error : "+subUrl + iurl);
			}
			if(doc!=null){
				System.out.println("in");
				Elements elements = doc.select("iframe");
				
				if (!elements.isEmpty()) {
					insertUrlList(elements);
				}
				Elements elements2 = doc.select("frame");
				if (!elements2.isEmpty()) {
					
					insertUrlList(elements2);
				}
			}
		}
	}

	public Element findFormByPasswd(Element e) {
		Element temp = e;
		while (!(temp.tagName().equals("form"))) {
			temp = temp.parent();
		}
		return temp;
	}

	public Element findTagByPasswd(Element e) {
		Element temp = e;
		Elements target;
		Element result = null;
		boolean isFind = false;
		do {
			result = null;
			target = temp.siblingElements();
			for (Element ele : target) {
				if (isFind) {
					break;
				}
				Elements in = ele.getElementsByTag("input");
				for (Element m : in) {
					if (m.tagName().equals("input")
							&& (m.attr("type").equals("text"))) {
						result = m;
						isFind = true;
						break;
					}
				}
			}
			temp = temp.parent();
		} while (result == null);

		return result;
	}

	public void getChildNodeInfo(Element e) {
		System.out.println("-------");
		System.out.println("path : " + getPath(e));
		System.out.println("size : " + e.children().size());
		System.out.println("tag name: " + e.tagName());
		System.out.println("id : " + e.id());
		System.out.println("class : " + e.className());

	}

	public String getPath(Element e) {
		String result = "";
		Element temp = e;
		while (!(temp.tagName().equals("body"))) {
			if (!temp.id().equals("")) {
				if(result.equals("")){
					result = temp.tagName() + "#" + temp.id();
				}
				else{
					result = temp.tagName() + "#" + temp.id()+" "+result;
				}
				break;
			} else if (!temp.className().equals("")) {
				String name = temp.className().split(" ")[0];
				if(result.equals("")){
					result = temp.tagName() + "." + name;
				}
				else{
					result = temp.tagName() + "." + name + " "+result;
				}
			} else {
				if(result.equals("")){
				result = temp.tagName() + result;
				}
				else{
					result = temp.tagName() +" "+ result;
				}
			}
			temp = temp.parent();
		}
		return result;

	}


	public String getResultName(Element e) {
		if (e.attr("name") != null && !e.attr("name").equals("")) {
			return e.attr("name");
		}
		return "";
	}

}