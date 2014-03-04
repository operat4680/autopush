package kr.co.autopush.tabledetection;

import java.util.ArrayList;
import java.util.Stack;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APTraverser {
	private String 	mainTagCode;
	private int		mainTagHeight;
	private ArrayList<Element> mainTagElements;
	public APTraverser() {
		this.mainTagCode = "NONE";
		this.mainTagHeight = 999;
		this.mainTagElements = new ArrayList<Element>();
	}
	
	public static String Dfstraverse(Element node) {
		Stack<Element> stack = new Stack<Element>();
		TypeBean bean = new TypeBean();
		
		stack.push(node);
		while(!stack.isEmpty()) {
			Element e = stack.pop();			
			bean.setType(e.tagName());
			Elements children = e.children();
			for(Element child : children) {
				stack.push(child);
			}
		}
		return bean.getTypeList();
	}
	
	public static ArrayList<Element> preOrderTraverse(Element node) {
		ArrayList<Element> res = new ArrayList<Element>();
		Stack<Element> stack = new Stack<Element>();
		Stack<Element> temp  = new Stack<Element>();
		stack.push(node);
		while(!stack.empty()) {
			Element e = stack.pop();
			temp.push(e);
			for(Element child : APFilter.getVisibleChildren(e))
				stack.push(child);
		}
		while(!temp.empty()) {
			res.add(temp.pop());
		}
		return res;
	}
	/*	역 레벨 순회를 이용해 주요 라인 태그를 찾습니다.
	 * 	가정1 : 기본적인 테이블 태그는 tr, li, article, dt 이라고 가정한다.
	 * 	가정2 : 테이블 태그가 2종류 이상 쓰인 경우 메인 라인 태그에 해당하는 것 아래에 존재하는 테이블 태그는 
	 * 			옵션 버튼 과 같은 잉여 태그라고 가정한다.
	 * 	가정3 : 테이블 태그가 2종류 이상 쓰인 경우 각 태그는 가장 깊은 깊이를 기준으로 한다.
	 * 			그리고 각각의 태그들의 깊이를 비교하여 가장 낮은 깊이의 태그를 메인 태그라고 한다.
	 *  메인 라인 태그를 할당합니다.
	 *  메인 라인 태그의 깊이값을 할당합니다.
	 *  
	 * 	params : Element 타겟 노드
	 * 	return : void
	 * */
	public void reverseLevelOrderTraverse(Element node) {
		/* index 는 태그 종류를 
		 * index의 값은 깊이를 말합니다. 크기가 클 수록 리프노드에 가깝습니다.
		 * 0 : article
		 * 1 : li
		 * 2 : tr
		 * 3 : dt
		 * */
		int[] label = {999,999,999,999};
		int tagIndex = -1;
		int min = 998; //스래기 값입니다.
		
		int h = height(node);
		for(int i = h; i >=1 ; i--) {		
//			System.out.println(" === " + i + " === ");
			processLineTag(node, i , label , i);
//			System.out.println("");
		}
		
		for(int i = 0 ; i < label.length; i++) {			
			tagIndex = min>label[i]?i:tagIndex;
			min 	 = min>label[i]?label[i]:min;
		}
		this.mainTagHeight = min;
		switch(tagIndex) {
		case 0 :
//			System.out.println(">> 본 태그의 자식 노드는 article 태그를 가지고 있습니다. article 태그를 하나의 라인으로 처리합니다.");
//			System.out.println(">> article은 html5 규격에서 글 하나를 담도록 권장합니다. 하지만 정확도는 떨어집니다.");
			this.mainTagCode = "article";
			break;
		case 1 :
//			System.out.println(">> 본 태그의 자식 노드는 li 태그를 가지고 있습니다. li 태그를 하나의 라인으로 처리합니다.");
			this.mainTagCode = "li";
			break;
		case 2 :
//			System.out.println(">> 본 태그의 자식 노드는 tr 태그를 가지고 있습니다. tr 태그를 하나의 라인으로 처리합니다.");
			this.mainTagCode = "tr";
			break;
		case 3 :
//			System.out.println(">> 본 태그의 자식 노드는  dt 태그를 가지고 있습니다. dt 태그를 하나의 라인으로 처리합니다.");
			this.mainTagCode = "dd";
			break;
		default :
			this.mainTagCode = "NONE";
		}		
	}
	
	/*	재귀적으로 트리를 내려가며 각 테이블 태그의 가장 깊은 깊이 값을 정합니다. 999는 쓰래기 값입니다.
	 * 	params : Element 노드 , int 추적하기 위한 레벨, int[4] 테이블 태그 라벨 배열, int 원하는 깊이값
	 * 	return : void
	 * */
	private void processLineTag(Element node , int level , int[] label , int height) {
		ArrayList<Element> temps = APFilter.getVisibleChildren(node);
		if(level == 1) {	
			
			/* 아래 코드는 테이블 태그 밑에 또 동일 태그가 있지만 태그의 개수가 3개 이하이거나 태그 안에 텍스트가 없는 경우를 제거 하기 위함입니다.
			 * */
			if(node.tagName().equals("article") || node.tagName().equals("li")
			|| node.tagName().equals("tr")		|| node.tagName().equals("dd")) {
				if(APFilter.textFilter(node.text()).length() == 0)
					return;
				String tName = node.tagName();
				int n = 0;
				for(Element sibling : node.siblingElements()) {
					if(sibling.tagName().equals(tName)) n++;
				}
				if(n < 2) return;
			}
			/**/
				
//			System.out.print(node.tagName() + " : ");
			if(node.tagName().equals("article")) {
				label[0] = label[0]!=999?label[0]:height;
			} else if(node.tagName().equals("li")){
				label[1] = label[1]!=999?label[1]:height;
			} else if(node.tagName().equals("tr")){
				label[2] = label[2]!=999?label[2]:height;
			} else if(node.tagName().equals("dd")){
				label[3] = label[3]!=999?label[3]:height;
			}
		} else if(level > 1) {
			for(Element e : temps) {
				processLineTag(e, level-1 , label , height);
			}
		}
	}
	
	/*	메인 라인 태그의 Element를 찾습니다.
	 * 	본 메소드는 reverseLevelOrderTraverse를 선 실행 해야 실행 합니다.
	 * 	params : Element 타겟 노드
	 * 	return : 메인 라인 태그 배열 , 만약 선 메소드가 불리지 않거나 메인 라인 태그가 없다면 null
	 * */
	public ArrayList<Element> getMainLineElement(Element target) {
		if(this.mainTagHeight == 999) return null;
		this.mainTagElements.clear();
		processMainLineElement(target, this.mainTagHeight);
		return this.mainTagElements;
	}
	
	private void processMainLineElement(Element node, int level) {
		ArrayList<Element> temps = APFilter.getVisibleChildren(node);
		if(level == 1) {
			if(node.tagName().equals("li") || node.tagName().equals("dd") || node.tagName().equals("dt")
			|| node.tagName().equals("tr") || node.tagName().equals("article")) {				
				this.mainTagElements.add(node);
			} else {
				for(Element e : temps) {
					processMainLineElement(e, level);
				}				
			}				
		} else if(level > 1) {
			for(Element e : temps) {
				processMainLineElement(e, level-1);
			}
		}
	}
	
	private int height(Element node) {
		if(APFilter.getVisibleChildren(node).size() < 1)
			return 0;
		else {
			int max = 0 ; 
			ArrayList<Element> children = APFilter.getVisibleChildren(node);
			for(Element child : children) {
				int h = height(child);
				max = max>h?max:h;
			}
			return max+1;
		}
	}
	
	public int getMainTagHeight() {
		return this.mainTagHeight;
	}
}
