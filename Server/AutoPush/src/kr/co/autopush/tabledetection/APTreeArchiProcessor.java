package kr.co.autopush.tabledetection;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APTreeArchiProcessor {
	public APTreeArchiProcessor() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isPossiblySameTree(Element node) {
		ArrayList<Element> children = APFilter.getVisibleChildren(node);
		
		ArrayList<String> res = new ArrayList<String>();		
		for(Element child : children) {
			if(APFilter.textFilter(child.text()).length() < 1) continue;
			res.add(APTraverser.Dfstraverse(child));
		}
		int n = res.size()-1;
		double sum = 0;
		for(int i = 0 ; i < res.size()-1 ; i++) {
			sum += APLevenshteinDistance.computeLevenShteinDistance(res.get(i), res.get(i+1));
		}
		
		sum = n > 0 ? sum/n : 0;
		
		if(sum > 0.5) return true;
		return false;
	}
	
	public static boolean isPossiblySameLine(Element node) {
		ArrayList<Element> children = APFilter.getVisibleChildren(node);
		
		ArrayList<String> res = new ArrayList<String>();		
		for(Element child : children) {
			if(APFilter.textFilter(child.text()).length() < 1) continue;
			res.add(APTraverser.Dfstraverse(child));		
		}
		int n = res.size()-1;
		double sum = 0;
		for(int i = 0 ; i < res.size()-1 ; i++) {
			sum += APLevenshteinDistance.computeLevenShteinDistance(res.get(i), res.get(i+1));
		}
		
		sum = n > 0 ? sum/n : 0;
		
		if(sum > 0.8) return true;
		return false;
	}
}
