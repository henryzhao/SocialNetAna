import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;


public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		readmysql rm = new readmysql();
//		String filepath = "E:\\Users\\dell\\workspace\\SocialNetAna\\db\\author_early.txt";
//		HashMap<Integer, Date> author_early = new HashMap<Integer, Date>();
//		try {
//			readmysql.read_author_early(filepath, author_early);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (int i = 0; i < author_early.size(); i++) {
//			System.out.println( i + " " + author_early.get(i));
//		}
//		String adbString = author_early.get(4333).toString();
//		int linenum = author_early.size();
//		System.out.println( linenum +adbString);
		String filepath = "./db/test_developer.txt";
		readmysql rm = new readmysql();
		Set<Integer> authors=new HashSet<Integer>();
		authors=rm.Read_Set_File(filepath);
		
		
		writeresult wr= new writeresult();
		try {
			for(Integer author:authors){
				
				author_peernet apn=new author_peernet();
				apn.dejinsum=0;
				apn.jieshusum=0;
			    apn.jiejinsum=0;
				apn.time=1;	
				apn.ADD_featrue_author=author;
				apn.MyGraph = new UndirectedSparseGraph<String, String>();
				apn.peer=new HashSet<Integer>();
				apn.count_author_commits(apn);
				apn.dejinsum=apn.dejinsum/apn.peer.size();
				apn.jieshusum=apn.jieshusum/apn.peer.size();
			    apn.jiejinsum=apn.jiejinsum/apn.peer.size();
				if(apn.peer.size()==1){
					apn.jiejinsum=0;   
				}
				String temp= "";
				temp=temp+apn.ADD_featrue_author+" "+(float)apn.dejinsum+" "+(float)apn.jieshusum+" "+(float)apn.jiejinsum;
			//	temp=temp+apn.MyGraph;
				wr.write_file("./db/result.txt", temp);
				
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
