import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import com.filetool.util.*;

public class author_peernet extends readmysql{
	public static HashMap<Integer,java.util.Date> author_early=new HashMap<Integer,java.util.Date>();
	
	//public static HashMap<Integer,HashMap<Integer,Set<Integer>>> author_peer_project=new HashMap<Integer,HashMap<Integer,Set<Integer>>>();
	public static HashMap<Integer,Float> author_avarage_clusting=new HashMap<Integer,Float>();
	public static HashMap<Integer, Float> author_average_dushu = new HashMap<Integer, Float>();
	public static HashMap<Integer, Float> author_average_jieshu = new HashMap<Integer, Float>();
	public static HashMap<Integer, Float> author_average_jiejin = new HashMap<Integer, Float>();
	public static UndirectedSparseGraph<String, String> MyGraph = new UndirectedSparseGraph<String, String>();
	public static Set<Integer> peer= new HashSet<Integer>();
	public static Integer ADD_featrue_author= new Integer(0);
	
	public static double jieshusum = 0;
	public static double jiejinsum = 0;
	public static double dejinsum = 0;
	public static double juleisum = 0;
	public static double[][] result = new  double[10000][5];
	
	public Integer counttotal=0;


	public static void count_author_commits(readmysql total) throws Exception{
		author_peer=Read_Int_Set_File("./db/author_peer.txt");
		//author_mainproj=Read_Int_Int_File("E:\\data\\seco_data_new\\last_experiment\\author_mainproj.txt");
		peer=author_peer.get(ADD_featrue_author);
		
		System.out.println("author_peer角色同行"+author_peer.size());
		read_author_early("./db/author_early.txt",author_early);
		
		read("mr_scmlog",total);
		System.out.println("author_peer_project："+author_peer_project.size());
		
		for(Integer author:author_peer_project.keySet()){
			for(Integer peerstart:author_peer_project.get(author).keySet()){
				for(Integer peerend:author_peer_project.get(author).keySet()){
					if(have_same_element(author_peer_project.get(author).get(peerstart), author_peer_project.get(author).get(peerend))){
						//put_peerlink(author, peerstart, peerend);
						put_MyGraph(author, peerstart, peerend);// MyGraph
					}
				}
			}
		}
		System.out.println("author_peerlink同行网络"+author_peerlink.size());
		
		for(Integer author:author_peerlink.keySet()){
			float totalclusting=0;
			
			for(Integer peer:author_peerlink.get(author).keySet()){
				float re=count_clusting(author,peer);
				totalclusting+=re;
			}
			float num=author_peerlink.get(author).size();
		
			float result=totalclusting/num;
			author_avarage_clusting.put(author, result);
		}
		System.out.println("author_avarage_clusting聚类系数："+author_avarage_clusting.size());
		
		
		//3 metric
		// myGraph
		
		
		DegreeScorer ds = new DegreeScorer(MyGraph);//度中心型

		BetweennessCentrality ranker = new BetweennessCentrality(MyGraph);// 计算介数中心性
		ranker.setRemoveRankScoresOnFinalize(false);
		ranker.evaluate();
		ClosenessCentrality cc = new ClosenessCentrality(MyGraph);// 计算接近中心性
	
		Collection<String> VCC = MyGraph.getVertices();
		

		Iterator<String> ii = VCC.iterator();

		int num = MyGraph.getVertexCount();

		int cnt = 0;
		while (ii.hasNext()) {
			cnt++;
			double bb = 0;
			String vv = ii.next();
			for(Integer one_peer:peer){
				Integer vvint=Integer.parseInt(vv);
				if(vvint.equals(one_peer)){
					bb = ds.getVertexScore(vv);
					dejinsum += bb;
					bb = ranker.getVertexRankScore(vv);
					jieshusum += bb;
					bb = cc.getVertexScore(vv);
					jiejinsum += bb;
				}
			}
			dejinsum=dejinsum;
			jieshusum=jieshusum;
			jiejinsum=jiejinsum;
		/*	result[cnt][0] = (double)cnt;
			result[cnt][1] = Double.parseDouble(vv);
			result[cnt][2] = dejinsum;
			result[cnt][3] = jieshusum;
			result[cnt][4] = jiejinsum;*/
			
			/*bb = ds.getVertexScore(vv);
			dejinsum += bb;
			result[cnt][0] = (double)cnt;
			result[cnt][1] = Double.parseDouble(vv);
			result[cnt][2] = bb;
			System.out.println("节点" + vv + "度中心性:" + bb);

			bb = ranker.getVertexRankScore(vv);
			jieshusum += bb;
			result[cnt][3] = bb;
			System.out.println("节点" + vv + "介数中心性:" + bb);
			bb = cc.getVertexScore(vv);
			jiejinsum += bb;
			result[cnt][4] = bb;
			System.out.println("节点" + vv + "接近中心性:" + bb);*/
		}
	//	FileUtil.write("./db/result.txt", result, true);
	}
	
