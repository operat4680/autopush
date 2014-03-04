package kr.co.autopush.tabledetection;



import java.io.FileWriter;
import java.util.ArrayList;

import kr.co.autopush.util.FileWriteUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class APTableDetector {
	private ArrayList<String> urlList;
	private ArrayList<Elements> elementList;
	private JSONArray urlArray;
	public APTableDetector(String list) throws JSONException{
		JSONObject obj = new JSONObject(list);
		JSONArray array= obj.getJSONArray("htmlData");
		FileWriteUtil.write(array.toString());
		urlList= new ArrayList<String>();
		elementList = new ArrayList<Elements>();
		for(int i=0;i<array.length();i++){

			elementList.add(getDocument(array.getJSONObject(i).getString("html")));
			urlList.add(array.getJSONObject(i).getString("url"));
		}
		urlArray = new JSONArray();
	}
	
	private Elements getDocument(String input){
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements elems = doc.getElementsByTag("html");

		Elements contents = elems.get(0).getAllElements();
		
//		contents.select("head").remove();
		return contents;
	}

    public String analyse() throws JSONException {
    	
    	for(int i=0;i<elementList.size();i++){
	        ArrayList<Element> resList = new ArrayList<Element>();
	        StringBuilder pathList = new StringBuilder();
	        for(Element node : elementList.get(i)) {
	        	String tag = node.tagName();
	        	if(tag.equals("body")) continue;        	
	            if(APFilter.getVisibleChildren(node).size() >= C.CHILD_MIN_COUNT ) {
	            	if(tag.equals("ul") || tag.equals("tbody") 
	                || tag.equals("thead") || tag.equals("ol"))
	            	{	
	                	if(APTableTagProcessor.hasText(node))
	                		resList.add(node);
	                } else {
	                	if(APTreeArchiProcessor.isPossiblySameTree(node))
	            			resList.add(node);
	                }
	            }
	        }
	        if(resList.size() > 100) 
	        	resList = APFilter.filtering(resList);
	        
	        if(resList.size()>0){
	        	System.out.println("result length : "+ resList.size());
		        for(Element node : resList) {
		        	  
		        	  pathList.append(getPath(node)+",");
		        }
		        pathList.deleteCharAt(pathList.length()-1);
		        JSONObject obj = new JSONObject();
		        obj.put("url", urlList.get(i));
		        obj.put("tagList", pathList.toString());
		        urlArray.put(obj);
	    	}
    	}
	    JSONObject result = new JSONObject();
	    result.put("pathList", urlArray);
		return result.toString();
    }
//    public String getPath(Element e) {
//		String result = "";
//		Element temp = e;
//		while (!(temp.tagName().equals("body"))) {
//			if (!temp.id().equals("")) {
//				if (result.equals("")) {
//					result = temp.tagName() + "#" + temp.id();
//				} else {
//					result = temp.tagName() + "#" + temp.id() + " " + result;
//				}
//				break;
//			} else if (!temp.className().equals("")) {
//				String name = temp.className().split(" ")[0];
//				int index =	temp.elementSiblingIndex();
//				String i = index==0?"":":nth-child("+(index+1)+")";
//				if (result.equals("")) {					
//					result = temp.tagName() + "." + name + i;
//				} else {
//					result = temp.tagName() + "." + name + i + " " + result;
//				}
//			} else {
//				int index =	temp.elementSiblingIndex();
//				String i = index==0?"":":nth-child("+(index+1)+")";
//				if (result.equals("")) {
//					result = temp.tagName() + i + result;
//				} else {
//					result = temp.tagName() + i + " " + result;
//				}
//			}
//			temp = temp.parent();
//		}
//		return result;
//	}
    public String getPath(Element e) {
	    String result = "";
		Element temp = e;		
		while (!(temp.tagName().equals("body"))) {
			if (!temp.id().equals("")) {
				if (result.equals("")) {
					result = temp.tagName() + "#" + temp.id();
				} else {
					result = temp.tagName() + "#" + temp.id() + " " + result;
				}
				break;
			} else {
				String selector = temp.tagName();
				while(temp.previousElementSibling() != null) {
					String tagName =  temp.previousElementSibling().tagName();
					if(tagName.equals("noscript")){
						tagName = "*";
					}
					selector = tagName+"+"+ selector;
					temp = temp.previousElementSibling();
				}
				
				if (result.equals("")) {
					result = selector + result;
				} else {
					result = selector + " " + result;
				}
			}
			temp = temp.parent();
			if(temp==null)break;
		}
		return result;
    }
}

