package kr.co.autopush.algorithm;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APFilter {
	public static ArrayList<Element> filtering(ArrayList<Element> elems) {
		ArrayList<Element> res = new ArrayList<Element>();
		for (Element elem : elems) {
			int sameNodes = 0;
			String selectors[] = new String[3];
			selectors[0] = elem.id();
			selectors[1] = elem.className().split(" ")[0];
			selectors[2] = getParentPath(elem);

			for (int i = 0; i < elems.size(); i++) {
				if (selectors[2].equals(getParentPath(elems.get(i))))
					sameNodes++;
			}

			if (sameNodes < C.SPAM_LIMIT)
				res.add(elem);
		}
		return res;
	}

	public static String getParentPath(Element e) {
		String result = "";
		Element temp = e.parent();
		for (int i = 0; i < 2 && temp.parent() != null; i++) {
			if (!temp.id().equals("")) {
				if (result.equals("")) {
					result = temp.tagName() + "#" + temp.id();
				} else {
					result = temp.tagName() + "#" + temp.id() + " " + result;
				}
				break;
			} else if (!temp.className().equals("")) {
				String name = temp.className().split(" ")[0];
				int index = temp.elementSiblingIndex();
				String str = index == 0 ? "" : ":nth-child(" + (index + 1)
						+ ")";
				if (result.equals("")) {
					result = temp.tagName() + "." + name + str;
				} else {
					result = temp.tagName() + "." + name + str + " " + result;
				}
			} else {
				int index = temp.elementSiblingIndex();
				String str = index == 0 ? "" : ":nth-child(" + (index + 1)
						+ ")";
				if (result.equals("")) {
					result = temp.tagName() + str + result;
				} else {
					result = temp.tagName() + str + " " + result;
				}
			}
			temp = temp.parent();
		}
		return result;
	}

	public static ArrayList<Element> getVisibleChildren(Element node) {
		Elements children = node.children();
		ArrayList<Element> visibleChilren = new ArrayList<Element>();
		for(Element child : children){
			String style = child.attr("style");
			if(style.contains("display:none")
			|| style.contains("display :none")
			|| style.contains("display: none")
			|| style.contains("display : none")){				
				continue;
			}
			visibleChilren.add(child);
		}
		return visibleChilren;
	}
	
	public static ArrayList<String> getIframeUrl(Element elem) {
		ArrayList<String> frameList = new ArrayList<String>();
		
		Elements iframes = elem.getElementsByTag("iframe");
		for(Element iframe : iframes) {
			String tempUrl = iframe.attr("src");			
			if(tempUrl.equals("") || tempUrl.startsWith("about")) continue;
			else if(tempUrl.contains("http") || tempUrl.contains("https")) frameList.add(tempUrl);
			else frameList.add(TestMain.url+tempUrl+"/");			
		}		
		Elements frames = elem.getElementsByTag("frame");
		for(Element frame : frames) {
			String tempUrl = frame.attr("src");
			System.out.println(">>> " + tempUrl);
			if(tempUrl.equals("") || tempUrl.startsWith("about")) continue;
			else if(tempUrl.contains("http") || tempUrl.contains("https")) frameList.add(tempUrl);
			else frameList.add(TestMain.url+tempUrl+"/");			
		}	
		
		return frameList;
	}
	
	public static String textFilter(String str) {
		/*
		 * 1. remove numerical text
		 * 2. remove html optional text
		 * 3. remove 
		 */		
		str = str.replaceAll("\\d", "");
		str = str.replaceAll("\u00a0", ""); // '&nbsp;' equal to '\u00a0' in jsoup
		str = str.replaceAll("[.;!?\"'\\s]", "");
		
		return str;
	}
	
	public static boolean isPossiblyMenu(Element tag) {
		Elements children = tag.children();
		int size = children.size();
		int count = 0;
		float result = 0f;
		for(Element child : children) {
			if(child.children().size() == 0 || (child.children().size() == 1 && !(child.children().get(0).tagName().equals("div"))) ) count++;
		}
		result = (float)count/size;
		if(result >= 0.8) return true;
		return false;
	}
}
