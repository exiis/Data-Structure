package SimpleDatabase;

public class data {
	String data;
	data next;
	long filePosition = -1;
	long dataSize = -1;
	/*-------------------[ 생성자 ]-------------------
	 * data를 입력 받아 데이터를 생성한다
	 * 이후 Linked List로 연결하여 저장
	 -----------------------------------------------*/	
	 public data(String data) {		
		this.data = data;
		next = null;
	}
	 
	 //head가 생성이 되어야 call 할 수 있음
	 public void addData(data newData) {
		 data temp = this;
		//마지막 노드까지 temp 이동
		 while(temp.next != null) {
			 temp = temp.next;
		 }
		 //tail 위치에 newData 삽입
		 temp.next = newData;
	 }

	/* ------------------------
	 * 멤버 변수 리턴 및 설정
	 * 1. i번째 필드의 데이터를 리턴
	 ------------------------ */
	public String getData(int i) { /* i = field position */
		if(i==0) return this.data;
		else {
			data temp = this;
			for(int j=0; j<i; j++) 
				temp = temp.next;
			return temp.data;
		}
	}
	
	public void setData(String newData) {
		this.data = newData;
	}
	
	public int getFilePosition(int i) {
		if(i==0) return (int) this.filePosition;
		else {
			data temp = this;
			for(int j=0; j<i; j++) 
				temp = temp.next;
			return (int)temp.filePosition;
		}
	}
}