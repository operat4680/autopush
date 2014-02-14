package kr.co.autopush.algorithm;

import java.util.Stack;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APTraverser {
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
}
