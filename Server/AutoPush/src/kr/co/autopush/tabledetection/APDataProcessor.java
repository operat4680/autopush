package kr.co.autopush.tabledetection;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* 선택된 타겟 태그를 DB에 필요한 형태로 저장하기 위한 클래스입니다.
 * */
public class APDataProcessor {
	ArrayList<String> mLines;
	public APDataProcessor(String html,String path) {
		// TODO Auto-generated constructor stub
		Document doc = Jsoup.parse(html, "UTF-8");
		Elements elems = doc.getElementsByTag("body");
		Elements contents = elems.get(0).getAllElements();
		Element target = getTarget(contents, path);
		mLines = new ArrayList<String>();
		setTargetElement(target);
	}
	public APDataProcessor(){
		mLines = new ArrayList<String>();
	}
	
	/* 타겟 태그를 입력 받는 메소드 입니다.
	 * params : Element 타겟 태그
	 * return : 
	 * */
	public void setTargetElement(Element target) {
		ArrayList<Element> dataList = splitElement(target);
		mLines = makeLines(dataList);
		for(String data : mLines) {
			System.out.println(">> " + data);
		}
	}

	/* 타겟 태그를 좀 더 세밀하게, 즉 단순하게 분리시키는 작업을 하는 메소드 입니다.
	 * params : Element 타겟 태그
	 * return : Arraylist<element> 태겟 태그의 서브 트리 리스트
	 * */
	private ArrayList<Element> splitElement(Element target) {
		ArrayList<Element> children = APFilter.getVisibleChildren(target);
		String tagName = target.tagName();
		ArrayList<Element> lineElements = new ArrayList<Element>();
		/* 여기 부터 수정*/
		for(Element child : children) {
			APTraverser traveler = new APTraverser();
			traveler.reverseLevelOrderTraverse(child);
			ArrayList<Element> mainLineElems = traveler.getMainLineElement(child);
			if(mainLineElems == null || mainLineElems.size() < 1){
				if(APFilter.textFilter(child.text()).length() > 0) {
					ArrayList<Element> temp = APTraverser.preOrderTraverse(child);
					for(Element e : temp) {
						if(APFilter.textFilter(e.ownText()).length() >0)
							lineElements.add(e);
					}
				}
			} else {
				lineElements.addAll(mainLineElems);
			}
		}	
		System.out.println("> 타겟 태그의 라인 추출이 완료 되었습니다.");
		return lineElements;
	}
	
	private ArrayList<String> makeLines(ArrayList<Element> dataList) {
		ArrayList<String> lines = new ArrayList<String>();
		for(Element data : dataList) {
			ArrayList<Element> temp = APTraverser.preOrderTraverse(data);
			String line = "";
			for(Element e : temp) {
				String txt = e.ownText();
				if(txt.length() < 1 && e.tagName().equals("img")) {					
					txt = APFilter.textFilter(e.attr("alt"));
				}
				line += APFilter.textFilter(txt);
			}
			if(line.length() > 0) lines.add(line);
		}		
		return lines;
	}
	
	
	public ArrayList<String> getLines() {
		return mLines;
	}
	
	public Element getTarget(Elements contents, String path) {
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
				if(siblings.length == 1) {
					target = target.children().get(0);
				} else {
					target = target.children().get(siblings.length-1);
				}
			}
		}
		return target;
	}
}
