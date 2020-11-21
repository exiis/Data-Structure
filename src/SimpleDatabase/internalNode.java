package SimpleDatabase;

public class internalNode extends node {
   /* internal node member */
   String[] key = new String[M];
   node leftSibling;
   node rightSibling;
   
   /* 링크드 리스트로 저장된 데이터의 헤더를 배열에 저장, M의 갯수에 따라 초기화 */
   node[] child = new node[M+1];
   
   
   internalNode() {
      super();
   }
   
   //새로운 노드를 만들 때 사용하는 생성자
   internalNode(String key){
      super();
      this.key[n] = key;
      n++;
   }
   
   //기존의 노드에 새로운 키를 삽입하는 함수
   void addKey(String key, node newChild) {
      int i=0; //새로 입력된 key가 가지는 자리
      
      /* 현재 데이터 수만큼 반복 */
      for(i=n; i>0; i--) {
         //새로운 키가 비교 대상보다 크면 오른쪽에 삽입
         if(key.compareTo(this.key[i-1]) > 0) {
            this.key[i] = key;
            break;
         }
         //작으면 비교한 데이터를 오른쪽으로 밀어버림
         else {
            this.key[i] = this.key[i-1];
         }
      }
      
      //마지막까지 삽입이 안된 경우
      if(i==0) this.key[0] = key;
      
      //새로운 child의 위치를 만들어주고 삽입
      for(int j=n; j>i; j--) {
         child[j+1] = child[j];
      }
      child[i+1] = newChild;
      
      newChild.parents = this;
      n++;
      
      //키가 추가되었기 때문에 트리 모양에 대한검사
      if(n == M) {
         //System.out.println("interSplit");
         split();
         
      }
         
   }
   
   void split() {
      int middle = (int)M/2;
      int CM = (int)Math.ceil(M/2.0);//ceil M
      
      /* 새로운 노드 n을 셋팅 */
      internalNode n = new internalNode();
      
      /* 새로운 노드에다가 child를 복사 */
      /* 새로운 노드가 가져가는 갯수는 ceil(M/2) 개 -> CM번 반복 */
      /* 새로운 노드가 가져가는 자식의 시작점은 middle의 오른쪽 자식부터니까 middle+1 */
      for(int i=0; i<CM; i++) {
         n.child[i] = this.child[i+middle+1];
         this.child[i+middle+1].parents = n;
         this.child[i+middle+1] = null;
      }
      
      /* 새로운 노드에다가 key 복사 */
      /* 새로운 노드가 가져가는 키의 갯수는 ceil(M/2)-1개  -> CM-1번 반복*/       
      /* middle key는 부모로 올라가서 middle+1 부터 복사 */
      for(int i=0; i<CM-1; i++) {
         n.key[i] = this.key[middle+1+i];
         this.key[middle+1+i] = null;
         n.n++;
         this.n--;
      }
      /* 기존 노드와 연결 */
      if(this.rightSibling!= null) {
         n.rightSibling = this.rightSibling;
         ((internalNode)this.rightSibling).leftSibling = n;
      }
      
      /* 둘을 연결 */
      this.rightSibling = n;
      n.leftSibling = this;      
      
      //부모 노드가 없을 경우
      if(this.parents == null) {
         //middle은 p로 올리고 삭제함
         internalNode p = new internalNode(key[middle]);
         key[middle] = null;
         this.n--;
         
         //부모의 자식 연결
         p.child[0] = this;
         p.child[1] = n;
         
         //자식의 부모 연결
         this.parents = p;
         n.parents = p;
         
         //부모가 없는 internal Node의 부모를 생성하였으므로 root를 갱신
         Main.mg.setRoot(p);
      }
      
      //부모노드가 존재할 경우
      else {
         internalNode temp = (internalNode)this.parents;
         temp.addKey(key[middle], n);
         this.key[middle] = null;
         this.n--;
      }
   }   
   
   void rebuilding() {
      //root는 N의 제약을 받지 않음, 단 N=0일 때는 root를 바꿔야 하나 밑의 merge연산에서 처리 
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
         //right의 부모의 key에서 작업이 실행됨, 맨 왼쪽 노드의 경우는 유지
         if(this.leftSibling != null ) parIndex++;
         
         //빌린데이터로 node 설정
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
         //left로 key 복사
         left.key[left.n] = p.key[parIndex];
         left.n++;
         for(int i=0; i<this.n; i++) 
            left.key[left.n+i] = this.key[i];
         
         //left로 child 복사 + set parents 
         int ln = left.n;
         for(int i=0; i<this.n+1; i++){
            left.child[ln+i] = this.child[i];
            left.child[ln+i].parents = left;
            left.n++;
         }
         left.n--;
         
         left.rightSibling = this.rightSibling;
         if(this.rightSibling != null) ((internalNode)this.rightSibling).leftSibling = left;
         
         
         //parents key 내려갔으므로 n-1, 당김, child 당김
         for(int i=0; i<p.n-parIndex-1; i++)  
            p.key[parIndex+i] = p.key[parIndex+i+1];
         p.key[p.n-1] = null;
         for(int i=0; i<p.n-parIndex-1; i++)
            p.child[parIndex+1+i] = p.child[parIndex+2+i];
         p.child[p.n] = null;
         p.n--;
         
         //p가 root이고 이 연산으로 인하여 갯수가 0이 되었을 때, 부모를 삭제, left를 root로 변경
         if(p == Main.mg.getRoot() && p.n == 0) {
            left.parents = null;
            Main.mg.setRoot(left);
         }
         //부모에 대하여 다시 검사
         else if (p.n < Main.mg.M/2)
            p.rebuilding();
      }
      //right merge
      else if(this.rightSibling != null && this.parents == this.rightSibling.parents) {
    	 //System.out.println("right merge");
         internalNode right = (internalNode)this.rightSibling;
         
         //right의 부모의 key에서 작업이 실행됨, 왼쪽 형제가 없을시 증가 안함
         if(this.leftSibling != null) parIndex++;
         
         //right Sibling에 merge할 자리를 마련함
         for(int i=0; i<right.n; i++) 
            right.key[right.n+this.n-i] = right.key[right.n-1-i];
         
         for(int i=0; i<right.n+1; i++)
            right.child[right.n+1+this.n-i] = right.child[right.n-i];
         
         //key 복사 : this node + 부모에서 주는 key
         for(int i=0; i<this.n; i++) {
            right.key[i] = this.key[i];
            right.n++;
         }
         right.key[this.n] = p.key[parIndex];
         right.n++;
         
         //child 복사, parents 설정, n증가
         for(int i=0; i<this.n+1; i++) {
            right.child[i] = this.child[i];
            right.child[i].parents = this.rightSibling;
         }
         
         //연결
         right.leftSibling = this.leftSibling;
         if(this.leftSibling != null) ((internalNode)this.leftSibling).rightSibling = right;   
 
         //parents에서 key를 가지고 왔으므로 p의 key, child를 땡겨옴
         for(int i=0; i<p.n-parIndex-1; i++) 
        	 p.key[parIndex+i] = p.key[parIndex+i+1];
        
         for(int i=0; i<p.n-parIndex; i++)
        	 p.child[parIndex+i] = p.child[parIndex+i+1];
         
         
         p.child[p.n] = null;
         p.n--;
         p.key[p.n] = null;
         
         
         //p가 root이고 이 연산으로 인하여 갯수가 0이 되었을 때, 부모를 삭제, right를 root로 변경
         if(p == Main.mg.getRoot() && p.n == 0) {
            right.parents = null;
            Main.mg.setRoot(right);
         }
         //부모에 대하여 다시 검사
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