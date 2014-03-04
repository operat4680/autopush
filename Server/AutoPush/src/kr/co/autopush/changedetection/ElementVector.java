package kr.co.autopush.changedetection;

import kr.co.autopush.tabledetection.C;



public class ElementVector {
	/*If element is no exist, point will be assigned as 99999*/
	private String 	mText;
	private ElementPoint sPtn;
	private ElementPoint ePtn;
	private int 	sPoint;
	private int 	ePoint;
	private int 	mVector;
	public ElementVector(String text) {
		// TODO Auto-generated constructor stub
		this.mText = text;		
		this.ePoint = C.NOTEXIST; 
		this.mVector = C.NOTEXIST;
	}
	
	public ElementVector(ElementPoint sPtn) {
		this.sPtn = sPtn;
		this.ePoint = C.NOTEXIST;
		this.mVector = C.NOTEXIST;
	}
	
	public void setStartPoint(int idx) {
		this.sPoint = idx;
	}
	
	public void setEndPoint(int index) {
		this.ePoint = index;
		mVector = ePoint - sPoint;
	}
	
	public int getVector() {
		return mVector;
	}
	
	public boolean isUp() {
		return mVector>0 && mVector <90000?true:false;
	}
	
	public boolean isDisp() {
		return ePoint==C.NOTEXIST?true:false;
	}
	
	public boolean isDown() {
		return mVector<0 ?true:false;
	}
	
	public boolean isEqual() {
		return mVector==0?true:false;
	}
	
	public void print() {
		sPtn.print();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub		
		return "sPtn : " + sPoint + " , ePtn : " +ePoint + " , :: text :: " +sPtn.toString();
	}
	
	public String getText() {
		return sPtn.toString();
	}
}