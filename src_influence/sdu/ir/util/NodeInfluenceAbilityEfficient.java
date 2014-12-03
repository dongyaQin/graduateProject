package sdu.ir.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import sdu.ir.interfaces.Graph;

public class NodeInfluenceAbilityEfficient {

	/**
	 * @param args
	 * 此类存储节点在k步之内分别能影响哪些节点
	 */
	Graph g = null;
	AdjacencyList[] list;
	Map<Integer,Info> map = new HashMap<Integer, Info>();
	int k;
	double[] record;//记录增益值,CELF中运用
	private double[] record_ub;//与上面的作用相同，在UBGreedy中使用
	public NodeInfluenceAbilityEfficient(Graph g,int k) {
		this.g = g;
		this.k = k;
		list = new AdjacencyList[g.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = new AdjacencyList(i);
		}
		for (int i = 0; i < g.size(); i++) {
			map.put(i, new Info((double)0,4));
		}
		record = new double[g.size()];
		for (int i = 0; i < record.length; i++) {
			record[i] = Double.MAX_VALUE;
		}
		record_ub = new double[g.size()];
		for (int i = 0; i < record_ub.length; i++) {
			record_ub[i] = Double.MAX_VALUE;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void calculate() {
		NetProperty np = new NetProperty(g);
		for (int i = 0; i < list.length; i++) {
			AdjacencyList temp = list[i];
			temp.cal(np,k);
		}
		
	}

	public int get_max_gain_node_efficient(Set<Integer> initSet, double[] p) {
		double max = -1;
		int selectedNode = -1;
		for (int i = 0; i < g.size(); i++) {
			if(!initSet.contains(i)){
				double temp = influenceSimulateEfficient(i,p,false);
				System.out.println(i+"增加"+temp);
				
				if(temp > max){
					max = temp;
					selectedNode = i;
				}
			}
		}
		influenceSimulateEfficient(selectedNode,p,true);

//		print_node_state();
//		Util.block();
		return selectedNode;
	}
	
	public int get_max_gain_node_efficient_CELF(Set<Integer> initSet, double[] p) {
		double last_max_gain = -1;
		int selectedNode = -1;
		for (int i = 0; i < g.size(); i++) {
			if(!initSet.contains(i)){
				if(record[i] <= last_max_gain)continue;
				double temp = influenceSimulateEfficient(i,p,false);
				record[i] = temp;
				System.out.println(i+"增加"+temp);
				
				if(temp > last_max_gain){
					last_max_gain = temp;
					selectedNode = i;
				}
			}
		}
		influenceSimulateEfficient(selectedNode,p,true);

//		print_node_state();
//		Util.block();
		return selectedNode;
	}
	
	private void print_node_state() {
		Set<Integer> set = map.keySet();
		for (Integer i : set) {
			Info info = map.get(i);
			if(info.p != 0){
				System.out.println(i+"--->"+info.p);
			}
		}
		
	}

	private void refresh(Map<Integer, Info> tempMap) {
		map.putAll(tempMap);
		
	}

	private double influenceSimulateEfficient(int i, double[] p, boolean isRefresh) {
		Map<Integer,Info> tempMap = new HashMap<Integer, Info>();
		double increase = 0;
		Queue<NodeProperty> q = new LinkedList<NodeProperty>();
		q.add(new NodeProperty(1, 0, i));
//		System.out.println(map.get(i) == null);
		increase += 1 - map.get(i).p;
		tempMap.put(i, new Info((double)1,0));
		while(!q.isEmpty()){
			NodeProperty np = q.remove();
			int node = np.node;
			double probability1 = -1;
			int dis1 = -1;
			if(!tempMap.containsKey(node)){
				probability1 = np.p;
				dis1 = np.dis;
			}else{
				probability1 = tempMap.get(node).p;
				dis1 = tempMap.get(node).dis;
			}
				
			if(dis1 == p.length)break;
			Info in = map.get(node);
			double probability0 = in.p;
			int dis0 = in.dis;
			Set<Integer> neibor = g.getNeighbor(node);
			for (Integer nei : neibor) {
				Info info = map.get(nei);
				double p_new = -1;
				int dis_new = -1;
//				System.out.println("对于node->"+nei+":");
				if(!tempMap.containsKey(nei)){//之前没有被搜到
//					System.out.println("之前没有被搜到");
					if(info.dis >= dis1+1){//能影响
//						System.out.println(node+"能影响他");
						if(info.dis <= dis0 || info.dis > p.length){
//							System.out.println(info.dis+"\t"+dis1);
							if(info.dis > dis1 + 1){
								p_new = probability1*p[dis1];
								dis_new = dis1 + 1;
							}else{
								p_new = 1 - (1 - info.p)*(1 - probability1*p[dis1]);
								dis_new = dis1 + 1;
							}
						}else{
							if(info.dis > dis1 + 1){
								p_new = probability1*p[dis1];
								dis_new = dis1 + 1;
							}else{
								double other_1_p = (1-info.p)/(1-probability0*p[dis0]);
								p_new = 1 - other_1_p*(1 - probability1*p[dis1]);
								dis_new = dis1 + 1;
							}
						}
						increase += p_new - info.p;
//						System.out.println("得到的新的概率和距离分别为："+p_new+"\t"+dis_new);
//						System.out.println("增加的概率为："+(p_new - info.p));
						
						NodeProperty np_new = new NodeProperty(p_new, dis_new, nei);
						q.add(np_new);
						tempMap.put(nei, new Info((double)p_new,dis_new));
					}
					
					
				}else{//之前有被搜到过
//					System.out.println("之前已经被搜到了");
					Info info1 = tempMap.get(nei);
					if(info1.dis >= dis1 + 1){//能影响
//						System.out.println(node+"能影响他");
						if(info.dis <= dis0 || info.dis > p.length){
							p_new = 1 - (1 - info1.p)*(1 - probability1*p[dis1]);
							dis_new = dis1 + 1;
						}else{
							double other_1_p = (1-info1.p)/(1-probability0*p[dis0]);
							p_new = 1 - other_1_p*(1 - probability1*p[dis1]);
							dis_new = dis1 + 1;
						}
//						System.out.println("得到的新的概率和距离分别为："+p_new+"\t"+dis_new);
						increase += p_new - info1.p;
//						System.out.println("增加的概率为："+(p_new - info1.p));
//						NodeProperty np_new = new NodeProperty(p_new, dis_new, nei);
//						q.add(np_new);
						tempMap.put(nei, new Info((double)p_new,dis_new));
					
					}
					
					
				}
//				if(dis_new != -1){
//					
//				}
				
				
			}
		}
		if(isRefresh){
			map.putAll(tempMap);
		}
		return increase;
	}

	class NodeProperty{
		double p;
		int dis;
		int node;
		public NodeProperty(double d, int i, int nodeId) {
			p = d;
			dis = i;
			node = nodeId;
		}
		public NodeProperty refresh(double d) {
			this.p = d;
//			System.out.println("refresh"+p);
			return this;
		}
		
	}

	public int get_max_gain_node_efficient(Set initSet) {
		double max = -1;
		int selectedNode = -1;
		for (int i = 0; i < g.size(); i++) {
			if(!initSet.contains(i)){
				double temp = bfs(i,false);
				System.out.println(i+"增加"+temp);
				
				if(temp > max){
					max = temp;
					selectedNode = i;
				}
			}
		}
		bfs(selectedNode,true);
		return selectedNode;
	}
	
	public int get_max_gain_node_efficient_CELF(Set initSet) {
		double last_max_gain = -1;
		int selectedNode = -1;
		for (int i = 0; i < g.size(); i++) {
			if(!initSet.contains(i)){
				if(record_ub[i] <= last_max_gain)continue;
				double temp = bfs(i,false);
				System.out.println(i+"增加"+temp);
				record_ub[i] = temp;
				if(temp > last_max_gain){
					last_max_gain = temp;
					selectedNode = i;
				}
			}
		}
		bfs(selectedNode,true);
		return selectedNode;
	}

	private double bfs(int i, boolean b) {
		Map<Integer,Info> tempMap = new HashMap<Integer, Info>();
		boolean[] bs = new boolean[g.size()];
		bs[i] = true;
		double increase = 0;
		Queue<NodeProperty> q = new LinkedList<NodeProperty>();
		q.add(new NodeProperty(1, 0, i));
		increase += 1 - map.get(i).p;
		while(!q.isEmpty()){
			NodeProperty np = q.remove();
			int node = np.node;
			double p = np.p;
			int dis = np.dis;
			if(dis == 3)break;
			Set<Integer> neibors = g.getNeighbor(node);
			for (Integer neibor : neibors) {
				if(!bs[neibor]){
					if(map.get(neibor).p != 1){
						tempMap.put(neibor, new Info((double)1,dis+1));
						increase ++;
					}
					q.add(new NodeProperty(1, dis+1, neibor));
					bs[neibor] = true;
				}
			}
		}
		if(b){
			map.putAll(tempMap);
		}
		return increase;
	}
	
}
