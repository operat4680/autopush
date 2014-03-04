package kr.co.autopush.tabledetection;

import java.util.ArrayList;

public class TypeBean {
	private String typeList;
	public TypeBean() {
		// TODO Auto-generated constructor stub
		typeList = "";
	}
	
	public String getTypeList() {
		return typeList;
	}
	
	public void setType(String tagName) {
		if(tagName.equals("img")) {
			typeList += C.TAG_img;
		}else if(tagName.equals("a")) {
			typeList +=C.TAG_a;
		}else if(tagName.equals("p")) {
			typeList +=C.TAG_p;
		}else if(tagName.equals("li")) {
			typeList +=C.TAG_li;
		}else if(tagName.equals("ul")) {
			typeList +=C.TAG_ul;
		}else if(tagName.equals("tr")) {
			typeList +=C.TAG_tr;
		}else if(tagName.equals("td") || tagName.equals("th")) {
			typeList +=C.TAG_td;
		}else if(tagName.equals("div")) {
			typeList +=C.TAG_div;
		}else if(tagName.equals("article")) {
			typeList +=C.TAG_article;
		}else if(tagName.equals("section")) {
			typeList +=C.TAG_section;
		}else if(tagName.equals("ol")) {
			typeList +=C.TAG_ol;
		}else if(tagName.equals("table")) {
			typeList +=C.TAG_table;
		}else if(tagName.equals("tbody")) {
			typeList +=C.TAG_tbody;
		}else {
			typeList +=C.TAG_other;
		}
	}
	
	public float getEvaluation(TypeBean bean) {
		return 0f;
	}
}
