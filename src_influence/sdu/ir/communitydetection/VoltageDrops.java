package sdu.ir.communitydetection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Constant;
import sdu.ir.util.Util;
import test.Print;

public class VoltageDrops {
	int refreshTimes = 50;
	int groups = 50;
	double errorRate = 0.5;
	double communitySizeerrorRate = 0.5;
	public static void main(String[] args) {
		 String filePath = "E:\\software\\数据集\\karateclub.txt";
//	     String filePath = "E:\\software\\数据集\\gh1.txt";
		 ReadGraph rd = new ReadGraph();
		 Graph gh1 = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2);
		 VoltageDrops vd = new VoltageDrops();
		 vd.communityDetection(gh1, 2);
	}

	public Graph[] communityDetection(Graph graph, int targetSize) {
		ArrayList<int[]> voteCommunity = getGoupLists(graph,targetSize);//得到一百个投票社区
		ArrayList<int[]> communitiesNode = getCommunitiesNode(voteCommunity,graph.size(),targetSize);//得到所划分的每个社区的节点
		
		return null;
	}
	private ArrayList<int[]> getCommunitiesNode(ArrayList<int[]> voteCommunity, int n, int targetSize) {
		int[] record = new int[n];
		boolean[] isFindedCom = new boolean[n];
		for (int[] arrays : voteCommunity) {
			for (int integer : arrays) {
				record[integer] ++;
				System.out.print(integer+"　");
			}
			System.out.println();
		}
		Print.print(record);
		for (int ii = 0; ii < targetSize; ii++) {
			int[] recordTimeSameCom = new int[n];
			int i = Util.findMax(record,isFindedCom,Constant.BEGINNODE);
			System.out.println("i->"+i);
			//记录跟节点i分到一个社区内的次数
			for (int[] arrays : voteCommunity) {
				if(Util.contains(arrays,i)){
					for (int j = 0; j < arrays.length; j++) {
						recordTimeSameCom[arrays[j]] ++;
					}
				}
			}
			int max = recordTimeSameCom[i];
			int standard = max - (int)(max*communitySizeerrorRate);
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < recordTimeSameCom.length; j++) {
				if(recordTimeSameCom[j] > standard){
					list.add(j);
					isFindedCom[j] = true;
				}
			}
			System.out.println(list);
		}
		
		return null;
	}

	//运用电压下降找到预备分组
	private ArrayList<int[]> getGoupLists(Graph graph,
			int targetSize) {
		ArrayList<int[]> returnList = new ArrayList<int[]>();
		for (int ii = 0; ii < this.groups; ii++) {
			int n = graph.size();
			int beginNode = randomChooseNode(Constant.BEGINNODE,n-1);//随机选择一个节点
			int endNode = findSinkNode(beginNode,graph);
			System.out.println("beginNode->"+beginNode+" endNode->"+endNode);
			if(beginNode == endNode){
				ii--;
				continue;
			}
			double[] voltages = new double[n];
			voltages[beginNode] = 1;
			for (int i = 0; i < refreshTimes; i++) {
				refreshVoltages(graph,voltages,beginNode,endNode);
//				Print.print(voltages);//当所输出数组过长时，使用print打印不出来！！！
			}
			double[][] use2Sort = new double[n][2];
			format(voltages,use2Sort);
//			Util.quickSort(use2Sort, 0, voltages.length-1); 					//这句话注掉是因为有个quicksort的bug ，若要使代码正确，需要对这句话解注
			int min = n/targetSize - (int)(n/targetSize*errorRate);
			int max = n/targetSize + (int)(n/targetSize*errorRate);
			//找到前一个分组
			int a = findPartition(use2Sort,min,max);
			int[] left = new int[a+1];
			for (int i = 0; i <= a; i++) {
				left[i] = ( (int) use2Sort[i][0] );
			}
			//找到后一个分组
			int b = findPartition(use2Sort,n-max,n-min);
			int[] right = new int[use2Sort.length-b-1];
			for (int i = b+1; i <use2Sort.length; i++) {
				right[i-b-1] = ( (int) use2Sort[i][0] );
			}
			Util.quickSort(left, 0, left.length-1);
			Util.quickSort(right, 0, right.length-1);
			returnList.add(left);
			returnList.add(right);
		}
		return returnList;
	}

	private int findSinkNode(int beginNode, Graph graph) {
		int[] dis = new int[graph.size()];
		for (int i = 0; i < dis.length; i++) {
			dis[i] = Integer.MAX_VALUE;
		}
		dis[beginNode] = 0;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(beginNode);
		while(!q.isEmpty()){
			int temp = q.remove();
			Set<Integer> set = graph.getNeighbor(temp);
			for (Integer integer : set) {
				
				if(dis[temp] + 1 < dis[integer]){
					if(dis[temp]+1 > 2)
						return integer;
					dis[integer] = dis[temp] + 1;
					q.add(integer);
				}
				
			}
		}
		System.out.println("为节点"+beginNode+"找sink节点的时候，没有找到距离大于2的");
		for (int i = 0; i < dis.length; i++) {
			if(dis[i] == 2){
				return i;
			}
		}
		System.out.println("为节点"+beginNode+"找sink节点的时候，没有找到距离等于2的");
		for (int i = 0; i < dis.length; i++) {
			if(dis[i] == 1){
				return i;
			}
		}
		return beginNode;
		
	}

	private int randomChooseNode(int Min, int Max) {
		return  (int) Math.round(Math.random()*(Max-Min)+Min);
	}

	//从0 ... record为一个社区 ，record+1 ... end是一个社区
	private int findPartition(double[][] use2Sort, int left, int right) {
		int n = use2Sort.length;
		int record = left;
		double maxReduce = 0;
		for (int i = left; i < right; i++) {
			if(use2Sort[i+1][1] - use2Sort[i][1] > maxReduce){
				record = i;
				maxReduce = use2Sort[i+1][1] - use2Sort[i][1];
			}
		}
		return record;
	}

	private void formatPrint(double[][] use2Sort) {
		for (int i = 0; i < use2Sort.length; i++) {
			System.out.print(use2Sort[i][0]+"->"+use2Sort[i][1]+"   ");
		}
		
	}

	private void format(double[] voltages, double[][] use2Sort) {
		for (int i = 0; i < use2Sort.length; i++) {
			use2Sort[i][0] = i;
			use2Sort[i][1] = voltages[i];
			
		}
		
	}

	private void refreshVoltages(Graph graph, double[] voltages, int beginNode, int endNode) {
		for (int i = Constant.BEGINNODE; i < graph.size(); i++) {
			if(i != beginNode && i != endNode){
				Set<Integer> set = graph.getNeighbor(i);
				double volSum = 0;
				for (Integer in : set) {
					volSum += voltages[in];
				}
//				System.out.println(volSum+" "+set.size());
				voltages[i] = volSum/set.size();
			}
			
		}
		
	}

	
	
}
