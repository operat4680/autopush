package kr.co.autopush.changedetection;
public class ElementPoint {
	private String 	mText;
	private String[] upperBuf;
	private String[] bottomBuf;
	private int 	 nUpperBuf;
	private int 	 nBottomBuf;
	
	public ElementPoint(String text) {
		// TODO Auto-generated constructor stub
		this.mText = text;
		this.nUpperBuf  = 0;
		this.nBottomBuf = 0;
		this.upperBuf 	= new String[2];
		this.bottomBuf 	= new String[2]; 
	}
	
	/* 타겟 라인의 상단에 위치한 2개의 라인 정보를 가지고 있는 버퍼 입니다.
	 * 입력은 타겟 라인에 가까운 것이 0번, 그 다음이 1번이 됩니다.
	 * params : String ([0]:타겟의 가장 가까운 라인,[1]:그다음 라인)
	 * return : void
	 * */
	public void setUpperBuf(String upperLines) {
		this.upperBuf[nUpperBuf++] = upperLines;
	}
	
	public int sizeOfUpBuf() {
		return nUpperBuf;
	}
	
	/* 타겟 라인의 하단에 위치한 2개의 라인 정보를 가지고 있는 버퍼 입니다.
	 * 입력은 타겟 라인에 가까운 것이 0번, 그 다음이 1번이 됩니다.
	 * params : String ([0]:타겟의 가장 가까운 라인,[1]:그다음 라인)
	 * return : void
	 * */
	public void setBottomBuf(String bottomLines) {
		this.bottomBuf[nBottomBuf++] = bottomLines;
	}
	
	public int sizeOfBtBuf() {
		return nBottomBuf;
	}
	
	public String[] getUpperBuf() {
		return this.upperBuf;
	}
	
	public String[] getBottomBuf() {
		return this.bottomBuf;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mText;
	}
	
	public void print() {
		System.out.println("======================");
		for(int i = upperBuf.length-1; i >=0; i--) {
			System.out.printf("> %10.10s \n", upperBuf[i]);
		}
		System.out.printf(">> %10.10s \n",mText);
		for(String buf : bottomBuf) {
			System.out.printf("> %10.10s \n",buf);
		}
		System.out.println("======================");
	}
	
	public boolean equals(ElementPoint obj) {	
		boolean result = false;
		ElementPoint temp = obj;
		if(this.mText.equals(temp.toString())) {
			int sum 	= 0;
			int count 	= 0;
			int min 	= 0;
			min = this.sizeOfUpBuf()>temp.sizeOfUpBuf()?temp.sizeOfUpBuf():this.sizeOfUpBuf();
//			System.out.printf(">> up ::: %d :: %d \n",this.sizeOfUpBuf(),temp.sizeOfUpBuf());
			sum = min;
			for(int i = 0 ; i < min; i++) {
				if(this.getUpperBuf()[i].equals(temp.getUpperBuf()[i])) count++;
			}
			min = this.sizeOfBtBuf()>temp.sizeOfBtBuf()?temp.sizeOfBtBuf():this.sizeOfBtBuf();
//			System.out.printf(">> bt ::: %d :: %d \n",this.sizeOfBtBuf(),temp.sizeOfBtBuf());
			sum+= min;
			for(int i = 0 ; i < min; i++) {
				if(this.getBottomBuf()[i].equals(temp.getBottomBuf()[i])) count++;
			}
//			System.out.println(">> ++++++++++++++ " + count + " :: " +sum);
			/* 1. 상하 버퍼가 전혀 없이 해당 라인만 같은 경우는 동일 라인으로 처리
			 * 2. 상하 버퍼의 전체 개수 중에 절반 이상이 일치해야 동일 라인으로 처리
			 * */
			if(sum == 0) return true;
			if((float)count/sum >= 0.5) result = true;
			
		}		
		return result;
	}
	
	public float equality (ElementPoint obj) {
		ElementPoint temp = obj;
		if(this.mText.equals(temp.toString())) {
			int sum 	= 0;
			int count 	= 0;
			int min 	= 0;
			min = this.sizeOfUpBuf()>temp.sizeOfUpBuf()?temp.sizeOfUpBuf():this.sizeOfUpBuf();
//			System.out.printf(">> up ::: %d :: %d \n",this.sizeOfUpBuf(),temp.sizeOfUpBuf());
			sum = min;
			for(int i = 0 ; i < min; i++) {
				if(this.getUpperBuf()[i].equals(temp.getUpperBuf()[i])) count++;
			}
			min = this.sizeOfBtBuf()>temp.sizeOfBtBuf()?temp.sizeOfBtBuf():this.sizeOfBtBuf();
//			System.out.printf(">> bt ::: %d :: %d \n",this.sizeOfBtBuf(),temp.sizeOfBtBuf());
			sum+= min;
			for(int i = 0 ; i < min; i++) {
				if(this.getBottomBuf()[i].equals(temp.getBottomBuf()[i])) count++;
			}
//			System.out.println(">> ++++++++++++++ " + count + " :: " +sum);
			/* 1. 상하 버퍼가 전혀 없이 해당 라인만 같은 경우는 동일 라인으로 처리
			 * 2. 상하 버퍼의 전체 개수 중에 절반 이상이 일치해야 동일 라인으로 처리
			 * */
			if(sum == 0) return 0f;
			return (float)count/sum;			
		}	
		return 0f;
	}
	
	public boolean equals(String str) {
		return this.mText.equals(str);
	}
}