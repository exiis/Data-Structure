package SimpleDatabase;

public class node {
	int M; // Max degree
	int n; //number of recode or key
	node parents;
	
	/*------[ 생성자 ]----------
	 * 
	 ------------------------*/
	node() {
		this.M = Main.mg.get_M();
		this.n = 0;
		parents = null;
	}
	
	
	/*----------------- [ findLeaf ] --------------------
	 *  삽입 / 삭제 과정에서 그 과정이 일어날 leaf 노드를 찾는 함수
	 *  internal Node의 경우 value값에 알맞은 자식들을 계속 찾아감
	 *  찾아 내려가다 leaf Node를 발견하면 return
	 --------------------------------------------------*/
	node findLeaf(String inputData) {
		node temp = Main.mg.root;
		
		while(true) {
			//temp is Leaf Node
			if(temp instanceof leafNode) 
				return temp;
			
			//temp is Internal Node
			else if(temp instanceof internalNode) {
				int n = temp.n;
				//System.out.println("n = "+n);
				int i;
				for(i=n-1; i>-1; i--) {
					if(inputData.compareTo(((internalNode) temp).key[i]) >= 0) {
						temp = ((internalNode)temp).child[i+1];
						break;
					}
				}
				if(i==-1) {
					temp = ((internalNode)temp).child[0];
				}
			}
		}
	}
	
	int getN() {
		return this.n;
	}

}
