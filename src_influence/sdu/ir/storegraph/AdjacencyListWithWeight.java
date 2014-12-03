package sdu.ir.storegraph;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.interfaces.Graph;
import sdu.ir.util.ChainNode;
import sdu.ir.util.Edge;
import sdu.ir.util.Util;

public class AdjacencyListWithWeight implements Graph{
	private Set<ChainNode>[] list;
//	private int length;
	public AdjacencyListWithWeight(int length) {
		list = new HashSet[length];
		for (int i = 0; i < list.length; i++) {
			list[i] = null;
		}
//		this.length = length;
	}
	
	@Override
	public int getWeight(int i, int j) {
		Set<ChainNode> temp = list[i];
		for (ChainNode node : temp) {
			if(node.getNumber() == j){
				return node.getWeight();
			}
		}
		return 0;
	}

	@Override
	public void setWeight(int i, int j, int weight) {
		Set<ChainNode> temp = list[i];
		for (ChainNode node : temp) {
			if(node.getNumber() == j){
				node.setWeight(weight);
			}
		}
		
	}

	@Override
	public int size() {
		return list.length;
	}
	
	public int getLength() {
		int temp = 0;
		for (int i = 0; i < list.length; i++) {
			if(list[i] != null)
				temp++;
		}
		return temp;
	}
	
	@Override
	public Set getNeighbor(int i) {
		Set set = new HashSet();
		if(list[i] != null){
			for (ChainNode node : list[i]) {
				set.add(node.getNumber());
			}
			return set;
		}
		return null;
	}

	@Override
	public int getIndegree(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutdegree(int i) {
		return list[i].size();
	}

	public void add(int i, int j, int k) {
		if(list[i] == null)
			list[i] = new HashSet<ChainNode>();
		list[i].add(new ChainNode(j,k));
		
	}
	public void setBetweenness(int i, int j, double betweenness) {
		Set<ChainNode> temp = list[i];
		for (ChainNode node : temp) {
			if(node.getNumber() == j){
				node.setBetweenness(betweenness);
			}
		}
	}
	public double getBetweenness(int i, int j) {
		Set<ChainNode> temp = list[i];
		for (ChainNode node : temp) {
			if(node.getNumber() == j){
				return node.getBetweenness();
			}
		}
		return -1;
	}

	public void removeEdge(Edge removeEdge) {
		int i = removeEdge.getI();
		int j = removeEdge.getJ();
		Set<ChainNode> set = list[i];
		for (ChainNode chainNode : set) {
			if(chainNode.getNumber() == j){
				set.remove(chainNode);
				break;
			}
		}
		Set<ChainNode> set1 = list[j];
		for (ChainNode chainNode : set1) {
			if(chainNode.getNumber() == i){
				set1.remove(chainNode);
				break;
			}
		}

		
	}
	//将图一分为二
	public Set<Graph> divideGraphIntoTwo(boolean[] isUsed) {
		AdjacencyListWithWeight g1 = new AdjacencyListWithWeight(list.length);
		AdjacencyListWithWeight g2 = new AdjacencyListWithWeight(list.length);
		g1.list = new HashSet[list.length];
		g2.list = new HashSet[list.length];
		for (int i = 0; i < isUsed.length; i++) {
			if(isUsed[i]){
				g1.list[i] = list[i];
//				g2.list[i] = new HashSet();
//				g2.setLength(g2.getLength()-1);
			}else{
				g2.list[i] = list[i];
//				g1.list[i] = new HashSet();
//				g1.setLength(g1.getLength()-1);
			}
		}
		Set<Graph> graphs = new HashSet<Graph>();
		graphs.add(g2);
		graphs.add(g1);
		return graphs;
	}

	
	public AdjacencyListWithWeight[] divideGraphIntoPieces() {
		int[] mark = new int[list.length];
		int biaoji = 1;
		int num = 0;
		
		for (int i = 0; i < list.length; i++) {
			if(list[i] != null && mark[i] == 0){
				Util.bfs(this,i,biaoji,mark);
				num++;
				biaoji++;
			}
			
		}
		AdjacencyListWithWeight[] graphs = new AdjacencyListWithWeight[num];
//		System.out.println(num);
		for (int i = 0; i < graphs.length; i++) {
			graphs[i] = new AdjacencyListWithWeight(list.length);
		}
		for (int i = 0; i < mark.length; i++) {
			if(mark[i] > 0){
//				System.out.println(mark[i]-1+" "+i);
				graphs[mark[i]-1].list[i] = list[i];
			}
		}
		return graphs;
	}
	
	
	public int computeConnectedComponentNum() {
		int[] mark = new int[list.length];
		int biaoji = 1;
		int num = 0;
		for (int i = 0; i < list.length; i++) {
			if(list[i] != null && mark[i] == 0){
				Util.bfs(this,i,biaoji,mark);
				num++;
			}
		}
		return num;
	}

	public Set<Integer> getNodes() {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < list.length; i++) {
			if(list[i] != null){
				set.add(i);
			}
		}
		return set;
	}
	//复制图
	public AdjacencyListWithWeight copy() {

		AdjacencyListWithWeight adjList = new AdjacencyListWithWeight(list.length);
		for (int j = 0; j < list.length; j++) {
			Set<ChainNode> set = list[j];
			if(list[j] != null){
				for (ChainNode chainNode : set) {
					adjList.add(j, chainNode.getNumber(), chainNode.getWeight());
				}
			}
			
			
		}
		return adjList;
	}

	@Override
	public Set<Integer> getInNeighbor(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
