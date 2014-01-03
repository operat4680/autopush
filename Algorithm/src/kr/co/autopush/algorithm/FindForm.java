package kr.co.autopush.algorithm;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindForm {

	
	private ArrayList<String> urlList;
	public String url;

	public FindForm(String url){
		this.url = url;
		urlList = new ArrayList<String>();
	}
	
	public void find(){
		Document doc=null;
		
		try {
		doc = Jsoup.connect(url).get();
		//System.out.println(doc.html());
		
		Elements elements = doc.getElementsByAttributeValue("type", "password");
		if(elements.isEmpty()){
			elements = doc.select("iframe");
			if(!elements.isEmpty()){
				insertUrlList(elements);
				for(String iurl : urlList){
					doc = Jsoup.connect(iurl).get();
					elements = doc.getElementsByAttributeValue("type", "password");
					if(!elements.isEmpty()){
						break;
					}
				}
			}
			
		}
		if(elements.isEmpty()){
			return;
		}
		getChildNodeInfo(elements.get(0));
		System.out.println(elements.get(0).attr("name"));
		getChildNodeInfo(findTagByPasswd(elements.get(0)));
		System.out.println((findTagByPasswd(elements.get(0))).attr("name"));
		getChildNodeInfo(findFormByPasswd(elements.get(0)));
		System.out.println(findFormByPasswd(elements.get(0)).attr("name"));
		System.out.println("form name : "+findFormByPasswd(elements.get(0)).attr("name") );
		}catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void insertUrlList(Elements list) throws IOException{
		for(Element e : list){
			String iurl = e.attr("src");	
			String subUrl="";
			if(!iurl.startsWith("http")){
				String[] subList = url.split("/");
				subUrl = subList[0]+"//"+subList[2];
			}
			urlList.add(subUrl+iurl);
			System.out.println(subUrl+iurl);
			Document doc = Jsoup.connect(subUrl+iurl).get();
			Elements elements = doc.select("iframe");
			if(!elements.isEmpty()){
				insertUrlList(elements);
			}
		}
	}
	public Element findFormByPasswd(Element e){
		Element temp = e;
		while(!(temp.tagName().equals("form"))){
			temp = temp.parent();
		}
		return temp;
	}
	public Element findTagByPasswd(Element e){
		Element temp= e;
		Elements target;
		Element result =null;
		boolean isFind = false;
		do{
			result = null;
			target = temp.siblingElements();
			for(Element ele : target){
				if(isFind){
					break;
				}
				Elements in = ele.getElementsByTag("input");
				for(Element m : in){
					if(m.tagName().equals("input")&&(m.attr("type").equals("text"))){
						result = m;
						isFind=true;
						break;
					}
				}
			}
			temp = temp.parent();	
		}while(result==null);
		
		
		return result;
	}
	
	public void getChildNodeInfo(Element e){
			System.out.println("-------");
			System.out.println("path : "+getPath(e));
			System.out.println("size : "+e.children().size());
			System.out.println("tag name: "+ e.tagName());
			System.out.println("id : "+e.id());
			System.out.println("class : "+e.className());
		
	}
	public String getPath(Element e){
		String result ="";
		Element temp = e;
		while(!(temp.tagName().equals("body"))){
			if(!temp.id().equals("")){
				result = temp.tagName()+"[id="+temp.id()+"]/"+result;
			}
			else if(!temp.className().equals("")){
				result = temp.tagName()+"[class="+temp.className()+"]/"+result;
			}
			else{
				result = temp.tagName()+"/"+result;
			}
			temp = temp.parent();
		}
		return result;
		
	}

}