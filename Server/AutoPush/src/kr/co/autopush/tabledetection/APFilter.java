package kr.co.autopush.tabledetection;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APFilter {
	
	/*	전체 구조 안에 각 라인이 일일히 잡히는 것을 방지 하기 위한 필터 입니다.
	 * 	기본적으로 해당 노드의 상위 패스 피교를 통해서 중복의 개수를 파악합니다.
	 * 	중복 개수가 한계치 : 7(C.SPAM_LIMIT) 이상이거나 형제 노드중에 중복되는 것의 비율이 0.8 이상인경우는 중복이라고 처리합니다.
	 * 	단, 형제 노드의 개수가 4개 미만인 경우는 제외합니다.
	 * 	params : ArrayList<Element> 테이블로 잡힌 결과물들
	 * 	return : ArrayList<Element> 잡힌 것 중에서 중복의 가능성이 낮은 결과물
	 * */
	public static ArrayList<Element> filtering(ArrayList<Element> elems) {
		ArrayList<Element> res = new ArrayList<Element>();
		int nCount = 0;
		for (Element elem : elems) {			
			int nSiblings = elem.siblingElements().size()+1;
			int sameNodes = 0;
			String selectors = getParentPath(elem);		
			
			for (int i = 0; i < elems.size(); i++) {
				if (selectors.equals(getParentPath(elems.get(i))))
					sameNodes++;
			}		
			
			if((sameNodes <= C.SPAM_LIMIT) && ((float)sameNodes/nSiblings < 0.8 || nSiblings < 4)){
				res.add(elem);	
				System.out.printf(">>> %3d :: %5s : %2d : %2d : 는 중복 가능성이 낮아 결과에서 추가합니다.\n" ,nCount++ ,elem.tagName(),sameNodes,nSiblings);				
			}				
			else System.out.printf(">>> --- :: %5s : %2d : %2d : 는 중복 가능성이 높아 결과에서 제거합니다.\n" ,elem.tagName(),sameNodes,nSiblings);
//			System.out.println(">>> " + getParentPath(elem));
		}
		return res;
	}

	/*	getPath와 동일합니다. 다
	 * */
	public static String getParentPath(Element e) {
		String result = "";
		Element temp = e.parent();
		while (!(temp.tagName().equals("body"))) {
			if (!temp.id().equals("")) {
				if (result.equals("")) {
					result = temp.tagName() + "#" + temp.id();
				} else {
					result = temp.tagName() + "#" + temp.id() + " " + result;
				}
				break;
			} else {
				int index =	temp.elementSiblingIndex();
				String nextSib 	= temp.tagName();
				String selector = temp.tagName();
				while(temp.previousElementSibling() != null) {					
					if(!temp.previousElementSibling().tagName().equals(nextSib)){
						selector = temp.previousElementSibling().tagName()+"+"+ selector;
					}
					nextSib = temp.previousElementSibling().tagName();
					temp = temp.previousElementSibling();
				}
				
				if (result.equals("")) {
					result = selector + result;
				} else {
					result = selector + " " + result;
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
	
	public static String textFilter(String str) {
		/*
		 * 1. remove numerical text
		 * 2. remove html optional text
		 * 3. remove 
		 */		
		str = str.replaceAll("\\d", "");
		str = str.replaceAll("\u00a0", ""); // '&nbsp;' equal to '\u00a0' in jsoup		
		str = str.replaceAll("[\\[\\]<>\\(\\)\\-.;:`![?]^[$]#%/\\{\\}\"'\\s]", "");		
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
