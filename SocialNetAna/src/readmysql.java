
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class readmysql {
	public static HashMap<Integer, Set<Integer>> author_peer = new HashMap<Integer, Set<Integer>>();
	public static HashMap<Integer, Set<Integer>> author_early_all = new HashMap<Integer, Set<Integer>>();//开发者进入前的全网络
	public static HashMap<Integer, Integer> author_mainproj = new HashMap<Integer, Integer>();
	public static HashMap<Integer, HashMap<Integer, Set<Integer>>> author_peerlink = new HashMap<Integer, HashMap<Integer, Set<Integer>>>();
	public static HashMap<Integer, HashMap<Integer, Set<Integer>>> author_peer_project = new HashMap<Integer, HashMap<Integer, Set<Integer>>>();

	public static Set<Integer> target = new HashSet<Integer>();
	public static long time = 0;// 延迟时间.比如说1年的数据

	public static void read(String tablename, readmysql rsql)
			throws ClassNotFoundException, Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/gnome", "root", "");
		System.out.print("连接成功");
		Statement stmt = conn.createStatement();
		String query = "select * from " + tablename + " limit 0,200000";
		ResultSet rs = stmt.executeQuery(query);
		rs.last();
		int count = 0;// 计算读取数据大小
		while (rs.getRow() != 0) {
			rs.beforeFirst();
			while (rs.next()) {
				Integer commiter_id = rs.getInt(1);
				Integer merged_id = rs.getInt(2);
				Date date = rs.getDate(3);
				Integer projid = rs.getInt(4);
				Integer density = rs.getInt(5);
				rsql.calculate(commiter_id, merged_id, date, projid, density);
			}
			count++;
			System.out.println("到第" + count * 200000);
			rs.close();
			query = "select * from " + tablename + " limit " + count * 200000
					+ ",200000";
			rs = stmt.executeQuery(query);
			rs.last();
		}
	}

	public static HashMap<Integer, Set<Integer>> Read_Int_Set_File(
			String filepath) {
		HashMap<Integer, Set<Integer>> author_peer = new HashMap<Integer, Set<Integer>>();
		File file = new File(filepath);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] temp = splitString(tempString);
				Set<Integer> a = new HashSet<Integer>();
				String[] projs;
				if (temp[1].contains(",")) {
					projs = temp[1].split(",");
				} else {
					projs = new String[1];
					projs[0] = temp[1];
				}

				Integer author = Integer.parseInt(temp[0].trim());
				author_peer.put(author, new HashSet<Integer>());
				for (int i = 0; i < projs.length; i++) {
					//System.out.println(projs[0]);
					Integer z = Integer.parseInt(projs[i].trim());
					author_peer.get(author).add(z);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return author_peer;
	}

	public static String[] splitString(String temp) {
		int k = temp.indexOf("[");
		String[] re = new String[2];
		re[0] = temp.substring(0, k);
		re[1] = temp.substring(k + 1, temp.length() - 1);
		return re;
	}

	public static HashMap<Integer, Integer> Read_Int_Int_File(String filepath) {
		HashMap<Integer, Integer> a = new HashMap<Integer, Integer>();
		File file = new File(filepath);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] temp = tempString.split(" ");
				Integer author = Integer.parseInt(temp[0]);
				Integer num = Integer.parseInt(temp[1]);
				a.put(author, num);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return a;
	}

	public static HashMap<Integer, Float> Read_Int_Float_File(String filepath) {
		HashMap<Integer, Float> a = new HashMap<Integer, Float>();
		File file = new File(filepath);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] temp = tempString.split(" ");
				Integer author = Integer.parseInt(temp[0]);
				Float num = Float.parseFloat(temp[1]);
				a.put(author, num);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return a;
	}
	public static Set<Integer> Read_Set_File(String filepath) {
		Set<Integer> result=new HashSet<Integer>();
		File file = new File(filepath);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				Integer author = Integer.parseInt(tempString);
				result.add(author);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return result;
	}
	public static void read_author_early(String filepath,
			HashMap<Integer, java.util.Date> author_early)
			throws ParseException {
		File file = new File(filepath);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				int k = tempString.indexOf(" ");
				String[] temp = new String[3];
				temp = tempString.split(" ");
				//temp[0] = tempString.substring(0, k).trim();
				Integer author = Integer.parseInt(temp[0]);
//				temp[1] = tempString.substring(k + 1, tempString.length())
//						.trim();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date time = sdf.parse(temp[2]);
				
				if (time == null) {
					System.out.println(tempString + " " + time);
				}
				author_early.put(author, time);
				line++;
				
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public void calculate(Integer commiter_id, Integer merged_id, Date date,
			Integer projid, Integer density) {
		System.out.println("no");
	}
}
