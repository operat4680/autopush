package kr.co.autopush.changedetection;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import kr.co.autopush.tabledetection.APDataProcessor;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* 추가 수정 사항
 * 1. old line data에서 중복 정보가 있을때도 SURF 방식의 매칭을 실시하도록 해야함.
 * */
public class APChangeDetector {
	private ArrayList<ElementPoint> 	mOldLines;
	private ArrayList<ElementPoint>		mNewLines;
	private boolean 			hasOldLines;
	private boolean 			hasNewLines;
	private int state;
	
	private ArrayList<String> 	keys;
	private int 				nLineIndex;
	private Logger logger = LoggerFactory.getLogger(getClass());
	public APChangeDetector() {
		hasNewLines = false;
		hasOldLines = false;
		mOldLines = new ArrayList<ElementPoint>();
		mNewLines = new ArrayList<ElementPoint>();
		state=-1;
	}
	
	public List<String> getNewLines(){
		List<String> list = new ArrayList<String>();
		for(ElementPoint p : mNewLines){
			list.add(p.toString());
		}
		return list;
	}
	/* 타겟 태그의 이전 라인 정보를 세팅합니다.
	 * params : Arraylist<String> 라인 정보
	 * return : void
	 * */
	public void setOldDatalist(ArrayList<String> DB) {
		for(String line : DB) {
			mOldLines.add(new ElementPoint(line));
		}
		hasOldLines = true;
	}
	
	/* 타겟 태그의 현재 라인 정보를 세팅합니다.
	 * params : Element 타겟 태그
	 * return : void
	 * */
	public void setNewDatalist(Element target) {
		APDataProcessor dataProc = new APDataProcessor();
		dataProc.setTargetElement(target);
		ArrayList<String> temp = dataProc.getLines();
		for(String line : temp) {
			mNewLines.add(new ElementPoint(line));
		}
		hasNewLines = true;
	}
	
