package SimpleDatabase;

public class develope {
	develope(){}
	void showField() {
		field temp = Main.mg.fieldHead;
		System.out.println("Print Field Name");
		System.out.println("위치 :  이름  /  필드타입  /  길이  ");
		while(temp != null) {
			System.out.println(temp.position+" : "+temp.name+" / "+temp.fieldType+" / "+temp.length);
			temp = temp.next;
		}
		System.out.println("End Print.");
	}
	
	/* 매개변수의 숫자만큼 중복 가능한 랜덤한 필드를 생성함 */
	void fieldAddMacro(int num) {
		String type = null;
		String name;
		
		for(int i=0; i<num; i++) {
			name = "field"+Integer.toString(i);
			//select type
			switch((int)(Math.random()*10)%3) {
			case 0:
				type = "String";
				break;
			case 1:
				type = "Integer";
				break;
			case 2:
				type = "Char";
				break;
			case 3:
				type = "Double";
				break;
			}
			new field(Main.mg.getFieldNum(), 10, type, name, true);
			
		}
	}
	
	void setSort(int num) {
		Main.mg.sortField = num-1;
	}
	
	void dataAddMacro(int num) {
		int fieldNum = Main.mg.getFieldNum();

		long start = System.currentTimeMillis();
		//num의 갯수만큼 recode를 추가함
		for(int i=0; i<num; i++) {
			data temp = null;
			data temphead = null;
			
			/* make data */
			for(int j=0 ; j<fieldNum; j++) {
				String str1 = "";
				String str2 = "0";
				int e=0;
				
				if(j/1000000>1) e=0;
				else if(i/100000>=1) e=1;
				else if(i/10000>=1) e=2;
				else if(i/1000>=1) e=3;
				else if(i/100>=1) e=4;
				else if(i/10>=1) e=5;
				else e=6;
			
				for(int k=0; k<e; k++) { 
					str1 = str1+str2;
				}
				
				if(j==0) {
					temp = new data(str1+Integer.toString(i));
					temphead = temp;
				}
				else {
					temphead.addData(new data(str1 + Integer.toString(i)));
				}
			}
			
			/* get sort Data */
			String inputData = temphead.getData(Main.mg.sortField);			
			leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);
			System.out.println(i+"번째 삽입...");
			leaf.addData(temphead);
			Main.mg.recNum++;
			System.out.println(i+"번째 완료");
			//show();
		}
		long end = System.currentTimeMillis();
		System.out.println("start : " + start/1000.0);
		System.out.println("end : " + end/1000.0);
		System.out.println("running time : " + (end - start)/1000.0);
		
		
	}
	
	
	void show() {
		node temp = Main.mg.getRoot();
		if(temp instanceof internalNode) System.out.println("root = Internal");
		else System.out.println("root = leaf");
		while(true) {
			// internal node의 경우
			if(temp instanceof internalNode) {
				node nextTemp;
				if(((internalNode) temp).child[0] instanceof internalNode) nextTemp = ((internalNode) temp).child[0];
				else nextTemp = ((internalNode)temp).child[0]; 
				//int sum = 0;
				//int sum2 = 0;
				while(true) {
					System.out.print("[");
					for(int i=0; i<temp.n; i++) {
						System.out.print(((internalNode)temp).key[i]+" ");
						//sum++;
					}
					//int cc=0;
					for(int i=0; i<temp.n+1; i++) {
						//if(((internalNode) temp).child[i]!=null) cc++;
						//else System.out.print("!!-"+i+"-!!(null)");
					}
					//System.out.print("child Number : " + cc);
					System.out.print("]");
					//System.out.print(temp);
					//sum2++;
					//int count=0;
					//for(int i=0; i<6; i++) 
						//if(((internalNode)temp).child[i] != null) count ++;
					//System.out.print("C="+count);
					if(((internalNode)temp).rightSibling != null) temp = ((internalNode)temp).rightSibling;
					else break;
				}
				System.out.println();
				//System.out.println("sum = "+sum+"("+sum2+")");
				//다음 level의 제일 왼쪽 노드를 temp로
				temp = nextTemp;
			}
			
			else if(temp instanceof leafNode) {
				//int sum=0;
				while(temp != null) {
					//sum+=temp.n;
					System.out.print("[");
					for(int i=0; i<temp.n; i++) 
						System.out.print(((leafNode) temp).dataArr[i].getData(Main.mg.sortField)+" ");
					System.out.print("]");
					//System.out.println(temp.parents);
					System.out.print("->");
					temp = ((leafNode) temp).next;
				}
				System.out.println();
				//System.out.println("sum = "+sum);
				return; //모든 leaf를 출력하면 종료
			}
		}
	}
}
