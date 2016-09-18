public class Graph {  
    private int length;  
    private GraphList[] list;  
    private int[][]     weight;  
      
    public Graph(int length) {  
        this.length = length;  
        list = new  GraphList[length];  
        weight = new int[length][length];  
    }  
  
      
    public void   dfs(int v) {  
        int[] ajd = new int[length];  
        int ajdlength = list[v].getAjd(ajd);  
        list[v].visitable = true;  
        System.out.print(v + " ");  
        for (int i = 0; i < ajdlength; i++) {  
            int w = ajd[i];  
            if (!list[w].visitable) {  
                dfs(w);  
            }  
        }  
    }  
  //����ƽ����
     public double GetAverDu()
     {
    	 int Alldu=0;
    	  for (int i = 0; i < length; i++) {  
    		  int[] ajd = new int[length];  
              int ajdlength = list[i].getAjd(ajd);   
              Alldu+=ajdlength;
        }  
    	
    	 return Alldu/length;
     }
     //�������ϵ��
     public double GetJuleixishu()
     {
    	 int ni=0;
    	 int li=0;
    	 double sum=0;
    	  for (int i = 0; i < length; i++)
    	  {  
    		  int[] ajd = new int[length];  
              int ajdlength = list[i].getAjd(ajd);   
               ni=ajdlength;//�ڵ�i���ڽӽڵ���
               li=0;
               
               for(int m=0;m<ajdlength-1;m++)//��Щ�ڽӽڵ��ʵ��������
               {
            	   for(int n=m+1;n<ajdlength;n++)
            	   {
            		   if(IsConnect(ajd[m],ajd[n]))
            			   li++;
            	   }
            	   
               }          
               if(ni>1)          
               sum+=2*li/(ni*(ni-1));
        }  
    	
    	 return sum;
     }
      
     boolean IsConnect(int v1,int v2)//�Д��ɂ����c�Ƿ����B
     {
    	boolean rs = false;
    
    	 for (int i = 0; i < length; i++) 
    	 {  
             if (list[i] != null)
             {  
                if(list[i].first.info == v1)
                { 
                	 int[] ajd = new int[length];  
                     int ajdlength = list[i].getAjd(ajd);    
                     
                     for(int j=0;j<ajdlength;j++)
                     {
                    	 if(v2 == ajd[j])
                    	 {
                    		 rs = true;
                    		 return rs;
                    	 }
                    	 
                    	 
                     }
                 }
         }
        }
             for (int i = 0; i < length; i++) 
        	 {  
                 if (list[i] != null)
                 {  
                    if(list[i].first.info == v2)
                    { 
                    	 int[] ajd = new int[length];  
                         int ajdlength = list[i].getAjd(ajd);    
                         
                         for(int j=0;j<ajdlength;j++)
                         {
                        	 if(v1 == ajd[j])
                        	 {
                        		 rs = true;
                        		 return rs;
                        	 }
                        	 
                        	 
                         }
                     }
             }
          }
    	 return rs;
     }
    // ������ȱ���  
    public void dfsTravel() {  
        for (int i = 0; i < length; i++) {  
            list[i].visitable = false;  
        }  
        for (int i = 0; i < length; i++) {  
            if (!list[i].visitable) {  
                dfs(i);  
            }  
        }  
    }  
  
    // ������ȱ���  
    public void bfsTravel() {  
        for (int i = 0; i < length; i++) {  
            list[i].visitable = false;  
        }  
        bfs();  
    }  
  
    private void bfs() {  
        Queue   queue =  new Queue();  
        for (int index = 0; index < length; index++) {  
            if (!list[index].visitable) {  
                queue.addQueue(index);  
                list[index].visitable = true;  
                System.out.print(index + " ");  
                while (!queue.isEmpty()) {  
                    int temp = queue.front();  
                    queue.deleteQueue();  
                    int[] ajd = new int[length];  
                    int ajdlength = list[temp].getAjd(ajd);  
                    for (int i = 0; i < ajdlength; i++) {  
                        int w = ajd[i];  
                        if (!list[w].visitable) {  
                            System.out.print(w + " ");  
                            queue.addQueue(w);  
                            list[w].visitable = true;  
                        }  
                    }  
                }  
            }  
          }  
    }  
  
    // ���·��  
    private int[] shortPath(int v) {  
        int[] shortPath = new int[length];  
        boolean[]   weightFound = new boolean[length];  
        for (int i = 0; i < length; i++) {  
                                                // ��������  
            shortPath[i] =   9999;  
            weightFound[i] = false;  
        }  
              
        shortPath[v] = 0;  
        weightFound[v] = true;  
        Queue queue = new Queue();  
        queue.addQueue(v);  
          
        while (!queue.isEmpty()) {  
            int temp = queue.front();  
            queue.deleteQueue();  
            int[]  ajd = new int[length];  
            int ajdlength =   list[temp].getAjd(ajd);  
              
              
            for (int i = 0; i < ajdlength; i++) {  
                int w = ajd[i];  
                if (!weightFound[w]) {  
                    if (shortPath[w] > shortPath[temp] + weight[temp][w]){  
                        shortPath[w] = shortPath[temp] + weight[temp][w];  
                    }  
                }  
            }  
              
            int minWeightNode = 0;    
            for (int i = 0; i < length; i++) {  
                if (!weightFound[i]) {  
                    minWeightNode = i;  
                    for (int j = 0; j < length; j++) {  
                        if (!weightFound[j]) {  
                            if (shortPath[j] < shortPath[minWeightNode]) {  
                                minWeightNode = j;  
                            }  
                        }  
                    }  
                    break;  
                }  
            }  
  
            if (!weightFound[minWeightNode]) {  
                weightFound[minWeightNode] = true;  
                queue.addQueue(minWeightNode);  
            }  
        }  
                  return shortPath;  
    }  
      
    // ����  
    public int  length() {  
        System.out.println("length="+length);  
        return length;  
    }  
      
      
    public boolean isEmpty() {  
        return  length == 0;  
    }  
  
      
    // ��ӽڵ�  
    public void addGraph(int info) {  
        for (int i = 0; i < length; i++) {  
            if (list[i] == null) {  
                GraphList g = new GraphList();  
                g.addNode(info);  
                list[i] = g;  
                break;  
            }  
        }  
    }  
    // ��ӱ�  
   public boolean addEdge(int vfrom, int vto, int value) { 
    	
    	int jIndex=-1;
    	int i=0;
    	int vindex = 0;
    	for (i = 0; i < length; i++) {  
            if (list[i] != null) {  
               if(list[i].first.info == vfrom)
               {  
                   int[] ajd = new int[length];  //����Ƿ���ӹ���
                   int ajdlength = list[i].getAjd(ajd);  
                   for(int w=0;w<ajdlength;w++)
                   {
                	   int temp = ajd[w]; 
                	   if(temp == vto)//��ʾ��ӹ���
                		   return  false;
                   }
            	   
            	   list[i].addNode(vto);  
            	   vindex = i;
            	   break;              	   
               }
               if(list[i].first.info == vto)
               {  
            	   jIndex=i; 
           	   
               }
            }  
        } 
    	if(jIndex!=-1)
    	{
            weight[vindex][jIndex] = value;  
    	}
    	
    	for (i = vindex; i < length; i++) {  
            if (list[i] != null) {  
               if(list[i].first.info == vto)
               {  
            	   weight[vindex][i] = value;   
            	   break;              	   
               }               
            }  
        } 
    	return true;

    }  
  
      
    // ��ӱ�  
//    public void addEdge(int vfrom, int vto, int value) {  
//        list[vfrom].addNode(vto);  
//        weight[vfrom][vto] = value;  
//    }  
//  
    // ��ӡͼ  
    public void print() {  
        for (int i = 0; i < length; i++) {  
            GraphNode current =  list[i].first;  
            while (current != null) {  
                System.out.print(current.info + " ");  
                current = current.link;  
            }  
               System.out.println("");  
        }  
    } 
}

class GraphList {  
    public GraphNode first;  
    public GraphNode last;  
    public boolean   visitable;  
    public int getAjd(int[] ajd) {  
        GraphNode current = first;  
        int length = 0;  
        while(current != null) {  
            ajd[length++] = current.info;  
            current = current.link;  
        }  
        return length;  
    }  
    public void addNode(int v) {  
        GraphNode node = new GraphNode();  
        node.info = v;  
        if(first == null) {  
            first = node;  
            last = node;  
        } else {  
            last.link = node;  
            last = node;  
        }  
    }  
}  
class GraphNode {  
    public GraphNode   link;  
    public int   info;  
}     