	/* 타겟 태그의 변화를 확인 합니다.
	 * params : void
	 * return : (bool) 변화의 여부
	 * */
	public boolean isChanged() {	
		ArrayList<String> rawNewLines = new ArrayList<String>();
		
		
		if(!(hasNewLines && hasOldLines)) {
			System.out.println(">>> 현재 데이터 정보가 초기화 되지 않았습니다. 비정상 종료");
			return false;
		}
		boolean isChanged = false;
		
		ArrayList<ElementVector> vectors = new ArrayList<ElementVector>();
		
		int oldSize = mOldLines.size();
		int newSize = mNewLines.size();
		int idx = 0;
		System.out.println("==============================================");
		System.out.println("old :::: new");
		System.out.printf("%3d :::: %3d \n", oldSize, newSize);
		System.out.println("==============================================");
		
		int min = oldSize > newSize?newSize:oldSize;	
		
		while(min > idx) {	
//			System.out.printf("%s \n %s \n", mOldLines.get(idx).toString(), mNewLines.get(idx).toString());
			System.out.printf("%10.10s :: %10.10s \n", mOldLines.get(idx).toString(), mNewLines.get(idx).toString());
			idx++;
		}
		
		initBuf(vectors, min);		
		
//		if(oldSize != newSize) {
//			System.out.println(">>> 사이즈가 다름으로 변화가 발생했다고 가정합니다. ");
//			isChanged = true;
//		}
	
		System.out.println("================ 변화 확인 시작 ====================");
		
		for(int i = 0 ; i < min; i++) {
			if(isMultiLines(i, min)) {		
				matchLineBySurf(vectors, i, min);
			} else {
				matchLine(vectors, i, min);
			}
		}

		
		Stack<String> stackBuf = new Stack<String>();
		int 	nDisp = 0;
		boolean isUp = false;
		List<String> logList = new ArrayList<String>();
		for(ElementVector vector : vectors) {	
			if(vector.isEqual()) {

				logList.add(vector.getText()+":: 동일 라인");
				System.out.printf("%10.10s :: 동일 라인 \n",vector.getText());
//				if(!isUp) rawNewLines.addAll(newLineBuf);				
//				newLineBuf.clear();
				if(isUp) {
					isUp = false;
					nDisp = 0;
				}
				rawNewLines.addAll(stackBuf);
				stackBuf.clear();
			} else if(vector.isDown()) {
				logList.add(vector.getText()+" :: 아래로 "+ Math.abs(vector.getVector())+"칸");
				System.out.printf("%10.10s :: 아래로  %2d 칸 \n",vector.getText() , vector.getVector());
				isChanged = true;
				if(isUp) {
					isUp = false;	
					nDisp = 0;
				}
				rawNewLines.addAll(stackBuf);
				stackBuf.clear();
			} else if(vector.isDisp()) {
				logList.add(vector.getText()+" :: 새글 혹은 수정");
				System.out.printf("%10.10s :: 새글 혹은 수정 \n" ,vector.getText());
//				newLineBuf.add(vector.getText());				
				stackBuf.push(vector.getText());
				if(isUp && stackBuf.size() == nDisp) {
					isUp = false; stackBuf.clear();
					nDisp = 0;
				}
			} else if(vector.isUp()) {
				logList.add(vector.getText()+" :: 위로 "+vector.getVector()+" 칸");
				System.out.printf("%10.10s :: 위로  %2d 칸 \n ", vector.getText() , vector.getVector());
				nDisp = vector.getVector();
				rawNewLines.addAll(stackBuf);
				stackBuf.clear();
				isUp = true;
//				if(nDisp > Math.abs(vector.getVector())) {
//					rawNewLines.addAll(newLineBuf);
//				} else {
//					rawNewLines.clear();
//					newLineBuf.clear();
//					for(ElementVector v : vectors)
//						newLineBuf.add(v.getText());
//					break;
//				}				
//				newLineBuf.clear();				
			}			
		}	
//		rawNewLines.addAll(newLineBuf);
//		newLineBuf.clear();
		rawNewLines.addAll(stackBuf);
		stackBuf.clear();

		
		if(rawNewLines.size() > 0) {
			isChanged = true;	
			logList.add(">>>>>>>>>>>>>>>> NEW LINE LIST >>>>>>>>>>>>>>");
			System.out.println(">>>>>>>>>>>>>>>> NEW LINE LIST >>>>>>>>>>>>>>");
			for(String line : rawNewLines) {
				if(!line.isEmpty()){
					logList.add(line);
					System.out.printf("%10.10s \n", line);
				}
			}
		}
		
		if(isChanged) {
			logger.info("==============================================");
			logger.info("old :::: new");
			logger.info(oldSize+" :::: "+newSize);
			logger.info("==============================================");
			int i=0;
			while(min > i) {	
				System.out.println();
//				System.out.printf("%s \n %s \n", mOldLines.get(idx).toString(), mNewLines.get(idx).toString());
				logger.info(mOldLines.get(i).toString()+"::"+mNewLines.get(i).toString());
				i++;
			}
			logger.info("================ 변화 확인 시작 ====================");
			for(String log : logList){
				if(!log.isEmpty()){
					logger.info(log);
				}
			}
			state=0;
			isChanged = keyFilter(rawNewLines);
			if(isChanged){
				state=1;
			}
			else{
				isChanged=true;
			}
		}
		
		return isChanged;
	}
	
//	private void 
	
	/*	SURF 방식 처리를 위해 각 라인에 상하 2개의 버퍼 데이터를 채워 넣습니다.
	 * 	params : ArrayList<ElementVector> 모든 라인의 벡터 , int 최신 글과 이전 글 라인의 개수중 작은 값
	 * 	return : void
	 * */
	private void initBuf(ArrayList<ElementVector> vectors, int size) {
		System.out.println(" >> 각 노드의 상하 버퍼를 채워넣습니다.");		
		for(int i = 0 ; i < size; i++) {	
//			ElementVector v = new ElementVector(mOldLines.get(i));
			ElementVector v = new ElementVector(mNewLines.get(i));
			v.setStartPoint(i);
			vectors.add(v);		
			
			if(i > 0) {
				mOldLines.get(i).setUpperBuf(mOldLines.get(i-1).toString());
				mNewLines.get(i).setUpperBuf(mNewLines.get(i-1).toString());
			}
			if(i > 1) {
				mOldLines.get(i).setUpperBuf(mOldLines.get(i-2).toString());
				mNewLines.get(i).setUpperBuf(mNewLines.get(i-2).toString());
			}
			if(i < size-1) {
				mOldLines.get(i).setBottomBuf(mOldLines.get(i+1).toString());
				mNewLines.get(i).setBottomBuf(mNewLines.get(i+1).toString());
			}
			if(i < size-2) {
				mOldLines.get(i).setBottomBuf(mOldLines.get(i+2).toString());
				mNewLines.get(i).setBottomBuf(mNewLines.get(i+2).toString());
			}
		}
	}
	
