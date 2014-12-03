package sdu.ir.communitydetection;

import irlab.dbOperate.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import sdu.ir.diffusionmodel.IndependentCascadeModel;
import sdu.ir.diffusionmodel.LinearThresholdModel;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.simulate.DiffusionSimulate;
import sdu.ir.storegraph.AdjacencyListWithWeight;
import sdu.ir.util.Edge;
import sdu.ir.util.Util;
import text.Print;

public class EdgeBetweenness {
	int beginNode = 0;
	public EdgeBetweenness() {

	}

	public EdgeBetweenness(int beginNode) {
		this.beginNode = beginNode;
	}

	public Graph[] divideAndCompute(AdjacencyListWithWeight gh, int targetSize, double[] communityDetectionHighdegree, double[] communityDetectionGreedy, DiffusionModel dm, Data database, String dataName, String suffix) {
		AdjacencyListWithWeight gh1 = gh.copy();
		Set<Graph> graphs = new HashSet<Graph>();
		graphs.add(gh1);
		int connectedComponentNum = 1;
		while(connectedComponentNum <= targetSize){
			double a = System.currentTimeMillis();
			if(communityDetectionHighdegree[connectedComponentNum] == 0){
				communityDetectionHighdegree[connectedComponentNum] = computeInfluenceHighDegree(gh1.divideGraphIntoPieces(),gh,dm,database,dataName,suffix);
			}
			if(communityDetectionGreedy[connectedComponentNum] == 0){
				communityDetectionGreedy[connectedComponentNum] = computeInfluenceGreedy(gh1.divideGraphIntoPieces(),gh,dm,database,dataName,suffix);
				if(connectedComponentNum == targetSize)break;
			}
//			double b = System.currentTimeMillis();
			computeBetweenness(gh1);
			Edge removeEdge = findMaxBetweenness(gh1);
			gh1.removeEdge(removeEdge);
//			System.out.println("removeEdge--->("+removeEdge.getI()+","+removeEdge.getJ()+")=>"+removeEdge.getBetweenness());
			connectedComponentNum = gh1.computeConnectedComponentNum();
//			System.out.println();
			System.out.println("执行耗时 ===>"+(System.currentTimeMillis()-a)/1000f+" 秒  and 当前连通分支数："+connectedComponentNum);
		}
		AdjacencyListWithWeight[] graphs1 = gh1.divideGraphIntoPieces();
//		for (int i = 0; i < graphs1.length; i++) {
//			Print.print(graphs1[i].getNodes());
//		}
		return graphs1;
	}

	private double computeInfluenceGreedy(
			AdjacencyListWithWeight[] divideGraphIntoPieces,
			AdjacencyListWithWeight gh,
			DiffusionModel dm, Data database, String dataName, String suffix) {
			Set initSet = new HashSet();
			Set returnSet = new HashSet();
			for (int i = 0; i < divideGraphIntoPieces.length; i++) {
				int selectedNode = -1;
			
				double max = 0;
//				Print.print(divideGraphIntoPieces[i]);
				for (int j = beginNode; j < divideGraphIntoPieces[i].size(); j++) {
					if(divideGraphIntoPieces[i].getNeighbor(j) != null){
						initSet.add(j);
						double temp = dm.diffusion(divideGraphIntoPieces[i], initSet);
//						System.out.println(j+"节点开始最大影响为"+temp);
						if(temp > max){
							max = temp;
							selectedNode = j;
						}
						initSet.remove(j);
					}
				}
//				Util.block();
	//			System.out.println("selectedNode---->"+selectedNode);
				
				
				
				returnSet.add(selectedNode);
//				System.out.println("集合"+returnSet);
	//			Util.block();
				
				
			}
			double temp = dm.diffusion(gh, returnSet);
			database.write2Database(returnSet.size(),temp,returnSet, dataName+"_communityDetectionGreedy"+suffix);
			return temp;
	}

