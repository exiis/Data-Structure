package SimpleDatabase;

public class data {
	String data;
	data next;
	long filePosition = -1;
	long dataSize = -1;
	/*-------------------[ ������ ]-------------------
	 * data�� �Է� �޾� �����͸� �����Ѵ�
	 * ���� Linked List�� �����Ͽ� ����
	 -----------------------------------------------*/	
	 public data(String data) {		
		this.data = data;
		next = null;
	}
	 
	 //head�� ������ �Ǿ�� call �� �� ����
	 public void addData(data newData) {
		 data temp = this;
		//������ ������ temp �̵�
		 while(temp.next != null) {
			 temp = temp.next;
		 }
		 //tail ��ġ�� newData ����
		 temp.next = newData;
	 }

	/* ------------------------
	 * ��� ���� ���� �� ����
	 * 1. i��° �ʵ��� �����͸� ����
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