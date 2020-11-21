package SimpleDatabase;

public class field {
	int position;
	int length;
	String fieldType;
	String name;
	boolean redundancy;
	
	// reference for Linked List
	field next;
	
	/*----------------------------
	 *  생성자
	 *  필드 타입과 이름, 필드의 위치를
	 *  사용하여 객체를 생성
	 ----------------------------*/
	field(int position, int length, String fieldType, String name, boolean redundancy){
		this.position = position;
		this.length = length;
		this.fieldType = fieldType;
		this.name = name;
		this.redundancy = redundancy;
		
		/* field head를 가지고 옴 */
		field temp = Main.mg.fieldHead;
		
		//필드가 하나도 없을 경우
		if (temp == null) {
			Main.mg.setHead(this);
		}
		else {
			//move to tail node
			while(temp.next != null)
				temp = temp.next;
			//set this node last
			temp.next = this;
		}
		//field number++
		Main.mg.fieldIn();
	}
	
	/*-----------[ reset ] --------------
	 * 재귀적으로 모든 데이터를 삭제하고 연결을 끊음
	 * 마지막으로 null을 리턴하여 본인도 제거
	 --------------------------------*/
	void reset() {
		if(this.next != null) {
			this.fieldType = null;
			this.name = null;
			this.next = null;
			if(this.next != null) this.next.reset();
		}
		else {
			this.fieldType = null;
			this.name = null;
		}
	}
	
	/*----------------
	 * 필드 멤버 리턴 함수
	 * 1. 필드 이름
	 * 2. 필드 위치
	 * 3. 필드 길이
	 ----------------*/
	String getName(int position) {
		field temp = Main.mg.fieldHead;
		for(int i=0; i<position; i++) {
			temp = temp.next;
		}
		return temp.name;
	}
	
	int getPosition() {
		return this.position;
	}
	int getFieldLength() {
		return this.length;
	}
	boolean getRedu() {
		return this.redundancy;
	}
}