	public static float count_clusting(Integer author,Integer peer){
		Set<Integer> peers=new HashSet<Integer>();
/*		for(Integer temp:author_peerlink.get(author).get(peer)){
			peers.add(temp);
		}//每锟斤拷同锟叫的固讹拷锟斤拷锟斤拷同锟斤拷
		Float result=(float) 0;
		for(Integer peer_target:peers){
			Set<Integer> temp=new HashSet<Integer>();
			temp.addAll(author_peerlink.get(author).get(peer_target));
			temp.retainAll(peers);
			result+=temp.size();
		}*/
		Float result=(float) 0;
		for(Integer temp:author_peerlink.get(author).keySet()){
			result+=author_peerlink.get(author).get(temp).size();
			result-=1;
		}
		float size=author_peerlink.get(author).size();
		float re=result/(float)((size-1)*(size));
		if(size==1){
			return 0;
		}else{
			return re;
		}
		
	}
	public static void put_peerlink(Integer author,Integer peerstart,Integer peerend){
		
		if(author_peerlink.containsKey(author)){
			if(author_peerlink.get(author).containsKey(peerstart)){
				author_peerlink.get(author).get(peerstart).add(peerend);
			}else{
				Set<Integer> a=new HashSet<Integer>();
				a.add(peerend);
				author_peerlink.get(author).put(peerstart, a);
			}
		}else{
			Set<Integer> a=new HashSet<Integer>();
			a.add(peerend);
			HashMap<Integer,Set<Integer>> b=new HashMap<Integer,Set<Integer>>();
			b.put(peerstart, a);
			author_peerlink.put(author, b);
			
		}
		
	}
	public static void put_MyGraph(Integer author, Integer peerstart, Integer peerend) {
	
				String str = String.valueOf(peerstart) + "-" + String.valueOf(peerend);
				MyGraph.addVertex(String.valueOf(peerstart));
				MyGraph.addVertex(String.valueOf(peerend));
				String edge = MyGraph.findEdge(String.valueOf(peerstart), String.valueOf(peerend));
				if (edge == null) {
					MyGraph.addEdge(str,
							String.valueOf(peerstart),
							String.valueOf(peerend));
				}				
			
	}
	
	public static boolean have_same_element(Set<Integer> a,Set<Integer> b){
		Set<Integer> result=new HashSet<Integer>();
		result.addAll(a);
		result.retainAll(b);
		if(result.size()>0){
			return true;
		}
		else{
			return false;
		}
	}
	public  void calculate(Integer commiter_id,Integer merged_id,Date date,Integer projid,Integer density ){
		Integer commiter=commiter_id;
		Integer fixer=merged_id;
		
		
			java.util.Date start=(java.util.Date) author_early.get(ADD_featrue_author);
			java.util.Date end=new Date(1970,1,1);
			Calendar ca = Calendar.getInstance(); 
			ca.setTime(start);
			ca.add(Calendar.YEAR, (int) time);//
			end=ca.getTime();
			if(date.getTime()<=start.getTime()){
				//if(author_peer.get(author).contains(fixer)){
				put_into_author_peer_projs(ADD_featrue_author,fixer,projid);
				//}
			}
		
		counttotal++;
	//	System.out.println(counttotal);
	


	}
	public static void put_into_author_peer_projs(Integer author,Integer fixer,Integer proj){
		if(author_peer_project.containsKey(author)){
			if(author_peer_project.get(author).containsKey(fixer)){
				author_peer_project.get(author).get(fixer).add(proj);
			}else{
				Set<Integer> a=new HashSet<Integer>();
				a.add(proj);
				author_peer_project.get(author).put(fixer, a);
			}
		}else{
			Set<Integer> b=new HashSet<Integer>();
			b.add(proj);
			HashMap<Integer,Set<Integer>> a=new HashMap<Integer,Set<Integer>>();
			a.put(fixer, b);
			author_peer_project.put(author, a);
		}
	}
}
