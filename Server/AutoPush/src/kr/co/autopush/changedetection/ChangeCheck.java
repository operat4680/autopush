package kr.co.autopush.changedetection;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChangeCheck {
	
	List<String> oldData;
	Element target;
	List<String> key;
	APChangeDetector detector;
	public ChangeCheck(String html,List<String> oldData,String path,List<String> key){
		detector = new APChangeDetector();		
		Document doc = Jsoup.parse(html, "UTF-8");
		Elements elems = doc.getElementsByTag("body");
		Elements contents = elems.get(0).getAllElements();
		target = getTarget(contents,path);
		this.oldData = oldData;
		this.key = key;
		
	}
	
	public List<String> checkChange(){
			
		detector.setOldDatalist((ArrayList<String>)oldData);
		detector.setNewDatalist(target);
		detector.setKeys((ArrayList<String>)key);
		detector.isChanged();
		return detector.getNewLines();
	}
	public int getState(){
		return detector.getState();
	}
	
	private Element getTarget(Elements contents, String path) {
		Element target = contents.get(0);		
		String[] paths = path.split(" ");
		for(String selector : paths) {
			if(selector.contains("#")) {
				String id = selector.split("#")[1];
				for(Element e : contents) {
					if(e.id().equals(id))
						target = e;
				}
			} else {
				String[] siblings = selector.split("\\+");
				if(target.children().size()>0){
					if(siblings.length == 1) {
						target = target.children().get(0);
					} else {
						target = target.children().get(siblings.length-1);
					}
				}
			}
		}
		return target;
	}
	
	
}
