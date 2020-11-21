package SimpleDatabase;

/* N = number of data */

public class leafNode extends node {
   /* leaf node member */
   data[] dataArr = new data[M];
   node next;
   node prior;
   //file read
   
   leafNode() {
      super();
   }
   
   /* Insert Data */
   void addData(data data){
      /* 데이터가 하나도 없을 경우 */
      if(dataArr[0] == null) {
         /* 입력할 데이터의 head를 0번째 요소로 추가, N증가 */
         dataArr[0] = data;
         n++;
      }
      
      /* 데이터가 하나라도 있을 경우 */
      else {
         int i;
         
         /* 정렬 기준 필드 위치 가지고와서 비교 */
         int position = Main.mg.sortField;
         String inputData = data.getData(position);
         
         /* 현재 데이터 수만큼 반복 */
         for(i=n; i>0; i--) {
            //새로운 데이터가 비교 대상보다 크면 오른쪽에 삽입
            if(inputData.compareTo(dataArr[i-1].getData(position)) > 0) {
               dataArr[i] = data;
               break;
            }
            //작으면 비교한 데이터를 오른쪽으로 밀어버림
            else 
               dataArr[i] = dataArr[i-1];
         }
         
         //마지막까지 삽입이 안된 경우
         if(i==0)  {
            dataArr[0] = data;
            Main.mg.curIndex = 0;
         }
         n++;

         /* Max Degree에 도달했는지 검사 */
         if(n == M) {
            System.out.println("Leaf split");
            split(position);
         }
      }
   }
   
   void split(int position) {
      int middle = (int)M/2;
      //key로 올릴 중앙 값 저장
      String key = dataArr[middle].getData(position);
      leafNode n = new leafNode();
      
      //새로운 leaf와 recode를 나눔
      for(int i=0; i<(int)Math.ceil(M/2.0); i++) {
         n.dataArr[i] = this.dataArr[i+middle];
         this.dataArr[i+middle] = null;
         n.n++;
         this.n--;
      }

      //기존의 node와 연결
      if(this.next != null) {
         n.next = this.next;
         ((leafNode)this.next).prior = n;
      }
      //leaf의 연결
      this.next = n;
      n.prior = this;
      
      //root = leaf의 경우 = parents node가 없음 
      // : 새로운 parents를 만들어야 함
      if(this.parents == null) {
         internalNode newP = new internalNode(key);
         newP.child[0] = this;
         newP.child[1] = n;
         this.parents = newP;
         n.parents = newP;
         Main.mg.setRoot(newP);
      }
      
      //parents가 존재하는 경우 parents에 key와 new node를 추가함
      else {
         ((internalNode)this.parents).addKey(key, n);
      }
   }
   
