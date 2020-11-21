package SimpleDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/*----------------management Class---------------------
 * 1. DB�� �������� ������ �ٷ�� Ŭ����
 ------------------------------------------------------*/

public class management{
	/*-----[ MAX_DEGREE ] ------
	 * ����� 3���� ���� �Ǿ� ������
	 * ������ �Է� �޾Ƽ� ������ �� �ֵ���
	 * Max Degree�� ���� M�� Ȱ���Ͽ�
	 * �Ϲ�ȭ �Ͽ� �����Ͽ���
	 -------------------------*/
	static final int MAX_DEGREE=5;
	public int M = MAX_DEGREE;
	
	public node root;
	/* ������ head�� ���������� first�� ���� */
	public data first;
	
	/* ���� ��ġ�� ����� ��ġ�� ���������� cur recde�� ���� */
	public node curNode;
	public int curIndex = 0;
	
	public int recNum;
	public int fieldNum;
	
	//�ʵ� ����, ��ũ�� ����Ʈ�� ������ �ʵ��� ��带 ����
	field fieldHead;
	
	//���� ������ �Ǵ� �ʵ��� ��ġ
	int sortField = -1;
	//file input-output
	long fileSize;
	int dataCount;
	/*-----[ void init() ]------------
	 * �⺻�������� �ʱ�ȭ
	 * ���ڵ� ���� 0��
	 * Field ���� 0��
	 * ó��, ������, ������ �ʵ� ��� null
	 -------------------------------*/
	public void init() {
		//root node ������ �� �� node�� �ϳ� �����Ѵ�.
		root = new leafNode();
		/* ����� �ʵ带 ��� ���� */
		if(this.fieldHead != null)
			fieldHead.reset();
		fieldHead = null;
		recNum = 0;
		fieldNum = 0;
		curIndex = 0;
		curNode = null;
		sortField = -1;
		fileSize=0;
		dataCount = 0;
	}
	
	
	/*--------[ DB Count Management ]----------
	 *  DB�� ���ڵ� ���� �����ϴ� �޼ҵ��
	 * 	1. ���ڵ��� ������ �þ�� ī��Ʈ
	 *  2. ���ڵ��� ������ �پ���� ī��Ʈ
	 *  3. ���� ���ڵ��� ���� ����
	 ------------------------------------*/
	public void recIn() {
		this.recNum++;
	}

	public void recOut() {
		this.recNum--;
	}
	
	public int getRecNum() {
		return this.recNum;
	}
	
	/*---------------
	 * MAX DEGREE ��ȯ
	 ----------------*/
	int get_M() {
		return this.M;
	}
	/*--------[ Recode Instance Management ]--------
	 * 1. root�� ��ȯ
	 * 2. root�� ����  
	 ----------------------------------------------*/
	node getRoot() {
		return this.root;
	}
	
	void setRoot(node root) {
		this.root = root;
	}
	
	void setCurNode() {
		node temp = root;
		this.curIndex = 0;
		if(temp instanceof leafNode) this.curNode = temp;
		else 
			while(true) {
				if(((internalNode)temp).child[0] instanceof internalNode)
					temp = ((internalNode)temp).child[0];
				else {
					this.curNode = (leafNode)((internalNode)temp).child[0];
					break;
				}
			}
	}
	
	/*---------------------------
	 * field ���� �Լ�
	 * 1. �ʵ��� �߰��� ���� ���� ���� �Լ�
	 ---------------------------*/
	void addField(int fieldPosition, field newField) {
		if(this.fieldNum == 0) {
			this.setHead(newField);
			this.fieldIn();
		}
		
		else {
			field temp = this.fieldHead;
			//move
			for(int i=0; i<fieldPosition-1; i++) 
				temp = temp.next;
			temp.next = newField;
			this.fieldIn();
		}
			
	}
	void fieldIn() {
		fieldNum++;
	}
	
	int getSortField() {
		return this.sortField;
	}
	
	int getFieldNum() {
		return this.fieldNum;
	}
	
