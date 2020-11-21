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
 * 1. DB의 전반적인 내용을 다루는 클래스
 ------------------------------------------------------*/

public class management{
	/*-----[ MAX_DEGREE ] ------
	 * 현재는 3으로 고정 되어 있으나
	 * 언제든 입력 받아서 구현할 수 있도록
	 * Max Degree의 값인 M을 활용하여
	 * 일반화 하여 구현하였음
	 -------------------------*/
	static final int MAX_DEGREE=5;
	public int M = MAX_DEGREE;
	
	public node root;
	/* 데이터 head를 저장함으로 first에 접근 */
	public data first;
	
	/* 현재 위치한 노드의 위치를 저장함으로 cur recde에 접근 */
	public node curNode;
	public int curIndex = 0;
	
	public int recNum;
	public int fieldNum;
	
	//필드 정보, 링크드 리스트로 구현된 필드의 헤드를 저장
	field fieldHead;
	
	//정렬 기준이 되는 필드의 위치
	int sortField = -1;
	//file input-output
	long fileSize;
	int dataCount;
	/*-----[ void init() ]------------
	 * 기본설정으로 초기화
	 * 레코드 갯수 0개
	 * Field 갯수 0개
	 * 처음, 마지막, 현재의 필드 모두 null
	 -------------------------------*/
	public void init() {
		//root node 역할을 할 빈 node를 하나 생성한다.
		root = new leafNode();
		/* 저장된 필드를 모두 삭제 */
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
	 *  DB의 레코드 수를 관리하는 메소드들
	 * 	1. 레코드의 갯수가 늘어남을 카운트
	 *  2. 레코드의 갯수가 줄어듬을 카운트
	 *  3. 현재 레코드의 갯수 리턴
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
	 * MAX DEGREE 반환
	 ----------------*/
	int get_M() {
		return this.M;
	}
	/*--------[ Recode Instance Management ]--------
	 * 1. root를 반환
	 * 2. root를 설정  
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
	 * field 관련 함수
	 * 1. 필드의 추가에 따른 갯수 증가 함수
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
	 *  1. DB 필수 정보를 저장
	 *  2. recode 데이터를 저장
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
						//이미 저장된 data라면
						if(leaf.dataArr[j].getFilePosition(k) != -1) {
							target = target.next;
							break;
						}

						
						
						long size = curData.length();
						if(size == 0) 
							curData = "-";
						size+= 1;
						
						raf.write(curData.getBytes());
						System.out.println(curData+"저장");
						if(k != sortFieldPosition ) target.setData("");
						target.filePosition = dataCount;
						target.dataSize = size;
						
						
						
						byte[] sep2 = ",".getBytes();
						System.out.println("저장위치/크기 : "+dataCount+"/"+size);
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
			System.out.println("저장완료");
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
			byte[] b = new byte[1024]; // 입력 저장 byte array
			int byteIndex = 0; // byte 위치 = 바이트 배열 길이
			int LoadState = 1; //주석의 숫자가 현재 읽는 데이터
			
			/* ----------load Field ---------------*/
			/* 필드 저장 정보 변수 */
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
			/* 데이터 저장 정보 변수 */
			File f = new File(fileName+".dat");
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			data loadHead = null;
			data temp;
			String loadData;
			
			//빈 DB에 Load할경우
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
	
					System.out.println("위치/크기 : "+fPosition+"/"+fSize);
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
			File file = new File("C:\\Users\\손상훈\\Desktop\\simpledb\\winemag-data-130k-v2 (2).csv");
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
				//System.out.println(array[i]+"추가완료");
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
			System.out.println("추가된필드  : " + Main.mg.getFieldNum() + "개");
			*/
			
			int sum=0;
			/* 데이터 저장 단계 */
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
				
				//초기 생성
				data dataList = new data(array[0]);
				int c=1;
				for(; c<Main.mg.fieldNum; c++) {
					dataList.addData(new data(array[c]));
					
				}
				String inputData = dataList.getData(0);

				//들어갈 leaf에 접근
				leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);	
				//데이터를 삽입한다.
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
