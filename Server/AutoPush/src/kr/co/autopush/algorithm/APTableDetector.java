package kr.co.autopush.algorithm;



import java.util.ArrayList;

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
		urlList= new ArrayList<String>();
		elementList = new ArrayList<Elements>();
		for(int i=0;i<array.length();i++){
			urlList.add(array.getJSONObject(i).getString("url"));
			elementList.add(getDocument(array.getJSONObject(i).getString("html")));
		}
		urlArray = new JSONArray();
	}
	
	private Elements getDocument(String input){
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements elems = doc.getElementsByTag("html");

		Elements contents = elems.get(0).getAllElements();
		
		contents.select("head").remove();
		contents.select("script").remove();
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
	        
	        if(resList.size()!=0){
	        	System.out.println("in~~~~"+urlList.get(i) );		             
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
	//        casper.connect();
	        
	        /*below code is just testing code for change detecting.*/
	//        System.out.println(":::::::::::::::::::::::::::::::::::");
	//        System.out.print("select node index :: ");
	//        Scanner scan = new Scanner(System.in);
	//        int index = scan.nextInt();
	//        if(index == -1) return;
	//        Element select_node = resList.get(index);
	//        System.out.println("select index  :: " + index);
	//        System.out.println(">> file saving now.");
	//        BufferedWriter writer;
	//        try {
	//        	writer = new BufferedWriter(new FileWriter(new File("./test.txt")));
	//        	writer.write(TestMain.getPath(select_node)+"\n");
	//        	writer.write(select_node.children().size()+"\n");        	
	//        	for(Element child : select_node.children())
	//        		writer.write(child.text().replaceAll("\\d", "")+"\n");
	//        	if(writer != null) writer.close();
	//		} catch (Exception e) {
	//			// TODO: handle exception
	//		}
	//        System.out.println(">> file saved now.");
	        /*=======================================================*/
	       
    	
    }
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
			} else if (!temp.className().equals("")) {
				String name = temp.className().split(" ")[0];
				int index =	temp.elementSiblingIndex();
				String i = index==0?"":":nth-child("+(index+1)+")";
				if (result.equals("")) {					
					result = temp.tagName() + "." + name + i;
				} else {
					result = temp.tagName() + "." + name + i + " " + result;
				}
			} else {
				int index =	temp.elementSiblingIndex();
				String i = index==0?"":":nth-child("+(index+1)+")";
				if (result.equals("")) {
					result = temp.tagName() + i + result;
				} else {
					result = temp.tagName() + i + " " + result;
				}
			}
			temp = temp.parent();
		}
		return result;
	}
}

