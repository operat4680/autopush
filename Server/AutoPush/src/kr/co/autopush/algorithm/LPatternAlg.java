package kr.co.autopush.algorithm;



import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LPatternAlg {
	
	public static float getValue(Element node) {
		Elements children = node.children();
		int n = children.size();
		int max = 0;
		int min = -1;
		float avg = 0;
		float std = 0;
		float sqsum = 0;
		float sum = 0;
		for(Element child : children) {
			Elements grandChildren = child.children();
			for(Element grandChild : grandChildren) {				
				int x = grandChildren.size();
				max = max >= x ? max : x;
	            min = (min <= x && min != -1)? min:x;
				sum +=x;
				sqsum += x*x;
			}			
		}
		if(sum == 0) return 0.0f;
		avg = sum / (float)n;
	    std = Math.round((float)Math.sqrt((double)(sqsum / (float)n) - (double)(avg * avg)));
	    if(std == 0) return 1.0f;
	    return ((float)(max - min) - 2*std) / (float)(max - min);
	}
}
