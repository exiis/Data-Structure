package SimpleDatabase;

public class internalNode extends node {
   /* internal node member */
   String[] key = new String[M];
   node leftSibling;
   node rightSibling;
   
   /* ��ũ�� ����Ʈ�� ����� �������� ����� �迭�� ����, M�� ������ ���� �ʱ�ȭ */
   node[] child = new node[M+1];
   
   
   internalNode() {
      super();
   }
   
   //���ο� ��带 ���� �� ����ϴ� ������
   internalNode(String key){
      super();
      this.key[n] = key;
      n++;
   }
   
   //������ ��忡 ���ο� Ű�� �����ϴ� �Լ�
   void addKey(String key, node newChild) {
      int i=0; //���� �Էµ� key�� ������ �ڸ�
      
      /* ���� ������ ����ŭ �ݺ� */
      for(i=n; i>0; i--) {
         //���ο� Ű�� �� ��󺸴� ũ�� �����ʿ� ����
         if(key.compareTo(this.key[i-1]) > 0) {
            this.key[i] = key;
            break;
         }
         //������ ���� �����͸� ���������� �о����
         else {
            this.key[i] = this.key[i-1];
         }
      }
      
      //���������� ������ �ȵ� ���
      if(i==0) this.key[0] = key;
      
      //���ο� child�� ��ġ�� ������ְ� ����
      for(int j=n; j>i; j--) {
         child[j+1] = child[j];
      }
      child[i+1] = newChild;
      
      newChild.parents = this;
      n++;
      
      //Ű�� �߰��Ǿ��� ������ Ʈ�� ��翡 ���Ѱ˻�
      if(n == M) {
         //System.out.println("interSplit");
         split();
         
      }
         
   }
   
   void split() {
      int middle = (int)M/2;
      int CM = (int)Math.ceil(M/2.0);//ceil M
      
      /* ���ο� ��� n�� ���� */
      internalNode n = new internalNode();
      
      /* ���ο� ��忡�ٰ� child�� ���� */
      /* ���ο� ��尡 �������� ������ ceil(M/2) �� -> CM�� �ݺ� */
      /* ���ο� ��尡 �������� �ڽ��� �������� middle�� ������ �ڽĺ��ʹϱ� middle+1 */
      for(int i=0; i<CM; i++) {
         n.child[i] = this.child[i+middle+1];
         this.child[i+middle+1].parents = n;
         this.child[i+middle+1] = null;
      }
      
      /* ���ο� ��忡�ٰ� key ���� */
      /* ���ο� ��尡 �������� Ű�� ������ ceil(M/2)-1��  -> CM-1�� �ݺ�*/       
      /* middle key�� �θ�� �ö󰡼� middle+1 ���� ���� */
      for(int i=0; i<CM-1; i++) {
         n.key[i] = this.key[middle+1+i];
         this.key[middle+1+i] = null;
         n.n++;
         this.n--;
      }
      /* ���� ���� ���� */
      if(this.rightSibling!= null) {
         n.rightSibling = this.rightSibling;
         ((internalNode)this.rightSibling).leftSibling = n;
      }
      
      /* ���� ���� */
      this.rightSibling = n;
      n.leftSibling = this;      
      
      //�θ� ��尡 ���� ���
      if(this.parents == null) {
         //middle�� p�� �ø��� ������
         internalNode p = new internalNode(key[middle]);
         key[middle] = null;
         this.n--;
         
         //�θ��� �ڽ� ����
         p.child[0] = this;
         p.child[1] = n;
         
         //�ڽ��� �θ� ����
         this.parents = p;
         n.parents = p;
         
         //�θ� ���� internal Node�� �θ� �����Ͽ����Ƿ� root�� ����
         Main.mg.setRoot(p);
      }
      
      //�θ��尡 ������ ���
      else {
         internalNode temp = (internalNode)this.parents;
         temp.addKey(key[middle], n);
         this.key[middle] = null;
         this.n--;
      }
   }   
   
