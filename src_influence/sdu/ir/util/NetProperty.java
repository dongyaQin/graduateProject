package sdu.ir.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import test.Print;

public class NetProperty {
	private Graph g;
	public byte[][] dis;
	public NetProperty(Graph g) {
		this.g = g;
	}
	
	public static void main(String[] args) {
	 	String filePath = "ccir2014\\email2.txt";
		ReadGraph rd = new ReadGraph();
		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
		Print.print(graph1);
		NetProperty np = new NetProperty(graph1);
//		int a = np.getNumOfDistance(112,7);
//		System.out.println(a);
		int b = np.getDistance(1,2);
		np.getAllPairDistance();
		byte[][] dis = np.dis;
		System.out.println(dis[332][516]);
		System.out.println(dis[231][516]);
		System.out.println(dis[1077][497]);
		System.out.println(dis[1050][1062]);
		System.out.println(dis[753][614]);
		System.out.println(dis[1118][1119]);
//		int count = 0;
//		for (int i = 1; i < dis.length; i++) {
//			System.out.print(dis[332][i]+" ");
//		}
//		System.out.println(count);
//		Print.print(dis);
		double d = np.getMeanDis();
		System.out.println("Mean Distance--->"+d);
		
//		int[] aa = np.getNeiborWeightNumberOrder(3,new double[]{ 0.8, 0.5, 0.1});
		int[] aa = np.getNeiborIntenOrderOf(3);
		Print.print(aa);
		
	}

	public double getMeanDis() {
		getAllPairDistance();
		int sum = 0;
		int count = 0;
		for (int i = 0; i < dis.length; i++) {
			for (int j = i +1; j < dis.length; j++) {
				if(i != j){
					sum += dis[i][j];
					count++;
				}
			}
		}
		return (double)sum/(double)count;
	}

	public void getAllPairDistance() {
		byte[][] dis = new byte[g.size()][g.size()];
		for (int i = 0; i < dis.length; i++) {
			for (int j = 0; j < dis[0].length; j++) {
				if(i == j){
					dis[i][j] = 0;
				}else{
					dis[i][j] = (byte) (g.getWeight(i, j)>0?1:50);
				}
			}
		}
		for (int k = 0; k < dis.length; k++) {
			for (int i = 0; i < dis.length; i++) {
				for (int j = 0; j < dis.length; j++) {
					dis[i][j] = min((byte) (dis[i][k]+dis[k][j]),dis[i][j]);
				}
			}
		}
		this.dis = dis;
	}

	private byte min(byte i, byte j) {
		return i<j?i:j;
	}

	public int getDistance(int i, int j) {
		if(dis != null){
			return dis[i][j];
		}
		int[] record = new int[g.size()+1]; 
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(i);
		record[i] = 1;
		while(!q.isEmpty()){
			int ii = q.poll();
			Set<Integer> neibor = g.getNeighbor(ii);
			if(neibor != null){
				for (Integer in : neibor) {
					if(record[in] == 0){
						record[in] = record[ii] + 1 ;
						q.add(in);
						if(in == j){
							return record[in];
						}
					}
				}
			}
			
		}
		return -1;
	}

	public int getNumOfDistance(int i,int dis) {
		int[] record = new int[g.size()+1]; 
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(i);
		record[i] = 1;
		int number = 0;
		while(!q.isEmpty()){
			int ii = q.poll();
			Set<Integer> neibor = g.getNeighbor(ii);
			if(neibor != null){
				for (Integer in : neibor) {
					if(record[in] == 0 && record[ii] <= dis){
						record[in] = record[ii] + 1 ;
						q.add(in);
						number ++;
					}
				}
			}
			
		}
		return number;
	}

	public int[] getNeiborIntenOrderOf(int numOfNeibor) {
		if(this.dis == null)this.getAllPairDistance();
		Float[][] result = new Float[g.size()][2];
		for (int i = 0; i < g.size(); i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(i);
			for (int j = 0; j < dis.length; j++) {
				if(dis[i][j] > 0 && dis[i][j] <= numOfNeibor){
					list.add(j);
				}
			}
			result[i][0] = (float) i;
//			System.out.println("---------------"+i+"----------------");
//			System.out.println(list);
			result[i][1] = calInten(list);
		}
		Util.quickSort(result, 0, result.length-1);
		int[] retru = new int[g.size()];
		for (int j = 0; j < retru.length; j++) {
			retru[j] = result[j][0].intValue();
//			System.out.print(retru[j]+"&&&");System.out.println();
//			System.out.print(result[j][1]+"***");System.out.println();
		}
		Util.invert(retru);
		return retru;
	}

	private float calInten(ArrayList<Integer> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				if(g.getWeight(list.get(i), list.get(j)) > 0){
					sum ++;
				}
			}
		}
		int divder = list.size()*(list.size()-1);
//		System.out.println(sum+" "+divder);
		return (float)sum/(float)divder;
	}

	public int[] getNeiborWeightNumberOrder(int numOfNeibor, double[] p) {
		if(this.dis == null)this.getAllPairDistance();
		Double[][] result = new Double[g.size()][2];
		for (int i = 0; i < g.size(); i++) {
//			System.out.println("---------"+i+"----------");
			double total = 0;
			for (int j = 1; j <= numOfNeibor; j++) {
				int sum = 0;
				for (int k = 0; k < dis.length; k++) {
					if(dis[i][k] > j-1 && dis[i][k] <= j){
						sum++;
					}
				}
//				System.out.println("---->"+sum+"*"+p[j-1]);
				total += sum*p[j-1];
			}
//			Util.block();
			result[i][0] =  (double) i;
			result[i][1] = total;
		}
		Util.quickSort(result, 0, result.length-1);
		int[] retru = new int[g.size()];
		for (int j = 0; j < retru.length; j++) {
			retru[j] = result[j][0].intValue();
			System.out.print(result[j][1]+"***");
		}
		Print.print(retru);
		Util.invert(retru);
		return retru;
	}
	
	public double[][] getNeiborWeightNumber(int numOfNeibor,double[] p){
		if(this.dis == null)this.getAllPairDistance();
//		System.out.println(dis.length);
		double[][] result = new double[g.size()][2];
		for (int i = 0; i < g.size(); i++) {
//			System.out.println("---------"+i+"----------");
			double total = 0;
			for (int j = 1; j <= numOfNeibor; j++) {
				int sum = 0;
				for (int k = 0; k < dis.length; k++) {
					if(dis[i][k] == j){
						sum++;
					}
				}
//				System.out.println("---->"+sum+"*"+p[j-1]);
				System.out.print(i+"距离为"+(j)+"的数量为->"+sum+"\t");
				total += sum*p[j-1];
			}
			System.out.println();
//			Util.block();
			result[i][0] =  (double) i;
			result[i][1] = total;
		}
		return result;
	}

	public ArrayList<Integer>[] getAdList(int i, int k) {
		ArrayList<Integer>[] list = new ArrayList[k];
		for (int j = 0; j < list.length; j++) {
			list[j] = new ArrayList<Integer>();
		}
		int[] record = new int[g.size()+1]; 
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(i);
		record[i] = 1;
		while(!q.isEmpty()){
			int ii = q.poll();
			Set<Integer> neibor = g.getNeighbor(ii);
			if(neibor != null){
				for (Integer in : neibor) {
					if(record[in] == 0 && record[ii] <= k){
						record[in] = record[ii] + 1 ;
						q.add(in);
						list[record[in]-2].add(in);
					}
				}
			}
			
		}
		return list;
	}

}
