/**
 * @(#)SocialNetAnyFrame.java
 *
 * JFC SocialNetAny application
 *
 * @author 
 * @version 1.00 2016/3/28
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.*;

import cern.colt.matrix.doublealgo.Transform;
import edu.uci.ics.jung.algorithms.blockmodel.StructurallyEquivalent;
import edu.uci.ics.jung.algorithms.blockmodel.VertexPartition;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialNetAnyFrame extends JFrame implements ActionListener {

	/**
	 * The constructor
	 */

	JButton b = new JButton("读取节点数");
	JButton c = new JButton("生成开发者网络");
	JButton d = new JButton("计算开发者网络");
	JButton ex = new JButton("显示开发者网络 ");

	JTextField Txaverdu = new JTextField(60);// 度
	JTextField Txjuleixishu = new JTextField(60);// 聚类系数
	JTextField Txdingdiancnt = new JTextField(60);// 顶点数
	JTextField Txbianshu = new JTextField(60);// 边数
	JTextField DuZhongxin = new JTextField(60);// 度中心性数
	JTextField JieshuZhongxin = new JTextField(60);// 介数中心性
	JTextField JiejinZhongxin = new JTextField(60);// 接近中心性
	JTextField mokuaidu = new JTextField(60);// 接近中心性

	JButton fx = new JButton("生成项目网络");
	JButton gx = new JButton("计算项目网络");
	JButton hx = new JButton("显示项目网络 ");
	JTextField pnetbian = new JTextField(60);// 项目网路的边数
	JTextField pnetdingdianshu = new JTextField(60);// 项目网络顶点数

	JTextField pnetpingjundu = new JTextField(60);// 项目网路的边数
	JTextField pnetpingjunqiangdu = new JTextField(50);// 项目网络顶点数
	JTextField pnetpingcuxishu = new JTextField(50);// 项目网络顶点数

	JTextField Txjingdu = new JTextField(60);// 进度
	JProgressBar progressbar = new JProgressBar();
	int allrows = 0;// 总行数
	int pagesize = 10;
	ArrayList Vlist = new ArrayList();
	Graph WorkGraph = null;
	int NodeCnt = 0;
	int BianCnt = 0;
	UndirectedSparseGraph<String, String> MyGraph = new UndirectedSparseGraph<String, String>();
	UndirectedSparseGraph<String, String> ProGraph = new UndirectedSparseGraph<String, String>();

	HashMap<Integer, Set<Integer>> AllProInfo = new HashMap<Integer, Set<Integer>>();// 保存所有信息

	// SparseMultigraph<String, String> g = new SparseMultigraph<String,
	// String>();
	public SocialNetAnyFrame() {
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu();
		JMenuItem menuFileExit = new JMenuItem();
		menuFile.setText("File");
		menuFileExit.setText("Exit");

		// Add action listener.for the menu button
		menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SocialNetAnyFrame.this.windowClosed();
			}
		});
		menuFile.add(menuFileExit);
		menuBar.add(menuFile);

		setTitle("SocialNetAny");
		setJMenuBar(menuBar);
		setSize(new Dimension(800, 800));

		Init();
		setLocationRelativeTo(null);

		// Add window listener.
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SocialNetAnyFrame.this.windowClosed();
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b)// 读取节点数
		{

			allrows = 0;// 总行数
			pagesize = 10;
			WorkGraph = null;
			NodeCnt = 0;
			BianCnt = 0;

			DBHelper help = new DBHelper();
			// String rowssql =
			// "select id from 9700scmlog where id = object_id('data2') and indid in (0,1)";
			String rowssql = "select * from 9700scmlog";
			int rows = Integer.parseInt((String) help.GetSingle(rowssql));
			allrows = rows;// //

			progressbar.setMinimum(1);
			progressbar.setMaximum(rows);

			Thread t = new Thread(new Runnable() {
				public void run() {
					int value = 1;

					int start = 1;
					int end = start + pagesize;
					String rowssql = "";
					Vlist.clear();
					while (true) {
						rowssql = "select  * 9700scmlog where id >= " + start
								+ " and id <" + end;

						DataTable dt = help.GetDataTable(rowssql);
						int committer_id = 0;
						int merged_id = 1;
						int repository_id = 1;
						for (int i = 0; i < dt.RowCount; i++) {

							DataRow cr = dt.GetRow().get(i);

							committer_id = cr.GetColumnInt("committer_id");
							merged_id = dt.GetRow().get(i)
									.GetColumnInt("merged_id");

							if (committer_id != merged_id) {

								if (Vlist.indexOf(committer_id) == -1) {
									Vlist.add(committer_id);
									NodeCnt++;
									Txdingdiancnt.setText(Integer
											.toString(NodeCnt));

								}
								if (Vlist.indexOf(merged_id) == -1) {
									Vlist.add(merged_id);
									NodeCnt++;
									Txdingdiancnt.setText(Integer
											.toString(NodeCnt));
								}
							}
						}
						start = end;
						end = start + pagesize;
						if (start == rows)
							break;
						if (end > rows)
							end = rows;
						Txjingdu.setText(end + "/" + Integer.toString(allrows));
						progressbar.setValue(end);

					}

				}
			});
			t.start();

		} else if (e.getSource() == c)// 生成网络
		{
			allrows = 0;// 总行数
			pagesize = 10;
			WorkGraph = null;
			NodeCnt = 0;
			BianCnt = 0;

			DBHelper help = new DBHelper();
			// String rowssql =
			// "select id from 9700scmlog where id = object_id('data2') and indid in (0,1)";
			String rowssql = "select * from 9700scmlog";
			int rows = Integer.parseInt((String) help.GetSingle(rowssql));
			allrows = rows;// //

			progressbar.setMinimum(1);
			progressbar.setMaximum(rows);

			Thread t = new Thread(new Runnable() {
				public void run() {

					DBHelper help = new DBHelper();
					int value = 1;
					int start = 1;
					int end = start + pagesize;
					String rowssql = "";
					Vlist.clear();
					while (true) {
						rowssql = "select * from 9700scmlog";

						DataTable dt = help.GetDataTable(rowssql);
						int committer_id = 0;
						int merged_id = 1;
						int repository_id = 1;
						for (int i = 0; i < dt.RowCount; i++) {

							DataRow cr = dt.GetRow().get(i);

							committer_id = cr.GetColumnInt("committer_id");
							merged_id = dt.GetRow().get(i)
									.GetColumnInt("merged_id");

							// /////////保存项目网络所有信息
							repository_id = dt.GetRow().get(i)
									.GetColumnInt("repository_id");
							if (AllProInfo.containsKey(repository_id) == false) {
								AllProInfo.put(repository_id,
										new HashSet<Integer>());
							}

							Set temp = AllProInfo.get(repository_id);

							temp.add(committer_id);
							temp.add(merged_id);

							if (committer_id != merged_id) {

								String str = new String();
								String str1 = new String();
								str = String.valueOf(committer_id) + "-"
										+ String.valueOf(merged_id);
								str1 = String.valueOf(committer_id) + "-"
										+ String.valueOf(merged_id);

								MyGraph.addVertex(String.valueOf(committer_id));
								MyGraph.addVertex(String.valueOf(merged_id));
								ProGraph.addVertex(String
										.valueOf(repository_id));

								String edge = MyGraph.findEdge(
										String.valueOf(committer_id),
										String.valueOf(merged_id));
								if (edge == null) {
									MyGraph.addEdge(str,
											String.valueOf(committer_id),
											String.valueOf(merged_id));
									Txbianshu.setText(Integer.toString(MyGraph
											.getEdgeCount()));
									Txdingdiancnt.setText(Integer
											.toString(MyGraph.getVertexCount()));
								}

							}
						}
						start = end;
						end = start + pagesize;
						if (start == allrows)
							break;
						if (end > allrows)
							end = allrows;
						Txjingdu.setText(end + "/" + Integer.toString(allrows));
						progressbar.setValue(end);

					}
				}
			});
			t.start();

		} else if (e.getSource() == d)// 计算平均度，聚类系数
		{

			// double rs = WorkGraph.GetAverDu();

			// Txaverdu.setText( Double.toString(rs));
			// Txjuleixishu.setText(
			// Double.toString(WorkGraph.GetJuleixishu()));

			DegreeScorer ds = new DegreeScorer(MyGraph);

			BetweennessCentrality ranker = new BetweennessCentrality(MyGraph);// 计算介数中心性
			ranker.setRemoveRankScoresOnFinalize(false);
			ranker.evaluate();
			ClosenessCentrality cc = new ClosenessCentrality(MyGraph);// 计算接近中心性

			Collection<String> VCC = MyGraph.getVertices();

			Iterator<String> ii = VCC.iterator();

			int num = MyGraph.getVertexCount();
			double jieshusum = 0;
			double jiejinsum = 0;
			double dejinsum = 0;
			double juleisum = 0;
			while (ii.hasNext()) {
				double bb = 0;
				String vv = ii.next();

				bb = ds.getVertexScore(vv);
				dejinsum += bb;
				System.out.println("节点" + vv + "度中心性:" + bb);

				bb = ranker.getVertexRankScore(vv);
				jieshusum += bb;
				System.out.println("节点" + vv + "介数中心性:" + bb);

				bb = cc.getVertexScore(vv);
				jiejinsum += bb;
				System.out.println("节点" + vv + "接近中心性:" + bb);

				// ///
				double ni = MyGraph.getNeighborCount(vv);// 该节点的邻接节点数

				double li = 0.0;// 邻接节点实际连接数
				Collection<String> NCC = MyGraph.getNeighbors(vv);
				String[] strNei = NCC.toArray(new String[NCC.size()]);

				for (int i = 0; i < strNei.length - 1; i++) {
					for (int j = 1; j < strNei.length; j++) {
						if (MyGraph.isNeighbor(strNei[i], strNei[j]))
							li++;
					}
				}
				double julei = 0;
				if (ni > 1)
					julei = 2 * li / (ni * (ni - 1));
				juleisum += julei;
				System.out.println("节点" + vv + "聚类系数:" + julei);
				System.out.println("");
			}

			StructurallyEquivalent vp = new StructurallyEquivalent();

			DuZhongxin.setText(Double.toString(dejinsum / num));
			JieshuZhongxin.setText(Double.toString(jieshusum / num));
			JiejinZhongxin.setText(Double.toString(jiejinsum / num));

			Txaverdu.setText(Double.toString(dejinsum / num));

			Txjuleixishu.setText(Double.toString(juleisum / num));

			// mokuaidu.setText(
			// Integer.toString(vp.transform(MyGraph).numPartitions()));

		} else if (e.getSource() == ex)// 计算平均度，聚类系数
		{

			System.out.println("The graph g = " + MyGraph.getEdgeCount());
			System.out.println("连接成功1");
			JFrame frame = new JFrame("显示开发者网络");
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			frame.setBounds(0, 0, 600, 600);
			Layout<String, String> layout = new CircleLayout(MyGraph);
			BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(
					layout);

			vv.getRenderContext().setVertexLabelTransformer(
					new ToStringLabeller());

			frame.getContentPane().add(vv);
			// frame.pack();
			frame.setVisible(true);

		} else if (e.getSource() == fx)// 生成网络
		{

			Integer[] vstr = AllProInfo.keySet().toArray(
					new Integer[AllProInfo.keySet().size()]);

			for (int i = 0; i < vstr.length - 1; i++) {
				Set vi = AllProInfo.get(vstr[i]);
				for (int j = i + 1; j < vstr.length; j++) {
					Set<Integer> result = new HashSet<Integer>();

					Set vj = AllProInfo.get(vstr[j]);
					// //求交集
					result.clear();
					result.addAll(vi);
					result.retainAll(vj);
					if (result.isEmpty() == false) {
						int jiaosize = result.size();

						// 求并集
						result.clear();
						result.addAll(vi);
						result.addAll(vj);
						double weight = jiaosize * 1.0 / result.size();
						ProGraph.addEdge(
								String.valueOf(weight) + ":"
										+ String.valueOf(vstr[i]) + "-"
										+ String.valueOf(vstr[j]),
								String.valueOf(vstr[i]),
								String.valueOf(vstr[j]));

					}

				}

			}

			pnetbian.setText(Integer.toString(ProGraph.getEdgeCount()));
			pnetdingdianshu
					.setText(Integer.toString(ProGraph.getVertexCount()));

		} else if (e.getSource() == gx)// 计算网络
		{

			DegreeScorer ds = new DegreeScorer(ProGraph);
			Collection<String> VCC = ProGraph.getVertices();

			Iterator<String> ii = VCC.iterator();

			int num = ProGraph.getVertexCount();

			double qiangdusum = 0;
			double dejinsum = 0;
			double cuxishusum = 0;
			while (ii.hasNext()) {
				double bb = 0;
				String vv = ii.next();

				bb = ds.getVertexScore(vv);
				dejinsum += bb;
				System.out.println("项目节点" + vv + "度:" + bb);

				Collection<String> NCC = ProGraph.getNeighbors(vv);
				String[] strNei = NCC.toArray(new String[NCC.size()]);
				double wv = 0.0;
				for (int i = 0; i < strNei.length; i++) {
					String edge = ProGraph.findEdge(vv, strNei[i]);
					String[] temparr = edge.split(":");
					double weight = Double.parseDouble(temparr[0]);
					wv += weight;
				}
				System.out.println("项目节点" + vv + "强度:" + wv);
				qiangdusum += wv;

				double cu = 0;
				cu = 1 / (wv * (bb - 1));
				System.out.println("项目节点" + vv + "簇系数:" + cu);
				System.out.println("");
				cuxishusum += cu;
			}

			pnetpingjundu.setText(Double.toString(dejinsum / num));
			pnetpingjunqiangdu.setText(Double.toString(qiangdusum / num));
			pnetpingcuxishu.setText(Double.toString(cuxishusum / num));

			System.out.println("");
		} else if (e.getSource() == hx)// 显示
		{

			JFrame frame = new JFrame("显示项目网络");
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			frame.setBounds(0, 0, 600, 600);
			Layout<String, String> layout = new CircleLayout(ProGraph);
			BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(
					layout);

			vv.getRenderContext().setVertexLabelTransformer(
					new ToStringLabeller());
			vv.getRenderContext().setEdgeLabelTransformer(
					new ToStringLabeller());

			frame.getContentPane().add(vv);
			// frame.pack();
			frame.setVisible(true);

		}
	}

	public void Init() {

		progressbar.setOrientation(JProgressBar.HORIZONTAL);
		Container contentPanel = getContentPane();

		b.addActionListener(this);
		d.addActionListener(this);
		c.addActionListener(this);
		ex.addActionListener(this);
		fx.addActionListener(this);
		gx.addActionListener(this);
		hx.addActionListener(this);
		JPanel pNorth = new JPanel();

		// pNorth.add(b);
		pNorth.add(c);
		pNorth.add(d);
		pNorth.add(fx);
		pNorth.add(gx);
		pNorth.add(ex);
		pNorth.add(hx);
		add(pNorth, BorderLayout.NORTH);
		add(progressbar, BorderLayout.SOUTH);

		JPanel pCenter = new JPanel();
		BoxLayout layout = new BoxLayout(pCenter, BoxLayout.Y_AXIS);
		pCenter.setLayout(layout);

		JPanel j1 = new JPanel();
		// pCenter.setLayout(new GridLayout(4,2));
		JPanel j3 = new JPanel();

		j3.add(new JLabel("顶点数："));
		j3.add(Txdingdiancnt);
		pCenter.add(j3);

		JPanel j4 = new JPanel();
		j4.add(new JLabel("边数："));
		j4.add(Txbianshu);
		pCenter.add(j4);
		j1.add(new JLabel("平均度："));
		j1.add(Txaverdu);
		pCenter.add(j1);

		JPanel j2 = new JPanel();
		j2.add(new JLabel("聚类系数："));
		j2.add(Txjuleixishu);
		pCenter.add(j2);

		JPanel j5 = new JPanel();
		j5.add(new JLabel("度中心性："));
		j5.add(DuZhongxin);
		pCenter.add(j5);

		JPanel j6 = new JPanel();
		j6.add(new JLabel("介数中心性："));
		j6.add(JieshuZhongxin);
		pCenter.add(j6);

		JPanel j7 = new JPanel();
		j7.add(new JLabel("接近中心性："));
		j7.add(JiejinZhongxin);
		pCenter.add(j7);

		// JPanel j8 = new JPanel();
		// j8.add(new JLabel("模块度："));
		// j8.add(mokuaidu);
		// pCenter.add(j8);

		JPanel j10 = new JPanel();
		j10.add(new JLabel("项目网络顶点数："));
		j10.add(pnetdingdianshu);
		pCenter.add(j10);

		JPanel j11 = new JPanel();
		j11.add(new JLabel("项目网络边数："));
		j11.add(pnetbian);
		pCenter.add(j11);

		JPanel j12 = new JPanel();
		j12.add(new JLabel("项目网络平均度："));
		j12.add(pnetpingjundu);
		pCenter.add(j12);

		JPanel j13 = new JPanel();
		j13.add(new JLabel("项目网络平均强度："));
		j13.add(pnetpingjunqiangdu);
		pCenter.add(j13);

		JPanel j14 = new JPanel();
		j14.add(new JLabel("项目网络平均簇系数："));
		j14.add(pnetpingcuxishu);
		pCenter.add(j14);

		JPanel j9 = new JPanel();
		j9.add(new JLabel("进度"));
		j9.add(Txjingdu);
		pCenter.add(j9);

		add(pCenter, BorderLayout.CENTER);
	}

	/**
	 * Shutdown procedure when run as an application.
	 */
	protected void windowClosed() {

		// TODO: Check if it is safe to close the application

		// Exit application.
		System.exit(0);
	}
}