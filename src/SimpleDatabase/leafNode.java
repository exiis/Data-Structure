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
      /* �����Ͱ� �ϳ��� ���� ��� */
      if(dataArr[0] == null) {
         /* �Է��� �������� head�� 0��° ��ҷ� �߰�, N���� */
         dataArr[0] = data;
         n++;
      }
      
      /* �����Ͱ� �ϳ��� ���� ��� */
      else {
         int i;
         
         /* ���� ���� �ʵ� ��ġ ������ͼ� �� */
         int position = Main.mg.sortField;
         String inputData = data.getData(position);
         
         /* ���� ������ ����ŭ �ݺ� */
         for(i=n; i>0; i--) {
            //���ο� �����Ͱ� �� ��󺸴� ũ�� �����ʿ� ����
            if(inputData.compareTo(dataArr[i-1].getData(position)) > 0) {
               dataArr[i] = data;
               break;
            }
            //������ ���� �����͸� ���������� �о����
            else 
               dataArr[i] = dataArr[i-1];
         }
         
         //���������� ������ �ȵ� ���
         if(i==0)  {
            dataArr[0] = data;
            Main.mg.curIndex = 0;
         }
         n++;

         /* Max Degree�� �����ߴ��� �˻� */
         if(n == M) {
            System.out.println("Leaf split");
            split(position);
         }
      }
   }
   
   void split(int position) {
      int middle = (int)M/2;
      //key�� �ø� �߾� �� ����
      String key = dataArr[middle].getData(position);
      leafNode n = new leafNode();
      
      //���ο� leaf�� recode�� ����
      for(int i=0; i<(int)Math.ceil(M/2.0); i++) {
         n.dataArr[i] = this.dataArr[i+middle];
         this.dataArr[i+middle] = null;
         n.n++;
         this.n--;
      }

      //������ node�� ����
      if(this.next != null) {
         n.next = this.next;
         ((leafNode)this.next).prior = n;
      }
      //leaf�� ����
      this.next = n;
      n.prior = this;
      
      //root = leaf�� ��� = parents node�� ���� 
      // : ���ο� parents�� ������ ��
      if(this.parents == null) {
         internalNode newP = new internalNode(key);
         newP.child[0] = this;
         newP.child[1] = n;
         this.parents = newP;
         n.parents = newP;
         Main.mg.setRoot(newP);
      }
      
      //parents�� �����ϴ� ��� parents�� key�� new node�� �߰���
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
      
      
      //�����͸� ã�� ���Ͽ��� �� ����
      if(i == this.getN()) {
         System.out.println("�����͸� ã�� ���߽��ϴ�.");
         return;
      }
      
      //�����͸� ã�����Ƿ� �����ߴ� �����ϰ� n�� ����,
      n--;
      
      //Root�� �ƴϰ� Leaf�� ������ M/2�� ������ ���
      if(Main.mg.getRoot() != this && this.n<M/2) {
         //prior���� ������ ���� ������
         if(this.prior != null && this.parents == this.prior.parents && this.prior.n >M/2) {
            /*-------------copy-------------------*/
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent�� key�� node�� ó�� ���� ��ġ�ϴ°� �ִٸ� ��ġ�� ������ �´�.
            for(int j=0; j<par.n; j++) 
               if(this.dataArr[0].getData(position).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            //���������� ���ٸ� 0��° child
            if(parKeyPosition == -1) parKeyPosition = 0;
            /*------------------------------------*/
            //������ �����͸� ����, ���� �ʵ��� key���� ����
            data temp = ((leafNode)this.prior).dataArr[this.prior.n-1];
            
            //�����͸� ä��� ���Ͽ� ���������� �а� �����͸� ����
            for(int j=0; j<i; j++) 
               this.dataArr[i-j] = this.dataArr[i-j-1];
            this.dataArr[0] = temp;
            this.n++;
            
            //�θ� Ű�� ����
            par.key[parKeyPosition] = this.dataArr[0].getData(position);
            
            //������ ������ ���� 
            ((leafNode)this.prior).dataArr[this.prior.n-1] = null;
            this.prior.n--;
         }
         
         //next���� ������ ���� ������
         else if(this.next != null && this.parents == this.next.parents && this.next.n > M/2) {
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent�� key�� next�� ó�� ���� ��ġ�ϴ°� �ִٸ� ��ġ�� ������ �´�.
            for(int j=0; j<par.n; j++) {
               if(((leafNode)this.next).dataArr[0].getData(Main.mg.getSortField()).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            }
            
            //���������� ��ã���� 0��° child
            if(parKeyPosition == -1) parKeyPosition = 0;
            //������ �����͸� ����, ���� �ʵ��� key���� ����
            data temp = ((leafNode)this.next).dataArr[0];
            
            //�����͸� ä��� ���Ͽ� �������� �а� �����͸� ����
            for(int j=0; j<n-i; j++) 
               this.dataArr[i+j] = this.dataArr[i+j+1];
            this.dataArr[n] = temp;
            this.n++;
            
            //������ ��忡���� ������ ������ ������ ó�� �� ������ ����
            for(int j=0; j<next.n-1; j++) 
               ((leafNode)this.next).dataArr[j] = ((leafNode)this.next).dataArr[j+1];
            ((leafNode)this.next).dataArr[next.n-1] = null;
            this.next.n--;
            
            //�θ� Ű�� ����
            par.key[parKeyPosition] = ((leafNode)this.next).dataArr[0].getData(position);
            //�� ���� ��忡�� �۵��� ��� key�� �ѹ��� �ٲ㵵 �ȴ�.
            if(parKeyPosition != 0) par.key[parKeyPosition-1] = this.dataArr[0].getData(position);
         }
      
         //������ �� ��� merge �ؾ��� ��
         //prior�� merge(�������� merge)
         else if(this.prior != null && this.prior.parents == this.parents) {   
            //System.out.println("left merge start");
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent�� key�� node�� ó�� ���� ��ġ�ϴ°� �ִٸ� ��ġ�� ������ �´�.
            for(int j=0; j<par.n; j++) 
               if(this.dataArr[0].getData(position).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            //���������� ���ٸ� 0��° child
            if(parKeyPosition == -1) parKeyPosition = 0;
            
            //�θ��� key�� ����, ������ ������ ��ĭ�� �������� ��� �Ų�
            for(int j=0; j<par.n-parKeyPosition-1; j++)  
               par.key[j+parKeyPosition] = par.key[j+parKeyPosition+1];
            
            
            //������ ���δ�.
            par.n--;
            par.key[par.n] = null;
            
            //�����Ϸ��� �ϴ� �����Ϳ� ����� ���ֹ���
            for(int j=0; j<this.n-i; j++) 
               this.dataArr[i+j] = this.dataArr[i+j+1];
            this.dataArr[n] = null;
                        
            //prior�� ����, leaf�� ����
            for(int j=0; j<this.n; j++) {
               ((leafNode)this.prior).dataArr[this.prior.n+j] = this.dataArr[j];
               this.prior.n++;
            }
            this.parents = null;
               
            ((leafNode)this.prior).next = this.next;
            if(this.next != null) ((leafNode)this.next).prior = this.prior;
            
            //�θ��� child ����
            for(int j=0; j<par.n-parKeyPosition;j++)
               par.child[parKeyPosition+j+1] =par.child[parKeyPosition+j+2]; 
            par.child[par.n+1] = null;
            
            //par�� root�� �ƴѵ� ������ �������� �ʴ� ���
            if(par != Main.mg.getRoot() && par.n < Main.mg.M/2 ) {
               //System.out.println("Parents N is too less");
               par.rebuilding();
            }
            //par�� root�̰� ������ 0�� ��
            else if(par == Main.mg.getRoot() && par.n == 0) {
               this.prior.parents = null;
               Main.mg.setRoot(this.prior);
            }
         }
      
         //next�� merge(������ merge), ���� �������� �Ͼ
         else if(this.next != null && this.next.parents == this.parents) {
            //System.out.println("right merge start");
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //parent�� key�� node�� ó�� ���� ��ġ�ϴ°� �ִٸ� ��ġ�� ������ �´�.
            for(int j=0; j<par.n; j++) 
               if(this.dataArr[0].getData(position).compareTo(par.key[j]) == 0) {
                  parKeyPosition = j;
                  break;
               }
            //���������� ���ٸ� 0��° child
            if(parKeyPosition == -1) parKeyPosition = 0;
            
            //�θ��� key�� ����, ������ ������ ��ĭ�� �������� ��� �Ų�
            for(int j=0; j<par.n-parKeyPosition-1; j++)  
               par.key[j+parKeyPosition] = par.key[j+parKeyPosition+1];
            
            //������ ���δ�.
            par.n--;
            par.key[par.n] = null;         
            
            //�����Ϸ��� �ϴ� �����Ϳ� ����� ���ֹ���
            for(int j=0; j<this.n-i; j++) 
               this.dataArr[i+j] = this.dataArr[i+j+1];
            
            this.dataArr[n] = null;
                        
            //merge��尡 ���� �ڸ��� ������
            for(int j=next.n; j>0; j--) 
               ((leafNode)this.next).dataArr[j+this.n-1] = ((leafNode)this.next).dataArr[j-1];
            
            
            //next�� �����
            for(int j=0; j<this.n; j++) {
               ((leafNode)this.next).dataArr[j] = this.dataArr[j];
               this.next.n++;
            }
            
            this.parents = null;
            
            //����
            ((leafNode)this.next).prior = this.prior;
            if(this.prior != null) ((leafNode)this.prior).next = this.next;
            
            //�θ��� child ����
            for(int j=0; j<par.n+1;j++) 
               par.child[parKeyPosition+j] =par.child[parKeyPosition+j+1];
            
            par.child[par.n+1] = null;
            
            //par�� root�� �ƴѵ� ������ �������� �ʴ� ���
            if(par != Main.mg.getRoot() && par.n < Main.mg.M/2 ) {
               //System.out.println("Parents N is too less");
               par.rebuilding();
            }
            //par�� root�̰� n�� 0�϶�
            else if(par == Main.mg.getRoot() && par.n == 0) {
               this.next.parents = null;
               Main.mg.setRoot(this.next);
            }
         }
      }
      
      //root�� leaf�ų�, Leaf�� ������ M/2�� �̻��̶� ������ ������ �̷���� ���
      else {
         //�����ʹ� �ؿ��� ������ ó�� �� �� ������ ����
         for(int j=0; j<n-i; j++) 
            this.dataArr[i+j] = dataArr[i+j+1];
         this.dataArr[n] = null;
         
         //�θ� �ְ�, target�� 0��°���� ��� �θ��� key�� �ٲ����
         if(this.parents != null && i==0) {
            //parent node call
            internalNode par = (internalNode)this.parents;
            int parKeyPosition=-1;
            
            //�� ���� ��尡 �ƴҰ�� �θ�Ű�� �ٲ���� �Ѵ�.
            if(par.child[0] != this) {
               //parent�� key�� target�� ��ġ�ϴ°� �ִٸ� ��ġ�� ������ �´�.
               for(int j=0; j<par.n; j++) 
                  if(data.compareTo(par.key[j]) == 0) {
                     parKeyPosition = j;
                     break;
                  }
               
               if(parKeyPosition != -1) par.key[parKeyPosition] = this.dataArr[0].getData(position);
               else System.out.println("�θ�Ű�����۾�����");
            }
         }
         //System.out.println("�����Ϸ�");
      }
   }
}