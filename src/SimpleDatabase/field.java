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
	 *  ������
	 *  �ʵ� Ÿ�԰� �̸�, �ʵ��� ��ġ��
	 *  ����Ͽ� ��ü�� ����
	 ----------------------------*/
	field(int position, int length, String fieldType, String name, boolean redundancy){
		this.position = position;
		this.length = length;
		this.fieldType = fieldType;
		this.name = name;
		this.redundancy = redundancy;
		
		/* field head�� ������ �� */
		field temp = Main.mg.fieldHead;
		
		//�ʵ尡 �ϳ��� ���� ���
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
	 * ��������� ��� �����͸� �����ϰ� ������ ����
	 * ���������� null�� �����Ͽ� ���ε� ����
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
	 * �ʵ� ��� ���� �Լ�
	 * 1. �ʵ� �̸�
	 * 2. �ʵ� ��ġ
	 * 3. �ʵ� ����
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
