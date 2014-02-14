package kr.co.autopush.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FrameFilter {
	private String url;
	private ArrayList<String> list;
	public FrameFilter(String url){
		this.url = url;
		list = new ArrayList<String>();
		list.add(url);
	}
	
	public String getUrlListToJson() throws JSONException, IOException{
		Document doc = null;
		if(url.startsWith("http://search.naver.com/"))doc = Jsoup.connect(url).header("Referer","http://www.naver.com").userAgent("Mozilla").get();
		else{
		doc = Jsoup.connect(url).userAgent("Mozilla").get();
		}
		Elements ele = doc.getElementsByTag("frame");
		getFrameUrl(ele,"frame");
		ele = doc.getElementsByTag("iframe");
		getFrameUrl(ele,"iframe");
		HashSet<String> set = new HashSet<String>(list);
		list = new ArrayList<String>(set);
		return getJsonUrlList();
		
		
	}
	private void getFrameUrl(Elements ele,String tag){
		for(Element e : ele){
			
			String src = e.attr("src");
			if(src.equals(""))continue;
			String realUrl = parseUrl(Jsoup.parse(src).text());
			list.add(realUrl);
			Document doc = Jsoup.parse(url);
			Elements elements = doc.getElementsByTag("tag");
			getFrameUrl(elements,tag);
		}
	}
	private String parseUrl(String url){
		if(url.equals("about:blank"))return url;
		if(url.startsWith("http") || url.startsWith("https")) return url;
		else {
//			if(url.startsWith("/")){
//				url = url.substring(1);
//			}
			return this.url+url;
		}
	}
	private String getJsonUrlList() throws JSONException{
		JSONObject obj=new JSONObject();
		obj.put("targetUrl", url);
		JSONArray array = new JSONArray();
		for(String s : list){
			JSONObject o=new JSONObject();
			o.put("url", s);
			array.put(o);
		}
		obj.put("urlList", array);
		return obj.toString();
		
	}
}