	private double computeInfluenceHighDegree(
			AdjacencyListWithWeight[] divideGraphIntoPieces,
			AdjacencyListWithWeight gh,
			DiffusionModel dm, Data database, String dataName, String suffix) {
			Set initSet = new HashSet();
			for (int i = 0; i < divideGraphIntoPieces.length; i++) {
				int selectedNode = -1;
				selectedNode = Util.findMaxIndegree(divideGraphIntoPieces[i]);
				initSet.add(selectedNode);
	//			System.out.println(initSet);
	//			Util.block();
//				System.out.println("----------------------------");
//				Print.print(divideGraphIntoPieces[i]);System.out.print("####");
//				System.out.println("----------------------------");
			
			}
//			System.out.println("_communityDetectionHighdgree"+initSet);
			double temp = dm.diffusion(gh, initSet);
			database.write2Database(initSet.size(),temp,initSet, dataName+"_communityDetectionHighdgree"+suffix);
			return temp;
	}

	public Graph[] divide(AdjacencyListWithWeight gh, int targetSize) {
		AdjacencyListWithWeight gh1 = gh.copy();
//		Print.print(gh);
//		Print.print(gh1);
//		Scanner scan = new Scanner(System.in);
//		scan.next();
		Set<Graph> graphs = new HashSet<Graph>();
		graphs.add(gh1);
		int connectedComponentNum = 1;
		while(connectedComponentNum < targetSize){
			double a = System.currentTimeMillis();
			computeBetweenness(gh1);
			Edge removeEdge = findMaxBetweenness(gh1);
			gh1.removeEdge(removeEdge);
//			System.out.println("removeEdge--->("+removeEdge.getI()+","+removeEdge.getJ()+")=>"+removeEdge.getBetweenness());
			connectedComponentNum = gh1.computeConnectedComponentNum();
//			System.out.println();
			System.out.println("执行耗时 ===>"+(System.currentTimeMillis()-a)/1000f+" 秒  and 当前连通分支数："+connectedComponentNum);
		}
		AdjacencyListWithWeight[] graphs1 = gh1.divideGraphIntoPieces();
		for (int i = 0; i < graphs1.length; i++) {
			Print.print(graphs1[i].getNodes());
		}
		return graphs1;
	}
	
	
	private double computeInfluence(AdjacencyListWithWeight[] divideGraphIntoPieces, DiffusionModel dm, AdjacencyListWithWeight gh, String strategy) {
		Set initSet = new HashSet();
		for (int i = 0; i < divideGraphIntoPieces.length; i++) {
			int selectedNode = -1;
			if(strategy == null || strategy.equalsIgnoreCase("highdegree"))
				selectedNode = Util.findMaxIndegree(divideGraphIntoPieces[i]);
			if(strategy.equalsIgnoreCase("greedy")){
				double max = 0;
				for (int j = beginNode; j < divideGraphIntoPieces[i].size(); j++) {
					if(divideGraphIntoPieces[i].getNeighbor(j) != null){
						initSet.add(j);
						double temp = dm.diffusion(divideGraphIntoPieces[i], initSet);
						System.out.println(j+"节点开始最大影响为"+temp);
						if(temp > max){
							max = temp;
							selectedNode = j;
						}
						initSet.remove(j);
					}
				}
//				System.out.println("selectedNode---->"+selectedNode);
			}
			
			
			initSet.add(selectedNode);
//			System.out.println(initSet);
//			Util.block();
			
		}
		return dm.diffusion(gh, initSet);
	}

	private Graph findMaxGraph(Set<Graph> graphs) {
		int max = 0;
		Graph temp = null;
		for (Graph graph : graphs) {
			if(((AdjacencyListWithWeight)graph).getLength() > max){
				max = ((AdjacencyListWithWeight)graph).getLength();
				temp = graph;
			}
		}
		return temp;
	}

	private Set<Graph> isConnected(AdjacencyListWithWeight gh1, int i, int j) {
		
		boolean[] isUsed = new boolean[gh1.size()];
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(i);
		while(!q.isEmpty()){
			int u = q.poll();
			Set<Integer> set = gh1.getNeighbor(u);
			for (Integer v : set) {
				if(!isUsed[v]){
					isUsed[v] = true;
					q.add(v);
					if(v == j){
						Set<Graph> returnSet = new HashSet<Graph>();
						returnSet.add(gh1);
						return returnSet;
					}
				}
			}
		}
		Set<Graph> returnSet = gh1.divideGraphIntoTwo(isUsed);
		return returnSet;
	}