	/*	동일한 텍스트를 갖는 라인이 있는지 확인합니다.
	 * 	기존 글에서 동일 라인이 있는지, 새로은 글에서 기존 글과 동일한 라인이 복수개로 있는지 확인 합니다.
	 * 	params : ind newIdx 새글 글에서 단일 라인의 인덱스
	 * 	return : boolean 동일 라인 존재 여부
	 * */
	private boolean isMultiLines(int newIdx, int size) {		
		int checkCount = 0;
		
		for(int j = 0 ; j <size; j++) {
			if(mNewLines.get(newIdx).equals(mNewLines.get(j).toString()))
				checkCount++;
		}
		
		if(checkCount > 1) return true;
		
		checkCount = 0;
		for(int j = 0 ; j < size; j++) {
			if(mNewLines.get(newIdx).equals(mOldLines.get(j).toString())) 
				checkCount++;
		}
		
		if(checkCount > 1) return true;
		
		return false;
	}
	
	/*	SURF 방식으로 라인 매칭을 합니다.
	 * 	params : ArrayList<ElementVector> vectors 백터 리스트 , int newIdx 새글에서 라인의 인덱스 값
	 * 	return : int 매치된 과거 글의 라인 인덱스 , 찾지 못한 경우 -1 리턴
	 * */
	private int matchLineBySurf(ArrayList<ElementVector> vectors, int newIdx , int size){
		System.out.println("> 본 글은 중복 글로 SURF 방식으로 매칭 처리를 실시 합니다.");
		boolean same = false;
		float max = 0f;
		int   idx = -1;
		for(int j = 0 ; j < size; j++) {
			if(mNewLines.get(newIdx).equals(mOldLines.get(j))){
				float equality = mNewLines.get(newIdx).equality(mOldLines.get(j));
				if(max<equality){
					if(newIdx == j) same = true;
					else	same = false;
					max = equality;
					idx = j;
				} else if (max == equality) {
					if(newIdx == j) same = true;					
				}
			}						
		}
		if(idx >= 0) {
			if(same)vectors.get(newIdx).setEndPoint(newIdx);
			else vectors.get(newIdx).setEndPoint(idx);
		}
			
		return idx;		
	}
	
	/* 	일반 방식으로 라인 매칭을 합니다.
	 * 	params : ArrayList<ElementVector> vectors 백터 리스트 , int newIdx 새글에서 라인의 인덱스 값
	 * 	return : int 매치된 과거 글의 라인 인덱스 , 찾지 못한 경우 -1 리턴
	 * */
	private int matchLine(ArrayList<ElementVector> vectors, int newIdx , int size) {
		for(int j = 0 ; j < size; j++) {			
			if(mNewLines.get(newIdx).equals(mOldLines.get(j).toString())){			
				vectors.get(newIdx).setEndPoint(j);
				return j;
			}						
		}
		return -1;
	}
	
	public void setKeys(ArrayList<String> keys) {
		this.keys = keys;
	}
	
	private boolean keyFilter(ArrayList<String> lines) {
		if(keys == null || keys.size() == 0) return true; 
		for(int i = 0 ; i < lines.size(); i++) {
			for(String key : keys) {
				if(lines.get(i).contains(key) == true) return true;
			}
		}
		return false;
	}
	public int getState() {
		
		return state;
	}
}


