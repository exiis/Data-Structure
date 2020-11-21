package SimpleDatabase;

public class node {
	int M; // Max degree
	int n; //number of recode or key
	node parents;
	
	/*------[ ������ ]----------
	 * 
	 ------------------------*/
	node() {
		this.M = Main.mg.get_M();
		this.n = 0;
		parents = null;
	}
	
	
	/*----------------- [ findLeaf ] --------------------
	 *  ���� / ���� �������� �� ������ �Ͼ leaf ��带 ã�� �Լ�
	 *  internal Node�� ��� value���� �˸��� �ڽĵ��� ��� ã�ư�
	 *  ã�� �������� leaf Node�� �߰��ϸ� return
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
