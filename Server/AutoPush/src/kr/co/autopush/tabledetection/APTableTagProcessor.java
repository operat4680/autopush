package kr.co.autopush.tabledetection;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APTableTagProcessor {
	public APTableTagProcessor() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean hasText(Element tag) {
		Elements children = tag.children();
		int size = children.size();
		int textCount = 0;		
		for(Element child : children) {		
			if(!(child.tagName().equals("li") || child.tagName().equals("tr"))){
				size--; continue;
			}
				
			if(APFilter.textFilter(child.text()).length()<1){	
				ArrayList<Element> temp = APTraverser.preOrderTraverse(child);
				for(Element e : temp) {
					if(e.tagName().equals("img") && APFilter.textFilter(e.attr("alt")).length() > 0) {
						textCount++;
						break;
					}						
				}				
				continue;					
			} else 	textCount++;
		}
		
		if(tag.parent().className().equals("newss")) System.out.println(">> " + textCount +" ::::::" +size);
		
		if((float)textCount/size >= 0.33) return true;
		return false;		
	}	
}
