package sdu.ir.diffusionmodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import text.Print;
//经测试，正确！
public class ICM3NoMentoCalo implements DiffusionModel{
	private double p1 = 0.5;//感染邻居的概率
	private double p2 = 0.5;//感染邻居的概率
	private double p3 = 0.5;//感染邻居的概率
	private boolean[] activated ;
	private int record = 0;
	private int count = 10;
	private double[] ps = new double[3];
	//count是指模拟多少次，因为ICM模型下每次结果不一样，所以需要试验多次以使得结果更准确
	public ICM3NoMentoCalo(int count,double p1,double p2,double p3) {
		this.count = count;
		ps[0] = p1;
		ps[1] = p2;
		ps[2] = p3;
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		int num = 0;
		int init =  (Integer) initialSet.iterator().next();
		System.out.println("init->"+init);
		num = spread(graph, init);
		return num;
	}

	//递归的用ICM传播模型传播
	private int spread(Graph graph, int init) {
		Map<Integer,Double>[] maps = new HashMap[4];
		for (int i = 0; i < maps.length; i++) {
			maps[i] = new HashMap<Integer, Double>();
		}
		int num = 0;
		int distance = 0;
		Queue<NodeProperty> q = new LinkedList<NodeProperty>();
		maps[0].put(init, 1.0);
		q.add(new NodeProperty(0, init));
		while(!q.isEmpty()){
			NodeProperty node = q.remove();
			Set<Integer> outNeibors = graph.getNeighbor(node.id);
			if(node.dis == 3) break;
			double p_node = maps[node.dis].get(node.id);
			for (Integer neibor : outNeibors) {
				if(maps[0].get(neibor) != null) continue;
				if(maps[node.dis+1].get(neibor) == null){
					if(node.dis == 2 && maps[1].containsKey(neibor) 
							&& graph.getWeight(neibor, node.id) == 1){
						double p1 = maps[1].get(neibor);
						double p2 = maps[2].get(node.id);
						double p2_except_p1 = 1 - (1-p2)/(1-p1*ps[2]);
						if(neibor == 6){
							System.out.println(p1+" "+p2+" "+p2_except_p1);
						}
						maps[node.dis+1].put(neibor, p2_except_p1*ps[node.dis]);
					}else{
						maps[node.dis+1].put(neibor, p_node*ps[node.dis]);
					}
					q.add(new NodeProperty(node.dis+1, neibor));
				}else{
					double p_temp = maps[node.dis+1].get(neibor);
					if(node.dis == 2 && maps[1].containsKey(neibor) 
							&& graph.getWeight(neibor, node.id) == 1){
						double p1 = maps[1].get(neibor);
						double p2 = p_node;
						double p2_except_p1 = 1 - (1-p2)/(1-p1*ps[2]);
						maps[node.dis+1].put(neibor,1-(1-p2_except_p1*ps[node.dis])*(1-p_temp));
						if(neibor == 6){
							System.out.println(p1+" "+p2+" "+p2_except_p1);
						}
					}else{
						maps[node.dis+1].put(neibor,1-(1-p_node*ps[node.dis])*(1-p_temp));
					}
					
				}
			}
		}
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < maps.length; i++) {
			set.addAll(maps[i].keySet());
		}
		for (int i = 0; i < maps.length; i++) {
			Set<Integer> set1 = maps[i].keySet();
			for (Integer in : set1) {
				System.out.println(in+"-->"+maps[i].get(in));
			}
			System.out.println("-----------------------------");
		}
		for (Integer node : set) {
			double temp = 0;
			for (int i = 0; i < maps.length; i++) {
				if(maps[i].containsKey(node)){
					temp = 1-(1-temp)*(1-maps[i].get(node));
				}
			}
			System.out.println(node+"--->"+temp);
		}
		return num;
	}

	private boolean checkCanInfect(int r) {
		
		double p = 0;
		if(r == 1){
			p = p1;
		}else if(r == 2){
			p = p2;
		}else if(r == 3){
			p = p3;
		}
//		System.out.print("当前的感染概率为"+p);
		if(Math.random() <= p)
			return true;
		else
			return false;
	}
	
	class NodeProperty{
		//		double p;
		int dis;
		int id;
		public NodeProperty(int i, int nodeId) {
//			p = d;
			dis = i;
			id = nodeId;
		}	
	}
	
	public static void main(String[] args) {
//		 String filePath = "E:\\数据集\\gh1.txt";
//		 String filePath = "E:\\数据集\\temp.txt";
		 String filePath = "G:\\研究僧\\数据集\\数据集\\gh2.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2);
		Print.print(graph);System.out.println(graph.size());
		ICM3NoMentoCalo icm = new ICM3NoMentoCalo(20000,0.5,0.5,0.5);
		Set set = new HashSet();
//		set.add(1);
//		set.add(2);
//		set.add(3);
//		set.add(4);
//		set.add(5);
//		set.add(6);
		set.add(5);
//		set.add(333);
//		set.add(8);
//		set.add(9);
		System.out.println(icm.diffusion(graph, set));
//		NetProperty net = new NetProperty(graph);
//		System.out.println(net.getNumOfDistance(1123, 10));
	}

}
