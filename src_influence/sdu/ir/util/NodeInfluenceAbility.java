package sdu.ir.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sdu.ir.interfaces.Graph;

public class NodeInfluenceAbility {

	/**
	 * @param args
	 * 此类存储节点在k步之内分别能影响哪些节点
	 */
	Graph g = null;
	AdjacencyList[] list;
	Map<Integer,Info> map = new HashMap<Integer, Info>();
	int k;
	public NodeInfluenceAbility(Graph g,int k) {
		this.g = g;
		this.k = k;
		list = new AdjacencyList[g.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = new AdjacencyList(i);
		}
		for (int i = 0; i < g.size(); i++) {
			map.put(i, new Info((double)0,4));
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

	public int get_max_gain_node(Set initSet, double[] p) {
		double max = 0;
		int selectedNode = -1;
		for (int i = 0; i < g.size(); i++) {
			if(!initSet.contains(i)){
				initSet.add(i);
				double temp = influenceSimulate(initSet,p);
				System.out.println(i+"影响"+temp);
				if(temp > max){
					max = temp;
					selectedNode = i;
				}
				initSet.remove(i);
			}
		}
		return selectedNode;
	}

	public double influenceSimulate(Set<Integer> initialSet, double[] p) {
		Set<Integer> backup = new HashSet<Integer>();
		backup.addAll(initialSet);
		int record = 0;
		Set<Integer> newInfected = new HashSet<Integer>();
		Map<Integer,NodeProperty> map = new HashMap<Integer, NodeProperty>();
		for (Integer in : initialSet) {
			map.put(in, new NodeProperty((double) 1,0));
		}
		boolean haveNewInfected = true;
		while(haveNewInfected){
			haveNewInfected = false;
			record ++;
//			System.out.println("这是第"+record+"步感染");
			for (Integer obj: initialSet) {   
				int i= obj; 
				Set<Integer> temp = g.getNeighbor(i);
				if(temp != null){
					for (Integer obj1 : temp) {  
						int i1= obj1;  
						if(record <= p.length && !map.containsKey(i1)){
//									System.out.println("成功感染节点"+i1);
							newInfected.add(i1);
							map.put(i1, new NodeProperty(map.get(i).p*p[record - 1],map.get(i).dis+1));
							haveNewInfected = true;
						}else if(record <= p.length && map.containsKey(i1) && map.get(i1).p != 1 && map.get(i1).dis == map.get(i).dis+1){
							double p1 = 1- map.get(i1).p;
							double p2 = 1- map.get(i).p*p[record - 1];
//							System.out.println(i+" "+p1+" "+p2);
							map.put(i1, map.get(i1).refresh(1-p1*p2));
						}else{
//									System.out.println(activated[i1]);
						}
					}
				}
			}
//			System.out.println("第"+record+"步感染结束");
//			System.out.println("感染了---》"+newInfected);
			initialSet.removeAll(initialSet);
			initialSet.addAll(newInfected);
			newInfected.removeAll(newInfected);
		}
		initialSet.removeAll(initialSet);
		initialSet.addAll(backup);
		Set<Integer> set = map.keySet();
		double t = 0;
		for (Integer i : set) {
			t += map.get(i).p;
//			System.out.println(i+"--->"+map.get(i).p);
		}
		return t;
	}
	class NodeProperty{
		public NodeProperty(double d, int i) {
			p = d;
			dis = i;
		}
		public NodeProperty refresh(double d) {
			this.p = d;
//			System.out.println("refresh"+p);
			return this;
		}
		double p;
		int dis;
	}
	
}