	void setHead(field head) {
		this.fieldHead = head;
	}
	
	String getFieldName(int i) {
		return this.fieldHead.getName(i);
	}
	
	boolean fieldIsNull() {
		if(this.fieldNum == 0) return true;
		else return false;
	}
	
	/*-------------[ Save ]---------------
	 *  1. DB �ʼ� ������ ����
	 *  2. recode �����͸� ����
	 -----------------------------------*/
	public void Save(String fileName) {
		BufferedOutputStream bs = null;
		BufferedOutputStream bs2 = null;
		try {
			int fieldNumber = Main.mg.getFieldNum();
			int sortFieldPosition = Main.mg.getSortField();
			byte[] fieldNum = Integer.toBinaryString(fieldNumber).getBytes();
			byte[] recNum = Integer.toBinaryString(Main.mg.getRecNum()).getBytes();
			byte[] sortField = Integer.toBinaryString(sortFieldPosition).getBytes();
			
			byte[] sep = ",".getBytes();
			
			
			bs = new BufferedOutputStream(new FileOutputStream(fileName+".def"));
			
			/*-----------------------------------------------*/
			/*                   .def file                   */
			/* 1. field number 2. recode number 3. sortField */
			/* 4. field Information                          */
			/*-----------------------------------------------*/
			bs.write(fieldNum);
			bs.write(sep);
			bs.write(recNum);
			bs.write(sep);
			bs.write(sortField);
			bs.write(sep);

			//field Info
			field head = Main.mg.fieldHead;
			for(int i=0; i<Main.mg.getFieldNum(); i++) {
				
				System.out.println("");
				byte[] position = Integer.toBinaryString(head.position).getBytes();				
				byte[] length = Integer.toBinaryString(head.length).getBytes();
				byte[] redundancy;
				String type = head.fieldType;
				String name = head.name;
				
				if(head.redundancy) redundancy = Integer.toBinaryString(1).getBytes();
				else redundancy = Integer.toBinaryString(0).getBytes();
				
				System.out.println(head.position+" "+ head.length+" "+ type+" "+ name);
				//Save field Infor
				bs.write(position);
				bs.write(sep);
				
				bs.write(length);
				bs.write(sep);
				
				bs.write(type.getBytes());
				bs.write(sep);
				
				bs.write(name.getBytes());
				bs.write(sep);
				
				bs.write(redundancy);
				bs.write(sep);
				head = head.next;
			}
			bs.close();
			/*-----------------------
			 * Data file
			 ----------------------*/
			File f = new File(fileName+".dat");
			bs2 = new BufferedOutputStream(new FileOutputStream(fileName+"_.def"));
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			
			leafNode leaf;
			node temp = root;
			while(true) {
				if(temp instanceof leafNode) {
					leaf = (leafNode)temp;
					break;
				}
				else {
					temp = ((internalNode)temp).child[0];
				}
			}
			
			while(leaf != null) {
				for(int j=0; j<leaf.n; j++) {
					data target = leaf.dataArr[j];
					System.out.println("data"+j);
					System.out.println("loop : "+fieldNumber);
					for(int k=0; k<fieldNumber; k++) {
						String curData = target.getData(k);
						//�̹� ����� data���
						if(leaf.dataArr[j].getFilePosition(k) != -1) {
							target = target.next;
							break;
						}

						
						
						long size = curData.length();
						if(size == 0) 
							curData = "-";
						size+= 1;
						
						raf.write(curData.getBytes());
						System.out.println(curData+"����");
						if(k != sortFieldPosition ) target.setData("");
						target.filePosition = dataCount;
						target.dataSize = size;
						
						
						
						byte[] sep2 = ",".getBytes();
						System.out.println("������ġ/ũ�� : "+dataCount+"/"+size);
						bs2.write(Integer.toBinaryString(dataCount).getBytes());
						bs2.write(sep2);
						bs2.write(Integer.toBinaryString((int)size).getBytes());
						bs2.write(sep2);
						
						dataCount += size;
					}
					target = target.next;
				}
				leaf = (leafNode)leaf.next;
			}
			bs.close();
			bs2.close();
			raf.close();
			System.out.println("����Ϸ�");
		} catch(IOException e) {	
		}	
	}
	
