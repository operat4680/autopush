package kr.co.autopush.algorithm;

import java.util.ArrayList;

public class TypeBean {
	private ArrayList<Integer> typeList;
	public TypeBean() {
		// TODO Auto-generated constructor stub
		typeList = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getTypeList() {
		return typeList;
	}
	
	public void setType(String tagName) {
		if(tagName.equals("img")) {
			typeList.add(C.TAG_img);
		}else if(tagName.equals("a")) {
			typeList.add(C.TAG_a);
		}else if(tagName.equals("p")) {
			typeList.add(C.TAG_p);
		}else if(tagName.equals("li")) {
			typeList.add(C.TAG_li);
		}else if(tagName.equals("ul")) {
			typeList.add(C.TAG_ul);
		}else if(tagName.equals("tr")) {
			typeList.add(C.TAG_tr);
		}else if(tagName.equals("td") || tagName.equals("th")) {
			typeList.add(C.TAG_td);
		}else if(tagName.equals("div")) {
			typeList.add(C.TAG_div);
		}else {
			typeList.add(C.TAG_other);
		}
	}
	
	public float getEvaluation(TypeBean bean) {
		return 0f;
	}
}