	private Edge findMaxBetweenness(AdjacencyListWithWeight gh1) {
		int i = -1;
		int j = -1;
		double max = -1;
		for (int k = beginNode; k < gh1.size(); k++) {
			Set<Integer> set = gh1.getNeighbor(k);
			if(set != null){
				for (Integer integer : set) {
					double temp = gh1.getBetweenness(k, integer);
					if(temp > max){
						max = temp;
						i = k;
						j = integer;
					}
				}
			}
			
		}
		
		return new Edge(i,j,max);
	}
	//不是连通的图也可以
	private void computeBetweenness(AdjacencyListWithWeight gh) {
		int v = gh.size();
		double[][] betweenness = new double[v][v];
//		double[] node_betweenness = new double[v];
		for (int i = beginNode; i < v; i++) {
			Stack<Integer> s = new Stack<Integer>();
			ArrayList<Integer>[] parents = new ArrayList[v];
			for (int j = beginNode; j < parents.length; j++) {
				parents[j] = new ArrayList<Integer>();
			}
			int[] dis = new int[v];
			for (int j = beginNode; j < dis.length; j++) {
				dis[j] = -1;
			}
			dis[i] = 0;
			int[] num_shortestPath = new int[v];
			num_shortestPath[i] = 1;
			Queue<Integer> q = new LinkedList<Integer>();
			q.add(i);
			while(!q.isEmpty()){
				int u = q.poll();
				s.push(u);
				Set<Integer> neibor = gh.getNeighbor(u);
				if(neibor != null){
					for (Integer w : neibor) {
						if(dis[w] < 0){
							dis[w] = dis[u]+1;
							q.add(w);
						}
						if(dis[w] == dis[u]+1){
							num_shortestPath[w] += num_shortestPath[u];
							parents[w].add(u);
						}
					}
				}
				
			}
			double[] dependency = new double[v];
			while(!s.isEmpty()){
				int w = s.pop();
				ArrayList<Integer> list = parents[w];
				for (int j = 0; j < list.size(); j++) {
					int u = list.get(j);
					betweenness[u][w] += ((double)num_shortestPath[u]/(double)num_shortestPath[w])*(1+dependency[w]);
					dependency[u] += ((double)num_shortestPath[u]/(double)num_shortestPath[w])*(1+dependency[w]);
				}
				//计算node的介数，已经注掉
//				if(w != i){
//					node_betweenness[w] += dependency[w];
//				}
					
			}
		}
		//计算node的介数，已经注掉
//		for (int i = beginNode; i < node_betweenness.length; i++) {
//			node_betweenness[i] /= 2;
//		}
		for (int i = beginNode; i < v; i++) {
			Set<Integer> set = gh.getNeighbor(i);
			if(set != null){
				for (Integer j : set) {
					gh.setBetweenness(i,j,betweenness[i][j]);
				}
			}
			
		}
	}

//	private int getEdgesNum(Graph gh) {
//		int out = 0;
//		for (int i = 0; i < gh.size(); i++) {
//			out+=gh.getNeighbor(i).size();
//		}
//		return out;
//	}
	public static void main(String[] args) {
//		 String filePath = "E:\\数据集\\gh1.txt";
//		 String filePath = "E:\\数据集\\Email\\email.txt";
		 String filePath = "G:\\研究僧\\数据集\\数据集\\"+"gh2"+".txt";
		 ReadGraph rd = new ReadGraph();
//		 Graph gh = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);//Print.print(gh);
		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithweight",2);
//		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2);
		 EdgeBetweenness eb = new EdgeBetweenness();
		 eb.divide((AdjacencyListWithWeight) gh, 2);
		 
		 LinearThresholdModel ltm = new LinearThresholdModel(0.5);//参数为阈值
		 IndependentCascadeModel icm = new IndependentCascadeModel(100,0.1);//参数为1.模拟次数         2.传染概率
		 DiffusionSimulate ds = new DiffusionSimulate(10);//参数为初始集合大小
		 ds.communityDetectionEdgeBetweenness((AdjacencyListWithWeight) gh,icm);
	}
	
}
