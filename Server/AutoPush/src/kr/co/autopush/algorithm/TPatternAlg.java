package kr.co.autopush.algorithm;




import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TPatternAlg {
    public static float getValue(Element node) {
        if(!node.isBlock())
            return 0.0F;
        Elements children = node.children();
        TypeAnalyser analyser = new TypeAnalyser();
        for(Element child : children) {            
            Elements grandChildren = child.children();
            ChildNode data = new ChildNode();            
            for(Element grandChild : grandChildren) {
            	data.add(grandChild.tagName());
            }
            if(data.size() >= 1)
                analyser.add(data);
        }
        return analyser.eval();
    }
}