   void deleteData(String data, int position) {
      int i;
      // i = target Position
      for(i=0; i<this.getN(); i++) 
         //find target data
         if(data.compareTo(this.dataArr[i].getData(position)) == 0) 
            break;
      
      
      //데이터를 찾지 못하였을 때 종료
      if(i == this.getN()) {
         System.out.println("데이터를 찾지 못했습니다.");
         return;
      }
      
      //데이터를 찾았으므로 삭제했다 가정하고 n을 줄임,
      n--;
      
      //Root가 아니고 Leaf의 갯수가 M/2개 이하일 경우
      if(Main.mg.getRoot() != this && this.n<M/2) {
         //prior에서 빌려올 값이 있으면
         if(this.prior != null && this.parents == this.prior.parents && this.prior.n >M/2) {
            /*-------------copy-------------------*/
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent의 key중 node의 처음 값과 일치하는게 있다면 위치를 가지고 온다.
            for(int j=0; j<par.n; j++) 
               if(this.dataArr[0].getData(position).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            //마지막까지 없다면 0번째 child
            if(parKeyPosition == -1) parKeyPosition = 0;
            /*------------------------------------*/
            //빌려올 데이터를 저장, 기준 필드의 key값을 저장
            data temp = ((leafNode)this.prior).dataArr[this.prior.n-1];
            
            //데이터를 채우기 위하여 오른쪽으로 밀고 데이터를 삽입
            for(int j=0; j<i; j++) 
               this.dataArr[i-j] = this.dataArr[i-j-1];
            this.dataArr[0] = temp;
            this.n++;
            
            //부모 키의 변경
            par.key[parKeyPosition] = this.dataArr[0].getData(position);
            
            //빌려준 데이터 삭제 
            ((leafNode)this.prior).dataArr[this.prior.n-1] = null;
            this.prior.n--;
         }
         
         //next에서 빌려올 값이 있으면
         else if(this.next != null && this.parents == this.next.parents && this.next.n > M/2) {
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent의 key중 next의 처음 값과 일치하는게 있다면 위치를 가지고 온다.
            for(int j=0; j<par.n; j++) {
               if(((leafNode)this.next).dataArr[0].getData(Main.mg.getSortField()).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            }
            
            //마지막까지 못찾으면 0번째 child
            if(parKeyPosition == -1) parKeyPosition = 0;
            //빌려올 데이터를 저장, 기준 필드의 key값을 저장
            data temp = ((leafNode)this.next).dataArr[0];
            
            //데이터를 채우기 위하여 왼쪽으로 밀고 데이터를 삽입
            for(int j=0; j<n-i; j++) 
               this.dataArr[i+j] = this.dataArr[i+j+1];
            this.dataArr[n] = temp;
            this.n++;
            
            //빌려준 노드에서는 덮어쓰기로 빌려준 데이터 처리 후 마지막 삭제
            for(int j=0; j<next.n-1; j++) 
               ((leafNode)this.next).dataArr[j] = ((leafNode)this.next).dataArr[j+1];
            ((leafNode)this.next).dataArr[next.n-1] = null;
            this.next.n--;
            
            //부모 키의 변경
            par.key[parKeyPosition] = ((leafNode)this.next).dataArr[0].getData(position);
            //맨 왼쪽 노드에서 작동할 경우 key는 한번만 바꿔도 된다.
            if(parKeyPosition != 0) par.key[parKeyPosition-1] = this.dataArr[0].getData(position);
         }
      
         //빌려올 수 없어서 merge 해야할 때
         //prior와 merge(왼쪽으로 merge)
         else if(this.prior != null && this.prior.parents == this.parents) {   
            //System.out.println("left merge start");
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent의 key중 node의 처음 값과 일치하는게 있다면 위치를 가지고 온다.
            for(int j=0; j<par.n; j++) 
               if(this.dataArr[0].getData(position).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            //마지막까지 없다면 0번째 child
            if(parKeyPosition == -1) parKeyPosition = 0;
            
            //부모의 key를 삭제, 삭제한 공간을 한칸씩 왼쪽으로 당겨 매꿈
            for(int j=0; j<par.n-parKeyPosition-1; j++)  
               par.key[j+parKeyPosition] = par.key[j+parKeyPosition+1];
            
            
            //갯수를 줄인다.
            par.n--;
            par.key[par.n] = null;
            
            //삭제하려고 하는 데이터에 덮어씌워 없애버림
            for(int j=0; j<this.n-i; j++) 
               this.dataArr[i+j] = this.dataArr[i+j+1];
            this.dataArr[n] = null;
                        
            //prior에 복사, leaf를 연결
            for(int j=0; j<this.n; j++) {
               ((leafNode)this.prior).dataArr[this.prior.n+j] = this.dataArr[j];
               this.prior.n++;
            }
            this.parents = null;
               
            ((leafNode)this.prior).next = this.next;
            if(this.next != null) ((leafNode)this.next).prior = this.prior;
            
            //부모의 child 관리
            for(int j=0; j<par.n-parKeyPosition;j++)
               par.child[parKeyPosition+j+1] =par.child[parKeyPosition+j+2]; 
            par.child[par.n+1] = null;
            
            //par가 root가 아닌데 조건을 만족하지 않는 경우
            if(par != Main.mg.getRoot() && par.n < Main.mg.M/2 ) {
               //System.out.println("Parents N is too less");
               par.rebuilding();
            }
            //par가 root이고 갯수가 0일 때
            else if(par == Main.mg.getRoot() && par.n == 0) {
               this.prior.parents = null;
               Main.mg.setRoot(this.prior);
            }
         }
      
         //next와 merge(오른쪽 merge), 왼쪽 끝에서만 일어남
         else if(this.next != null && this.next.parents == this.parents) {
            //System.out.println("right merge start");
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent의 key중 node의 처음 값과 일치하는게 있다면 위치를 가지고 온다.
            for(int j=0; j<par.n; j++) 
               if(this.dataArr[0].getData(position).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            //마지막까지 없다면 0번째 child
            if(parKeyPosition == -1) parKeyPosition = 0;
            
            //부모의 key를 삭제, 삭제한 공간을 한칸씩 왼쪽으로 당겨 매꿈
            for(int j=0; j<par.n-parKeyPosition-1; j++)  
               par.key[j+parKeyPosition] = par.key[j+parKeyPosition+1];
            
            //갯수를 줄인다.
            par.n--;
            par.key[par.n] = null;         
            
            //삭제하려고 하는 데이터에 덮어씌워 없애버림
            for(int j=0; j<this.n-i; j++) 
               this.dataArr[i+j] = this.dataArr[i+j+1];
            
            this.dataArr[n] = null;
                        
            //merge노드가 들어올 자리를 마련함
            for(int j=next.n; j>0; j--) 
               ((leafNode)this.next).dataArr[j+this.n-1] = ((leafNode)this.next).dataArr[j-1];
            
            
            //next에 복사ㅣ
            for(int j=0; j<this.n; j++) {
               ((leafNode)this.next).dataArr[j] = this.dataArr[j];
               this.next.n++;
            }
            
            this.parents = null;
            
            //연결
            ((leafNode)this.next).prior = this.prior;
            if(this.prior != null) ((leafNode)this.prior).next = this.next;
            
            //부모의 child 관리
            for(int j=0; j<par.n+1;j++) 
               par.child[parKeyPosition+j] =par.child[parKeyPosition+j+1];
            
            par.child[par.n+1] = null;
            
            //par가 root가 아닌데 조건을 만족하지 않는 경우
            if(par != Main.mg.getRoot() && par.n < Main.mg.M/2 ) {
               //System.out.println("Parents N is too less");
               par.rebuilding();
            }
            //par가 root이고 n이 0일때
            else if(par == Main.mg.getRoot() && par.n == 0) {
               this.next.parents = null;
               Main.mg.setRoot(this.next);
            }
         }
      }
      
      //root가 leaf거나, Leaf의 갯수가 M/2개 이상이라서 데이터 삭제만 이루어질 경우
      else {
         //데이터는 밑에서 덮어쓰기로 처리 후 맨 마지막 삭제
         for(int j=0; j<n-i; j++) 
            this.dataArr[i+j] = dataArr[i+j+1];
         this.dataArr[n] = null;
         
         //부모가 있고, target이 0번째였을 경우 부모의 key를 바꿔야함
         if(this.parents != null && i==0) {
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //맨 왼쪽 노드가 아닐경우 부모키를 바꿔줘야 한다.
            if(par.child[0] != this) {
               //parent의 key중 target과 일치하는게 있다면 위치를 가지고 온다.
               for(int j=0; j<par.n; j++) 
                  if(data.compareTo(par.key[j]) == 0) {
                     parKeyPosition = j;
                     break;
                  }
               
               if(parKeyPosition != -1) par.key[parKeyPosition] = this.dataArr[0].getData(position);
               else System.out.println("부모키변경작업에러");
            }
         }
         //System.out.println("삭제완료");
      }
   }
}