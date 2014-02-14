package kr.co.autopush.algorithm;

import java.util.ArrayList;

public class ChildNode {
	private ArrayList<Integer> grandChilds;
    public ChildNode() {
        grandChilds = new ArrayList<Integer>();
    }

    public ArrayList<Integer> getGrandChilds() {
        return grandChilds;
    }

    public void add(String tagName) {
        tagName.replaceAll("\\d", "");
        if(tagName.equals("img"))
            grandChilds.add(C.TAG_img);
        else if(tagName.equals("a"))
            grandChilds.add(C.TAG_a);
        else if(tagName.equals("p"))
            grandChilds.add(C.TAG_p);
        else if(tagName.equals("li"))
            grandChilds.add(C.TAG_li);
        else if(tagName.equals("ul"))
            grandChilds.add(C.TAG_ul);
        else if(tagName.equals("tr"))
            grandChilds.add(C.TAG_tr);
        else if(tagName.equals("td") || tagName.equals("th"))
            grandChilds.add(C.TAG_td);
        else if(tagName.equals("div"))
            grandChilds.add(C.TAG_div);
        else if(tagName.equals("h"))
            grandChilds.add(C.TAG_h);
        else
            grandChilds.add(C.TAG_other);
    }

    public int size() {
        return grandChilds.size();
    }

    public void print() {
        if(grandChilds.size() == 0)
            return;
        System.out.print(">> CHILD :: ");
        System.out.println("");
    }

    public boolean equals(Object obj) {
        ChildNode temp = (ChildNode)obj;
        return grandChilds.equals(temp.getGrandChilds());
    }   
}