	public void Load(String fileName) {
		BufferedInputStream bs = null;
		BufferedInputStream bs2 = null;		
		try {
			//seperater = ","
			bs = new BufferedInputStream(new FileInputStream(fileName+".def"));
			bs2 = new BufferedInputStream(new FileInputStream(fileName+"_.def"));
			
			/*-----------------------------------------------*/
			/*                   .def file                   */
			/* 1. field number 2. recode number 3. sortField */
			/* 4. field Information                          */
			/*-----------------------------------------------*/
			int fieldNum = 0;
			int recNum=0;
			int sortField=0;
			
			byte data;
			byte[] b = new byte[1024]; // �Է� ���� byte array
			int byteIndex = 0; // byte ��ġ = ����Ʈ �迭 ����
			int LoadState = 1; //�ּ��� ���ڰ� ���� �д� ������
			
			/* ----------load Field ---------------*/
			/* �ʵ� ���� ���� ���� */
			int position=0;
			int length=0;
			String type=null;
			String name=null;
			boolean redundancy=true;
			
			while((data = (byte)bs.read()) != -1) {
				if((char)data != ',') {
					b[byteIndex] = data;
					byteIndex++;
				}
				//Management Infor Load
				else{
					//make integer value
					switch(LoadState) {
						case 1:
							fieldNum = byteToInt(byteIndex, b);
							LoadState++;
							break;
						case 2:
							recNum = byteToInt(byteIndex, b);;
							LoadState++;
							break;
						case 3:
							sortField = byteToInt(byteIndex, b);
							LoadState++;
							break;
						//field position
						case 4:
							position = byteToInt(byteIndex, b);
							LoadState++;
							break;
						//field length
						case 5:
							length = byteToInt(byteIndex, b);
							LoadState++;
							break;
						//field type
						case 6:
							type = new String(b);
							type = type.substring(0, byteIndex);
							LoadState++;
							break;
						//field name
						case 7:
							name = new String(b);
							name = name.substring(0, byteIndex);
							LoadState++;
							break;
						//field redundancy
						case 8:
							int state = byteToInt(byteIndex, b);
							if(state == 1 ) redundancy = true;
							else redundancy = false;
							
							//make field
							System.out.println(position +", "+length+", "+type+", "+name+", "+redundancy);
							new field(position, length, type, name, redundancy);
							// init var and State
							LoadState = 4;
							position=0;
							length=0;
							type=null;
							name=null;
							redundancy=true;
							break;
						default :
							System.out.println("error");
						break;
					}
					byteIndex = 0;
				}
			}
				
			/* ----------load Data ---------------*/
			/* ������ ���� ���� ���� */
			File f = new File(fileName+".dat");
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			data loadHead = null;
			data temp;
			String loadData;
			
			//�� DB�� Load�Ұ��
			if(Main.mg.recNum == 0) {
				int dataCount = 0;
				long fPosition=0;
				int fSize=0;
				LoadState = 0;
				
				byte[] bt = new byte[32];
				System.out.println(recNum);
				for(int i=0; i<recNum*fieldNum; i++) {
					while(fSize == 0 && (data = (byte)bs2.read()) != -1) {
						// get file information
						if((char)data != ',') {
							bt[byteIndex] = data;
							byteIndex++;
						}
						//Management Infor Load
						else {
							switch(LoadState) {
								case 0:
									fPosition = byteToInt(byteIndex, bt);
									byteIndex = 0;
									LoadState++;
									break;
									
								case 1:
									fSize = byteToInt(byteIndex, bt);
									LoadState++;
									byteIndex=0;
									LoadState = 0;
									break;
							}
						}
					}
	
					System.out.println("��ġ/ũ�� : "+fPosition+"/"+fSize);
					raf.seek(fPosition);
					byte[] fData = new byte[fSize];
					for(int j=0; j<fSize; j++) 
						raf.read(fData);
					loadData = new String(fData);
					System.out.println(loadData);
					
					fPosition=0;
					fSize=0;
				
					
					if(dataCount == 0) {
						temp = new data(loadData);
						loadHead = temp;
						dataCount++;
					}
					else {
						loadHead.addData(new data(loadData));
						dataCount++;
					}
					
					if(dataCount == fieldNum) {
						String inputData = loadHead.getData(sortField);			
						leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);
						leaf.addData(loadHead);
						Main.mg.recNum++;
						dataCount = 0;
						if(Main.mg.recNum == 4000) break;
					}
					LoadState = 0;
				}
			}	
		}catch(FileNotFoundException e1) {
			System.out.println("?");
		} catch(IOException e) {	
			e.printStackTrace();
		}	
	}
	
	public void dataLoad() {
		
	}
	
	public void textLoad(String fileName) {
		try {
			File file = new File("C:\\Users\\�ջ���\\Desktop\\simpledb\\winemag-data-130k-v2 (2).csv");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Main.mg.sortField = 0;
			String line = "";
			line = br.readLine();
			String[] array = line.split(",");
			
			int[] length = {4, 50, 1000, 50, 3, 4, 50, 50, 50, 50, 50, 200, 50, 50};
			String[] type = {"Integer", "String","String", "String","Integer","Integer", "String", "String", "String", "String", "String", "String", "String", "String" };
			
			new field(0, length[0], type[0], "id", false);
			//make field
			for(int i=1; i<array.length; i++) {
				new field(i, length[i], type[i], array[i], true);
				//System.out.println(array[i]+"�߰��Ϸ�");
			}
			Main.mg.fieldHead.redundancy = false;
			
			/* check field
			field temp = Main.mg.fieldHead;
			int i;
			for(i=0; i<Main.mg.fieldNum; i++) {
				System.out.print(temp.name+" ");
				if(temp.next != null) temp = temp.next;
			}
			System.out.println("");
			System.out.println("�߰����ʵ�  : " + Main.mg.getFieldNum() + "��");
			*/
			
			int sum=0;
			/* ������ ���� �ܰ� */
			while((line = br.readLine()) != null) {
				for(int i=0; i<array.length; i++) 
					array[i] = null;
				
				if(sum == 18881) {
					line = line+br.readLine();
					line = line+br.readLine();
				}
				
				if(sum == 21516) {
					line = line+br.readLine();
				}
				
				if(sum == 51394)
					line = line+br.readLine();
				
				int start = 0;
				int end = 0;
				int count = 0;
				boolean str = false;
				for(int i=0; i<line.length(); i++) {
					if(count == 13) {
						array[13] = line.substring(start);
						break;

					}
					if(line.charAt(i) == '\"' && !str) {
						str = true;
						end++;
					}
					else if(line.charAt(i) == '\"' && str) {
						str = false;
						end++;
					}
					else if(str) {
						end++;
					}
					else if(line.charAt(i) != ',')
						end++;
					else{
						array[count] = line.substring(start, end);
						count++;
						start = end+1;
						end = start;
					}
				}
				for(int i=0; i<array.length; i++) 
					System.out.print(array[i]+"/");
				System.out.println();
				
				//�ʱ� ����
				data dataList = new data(array[0]);
				int c=1;
				for(; c<Main.mg.fieldNum; c++) {
					dataList.addData(new data(array[c]));
					
				}
				String inputData = dataList.getData(0);

				//�� leaf�� ����
				leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);	
				//�����͸� �����Ѵ�.
				leaf.addData(dataList);
				sum++;
				Main.mg.recIn();
			}
			br.close();
		}
		catch(IOException e) {
			
		}
	}
	public int byteToInt(int byteIndex, byte[] b) {
		int value=0;
		for(int i=0; i<byteIndex; i++) {
			char temp = (char)b[i];
			value += (temp-48)*Math.pow(2.0, (double)(byteIndex-i-1));
		}
		return value;
	}

}