   void rebuilding() {
      //root�� N�� ������ ���� ����, �� N=0�� ���� root�� �ٲ�� �ϳ� ���� merge���꿡�� ó�� 
      if(this == Main.mg.getRoot()) return;
      
      //System.out.println("Start Rebuilding.");
      
      internalNode p = (internalNode)this.parents;
      
      int parIndex = getParIndex(); //parents Index
      //left borrow
      if(this.leftSibling != null && this.leftSibling.n > M/2) {
    	 //System.out.println("left Borrow");
         internalNode left = (internalNode)this.leftSibling;
         
         //make borrowed key space
         for(int i=0; i<this.n; i++) 
            this.key[n-i] = this.key[n-i-1];
         //make borrowed child space
         for(int i=0; i<this.n+1; i++)
            this.child[n-i+1] = this.child[n-i];
         
         //set key and child
         this.key[0] = p.key[parIndex]; 
         this.child[0] = left.child[left.n];
         left.child[left.n] = null;
         this.child[0].parents = this;
         this.n++;
         p.key[parIndex] = left.key[left.n-1];
         left.key[left.n-1] = null;
         left.n--;
      }
      //right borrow
      else if(this.rightSibling != null && this.rightSibling.n > M/2) {
    	  //System.out.println("right Borrow");
         internalNode right = (internalNode)this.rightSibling;
         //right�� �θ��� key���� �۾��� �����, �� ���� ����� ���� ����
         if(this.leftSibling != null ) parIndex++;
         
         //���������ͷ� node ����
         this.key[n] = p.key[parIndex];
         this.child[n+1] = right.child[0];
         this.child[n+1].parents = this;
         this.n++;
         
         right.n--;
         
         //set p's key
         p.key[parIndex] = right.key[0];
         
         //move left key and data of right 
         for(int i=0; i<right.n; i++) 
            right.key[i] = right.key[i+1];
         right.key[right.n] = null;
         for(int i=0; i<right.n+1; i++)
            right.child[i] = right.child[i+1];
         right.child[right.n+1] = null;
         
      }
      
      //left merge
      else if(this.leftSibling != null && this.parents == this.leftSibling.parents) {
    	 //System.out.println("left merge");
         internalNode left = (internalNode)this.leftSibling;
         //left�� key ����
         left.key[left.n] = p.key[parIndex];
         left.n++;
         for(int i=0; i<this.n; i++) 
            left.key[left.n+i] = this.key[i];
         
         //left�� child ���� + set parents 
         int ln = left.n;
         for(int i=0; i<this.n+1; i++){
            left.child[ln+i] = this.child[i];
            left.child[ln+i].parents = left;
            left.n++;
         }
         left.n--;
         
         left.rightSibling = this.rightSibling;
         if(this.rightSibling != null) ((internalNode)this.rightSibling).leftSibling = left;
         
         
         //parents key ���������Ƿ� n-1, ���, child ���
         for(int i=0; i<p.n-parIndex-1; i++)  
            p.key[parIndex+i] = p.key[parIndex+i+1];
         p.key[p.n-1] = null;
         for(int i=0; i<p.n-parIndex-1; i++)
            p.child[parIndex+1+i] = p.child[parIndex+2+i];
         p.child[p.n] = null;
         p.n--;
         
         //p�� root�̰� �� �������� ���Ͽ� ������ 0�� �Ǿ��� ��, �θ� ����, left�� root�� ����
         if(p == Main.mg.getRoot() && p.n == 0) {
            left.parents = null;
            Main.mg.setRoot(left);
         }
         //�θ� ���Ͽ� �ٽ� �˻�
         else if (p.n < Main.mg.M/2)
            p.rebuilding();
      }
      //right merge
      else if(this.rightSibling != null && this.parents == this.rightSibling.parents) {
    	 //System.out.println("right merge");
         internalNode right = (internalNode)this.rightSibling;
         
         //right�� �θ��� key���� �۾��� �����, ���� ������ ������ ���� ����
         if(this.leftSibling != null) parIndex++;
         
         //right Sibling�� merge�� �ڸ��� ������
         for(int i=0; i<right.n; i++) 
            right.key[right.n+this.n-i] = right.key[right.n-1-i];
         
         for(int i=0; i<right.n+1; i++)
            right.child[right.n+1+this.n-i] = right.child[right.n-i];
         
         //key ���� : this node + �θ𿡼� �ִ� key
         for(int i=0; i<this.n; i++) {
            right.key[i] = this.key[i];
            right.n++;
         }
         right.key[this.n] = p.key[parIndex];
         right.n++;
         
         //child ����, parents ����, n����
         for(int i=0; i<this.n+1; i++) {
            right.child[i] = this.child[i];
            right.child[i].parents = this.rightSibling;
         }
         
         //����
         right.leftSibling = this.leftSibling;
         if(this.leftSibling != null) ((internalNode)this.leftSibling).rightSibling = right;   
 
         //parents���� key�� ������ �����Ƿ� p�� key, child�� ���ܿ�
         for(int i=0; i<p.n-parIndex-1; i++) 
        	 p.key[parIndex+i] = p.key[parIndex+i+1];
        
         for(int i=0; i<p.n-parIndex; i++)
        	 p.child[parIndex+i] = p.child[parIndex+i+1];
         
         
         p.child[p.n] = null;
         p.n--;
         p.key[p.n] = null;
         
         
         //p�� root�̰� �� �������� ���Ͽ� ������ 0�� �Ǿ��� ��, �θ� ����, right�� root�� ����
         if(p == Main.mg.getRoot() && p.n == 0) {
            right.parents = null;
            Main.mg.setRoot(right);
         }
         //�θ� ���Ͽ� �ٽ� �˻�
         else if(p.n < Main.mg.M/2)
            p.rebuilding();
      }   
   }
   
   int getParIndex() {
      internalNode par = (internalNode)this.parents;
      //child key < par Node's first key
      if(this.key[0].compareTo(par.key[0]) < 0) 
         return 0;
      //child key > par Node's last key
      else if(this.key[0].compareTo(par.key[par.n-1]) > 0)
         return par.n-1;
      
      //if par's i key < child key < par's i+1 key, then return i
      for(int i=0; i<par.n-1; i++) 
         if(this.key[0].compareTo(par.key[i]) > 0 && this.key[0].compareTo(par.key[i+1]) < 0)
            return i;
      
      System.out.println("error - not found parents index");
      return -1;
   }
}