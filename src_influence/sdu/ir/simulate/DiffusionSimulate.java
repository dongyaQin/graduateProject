package sdu.ir.simulate;

import irlab.dbOperate.Data;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.communitydetection.EdgeBetweenness;
import sdu.ir.communitydetection.VoltageDrops;
import sdu.ir.diffusionmodel.ICM3;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.storegraph.AdjacencyListWithWeight;
import sdu.ir.util.NetProperty;
import sdu.ir.util.Util;
import test.Print;

import com.mathworks.toolbox.javabuilder.MWException;

public class DiffusionSimulate {
	int beginNode = 0;//��¼��С�Ľڵ���0����1
	static int targetSize = 2;//��ʼ�������ϵĴ�С
	static Data database = new Data();//Data�������������ݿ�д����
	static String suffix;//���β������ݿ��ĺ�׺
	static String dataName;//���β��������ļ���
	//����ֱ��¼�����ֲ�ͬ��ѡ���ʼ���Ϸ�ʽ���ֱ��ڳ�ʼ���ϴ�С��1-targetSize��С������£�����Ӱ��ĸ���
	
	
	static double[] communityDetectionHighdgree ;
	static double[] communityDetectionGreedy ;
	public DiffusionSimulate(int targetSize) {
		this.targetSize = targetSize;
		
		communityDetectionHighdgree = new double[targetSize+1];
		communityDetectionGreedy = new double[targetSize+1];
	}
	
	//ÿ��̰��ѡ��ʹӰ����󻯵Ľڵ�
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private double[] greedy(Graph graph,DiffusionModel dm) {
		double[] greedy = new double[targetSize];
		Set initSet = new HashSet();
		initSet.add(107);
		double max = 0;
		while(initSet.size() < targetSize){
			long a=System.currentTimeMillis();
			max = 0;
			int selectedNode = -1;
			for (int i = beginNode; i < graph.size(); i++) {
				if(!initSet.contains(i)){
					initSet.add(i);
					double temp = dm.diffusion(graph, initSet);
					System.out.println(i+"Ӱ��"+temp);
					if(temp > max){
						max = temp;
						selectedNode = i;
					}
					initSet.remove(i);
				}
			}
//			System.out.println("selectedNode---->"+selectedNode);
			initSet.add(selectedNode); 
			System.out.println(initSet); 
			double temp = dm.diffusion(graph, initSet);
			greedy[initSet.size()-1] = temp;
//			database.write2Database(initSet.size(),temp,initSet, dataName+"_greedy"+suffix);
			System.out.println("ִ�к�ʱ ===>"+(System.currentTimeMillis()-a)/1000f+" �� ");
//			Scanner scan = new Scanner(System.in);
//			scan.next();
		}
		System.out.println("���ŵĳ�ʼ����"+initSet);
		return greedy;
	}
	//���ݶ�ֵ�ĴӴ�С��˳��ѡ��ڵ�
	private double[] highDegree(Graph graph,DiffusionModel dm) {
		double[] highDegree = new double[targetSize];
		int[] degree = new int[graph.size()];
		for (int i = beginNode; i < graph.size(); i++) {
			degree[i] = graph.getOutdegree(i);
		}
//		Print.print(degree);
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			long a=System.currentTimeMillis();
			int max = Util.findMax(degree,0);
			initSet.add(max);
			highDegree[i] = dm.diffusion(graph, initSet);
			database.write2Database(initSet.size(),highDegree[i],initSet, dataName+"_highDegree"+suffix);
			System.out.println(initSet+"  "+highDegree[i]);
			degree[max] = -1;
			System.out.println("ִ�к�ʱ ===>"+(System.currentTimeMillis()-a)/1000f+" �� ");
		}
		System.out.println(("���ŵĳ�ʼ����"+initSet));
		return highDegree;

	}
	
	//strategy������1.highdegree ��ʾ������ѡ��������Ƚڵ���Ϊ�����ڵ� 2.greedy ������̰��ѡ��ʹ������Ӱ����󻯵Ľڵ�
	public double communityDetectionEdgeBetweenness(AdjacencyListWithWeight graph, DiffusionModel dm) {
		EdgeBetweenness eb = new EdgeBetweenness(beginNode);
		Graph[] communitys = null;

		communitys = eb.divideAndCompute(graph,targetSize,communityDetectionHighdgree,communityDetectionGreedy,dm,database,dataName,suffix);
//
//			
//		
//		Set initSet = new HashSet();
//		for (int i = 0; i < targetSize; i++) {
//			int max = Util.findMaxIndegree(communitys[i]);
//			initSet.add(max);
//		}
//		
//		System.out.println("���ŵĳ�ʼ����"+initSet);
//		Print.print(graph);
		return communityDetectionHighdgree[targetSize];
	}
	
	private double communityDetectionVoltageDrops(Graph graph,DiffusionModel dm,int targetSize) {
		VoltageDrops vd = new VoltageDrops();
		Graph[] communitys = vd.communityDetection(graph,targetSize);
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			int max = Util.findMaxIndegree(communitys[i]);
			initSet.add(max);
		}
		return dm.diffusion(graph, initSet);

	}
	
	public double[] neiborIntensity(Graph graph,DiffusionModel dm,int numOfNeibor,int targetSize){
		NetProperty np = new NetProperty(graph);
		int[] rankResult = np.getNeiborIntenOrderOf(numOfNeibor);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
		int count = -1;
		for (int i = 0; i < rankResult.length && count < targetSize - 1; i++) {
			if(i != 0 && distanceCan(initSet,rankResult[i],np,numOfNeibor)){
				continue;
			}
			count++;
			initSet.add(rankResult[i]);
			result[count] = dm.diffusion(graph, initSet);
			database.write2Database(initSet.size(),result[count],initSet, dataName+"_neiborIntensity"+suffix);
		}
		return result;
	}


	public double[] neiborIntensityOverlap(Graph graph,DiffusionModel dm,int numOfNeibor,int targetSize){
		NetProperty np = new NetProperty(graph);
		int[] rankResult = np.getNeiborIntenOrderOf(numOfNeibor);
		Print.print(rankResult);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			initSet.add(rankResult[i]);
			result[i] = dm.diffusion(graph, initSet);
			System.out.println(initSet+"*&&&*"+result[i]);
			database.write2Database(initSet.size(),result[i],initSet, dataName+"_neiborIntensityOverlap"+suffix);
		}
		return result;
	}
	
	public double[] neiborWeightNum(Graph graph,DiffusionModel dm,int numOfNeibor,double[] p,int targetSize){
		NetProperty np = new NetProperty(graph);
		int[] rankResult = np.getNeiborWeightNumberOrder(numOfNeibor,p);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
		int count = -1;
		int lastChoose = -1;
		for (int i = 0; i < rankResult.length && count < targetSize - 1; i++) {
			if(i != 0 && distanceCan(initSet,rankResult[i],np,numOfNeibor)){
				continue;
			}
			count++;
			initSet.add(rankResult[i]);
			lastChoose = rankResult[i];
			result[count] = dm.diffusion(graph, initSet);
			
			database.write2Database(initSet.size(),result[count],initSet, dataName+"_neiborWeightNum"+suffix);
		}
		return result;
	}
	
	public double[] neiborWeightNumOverlap(Graph graph,DiffusionModel dm,int numOfNeibor,double[] p,int targetSize){
		NetProperty np = new NetProperty(graph);
		int[] rankResult = np.getNeiborWeightNumberOrder(numOfNeibor,p);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			initSet.add(rankResult[i]);
			result[i] = dm.diffusion(graph, initSet);
			database.write2Database(initSet.size(),result[i],initSet, dataName+"_neiborWeightNumOverlap"+suffix);
		}
		return result;
	}
	
	public static void main(String[] args) throws MWException {
		
		dataName = "gyjs";
		double begin = System.currentTimeMillis();
//		 String filePath = "E:\\software\\���ݼ�\\������Ƿ񴫲����.txt";
		 String filePath = "G:\\�о�ɮ\\���ݼ�\\���ݼ�\\"+dataName+".txt";
//	     String filePath = "E:\\���ݼ�\\gh1.txt";
		 ReadGraph rd = new ReadGraph();
//		 Graph gh2 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);//Print.print(gh);
//		 AdjacentMatrix aa = (AdjacentMatrix)gh2;
//		System.out.println(aa.getEdges());
//		System.out.println(aa.isSymmetry());
//		Util.block();
		 Graph gh1 = rd.readTxtFile2Graph(filePath, "Adjacentlistwithweight",2);
		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2);
		 Print.print(gh1);
		 Print.print(gh);
		 int executions = 2000;//icm��ģ�����
		 int set = 2;//���ϴ�С
		 double threshold = 0.3;
//		 double p = 0.5;
		 double[] p = new double[]{0.5,0.3,0.1};
//		 LinearThresholdModel ltm = new LinearThresholdModel(threshold,gh);//����Ϊ��ֵ
//		 IndependentCascadeModel icm = new IndependentCascadeModel(executions,p);//����Ϊ1.ģ�����         2.��Ⱦ����
		 ICM3 icm3 = new ICM3(executions, p[0], p[1], p[2]);
		 DiffusionSimulate ds = new DiffusionSimulate(set);//����Ϊ��ʼ���ϴ�С
		 DiffusionModel dm = icm3;//ѡ��Ҫʹ�õĴ���ģ��

		 suffix = Util.calSuffix(executions,set,p);
//		 database.createTables(dataName,suffix);
		 double[] greedy = ds.greedy(gh, dm);
//		 double[] highDegree = ds.highDegree(gh, dm);
//		 double[] neiborIntensity = ds.neiborIntensity(gh, dm, 3, set);
//		 double[] neiborWeightNum = ds.neiborWeightNum(gh, dm, 3, p, set);
//		 double[] neiborIntensityOverlap = ds.neiborIntensityOverlap(gh, dm, 3, set);
//		 double[] neiborWeightNumOverlap = ds.neiborWeightNumOverlap(gh, dm, 3, p, set);
////		 ds.communityDetectionEdgeBetweenness((AdjacencyListWithWeight) gh1,dm);
		 communityDetectionHighdgree = format(communityDetectionHighdgree);
		 communityDetectionGreedy = format(communityDetectionGreedy);
		 Print.print(greedy);
//		 Print.print(highDegree);
//		 Print.print(neiborIntensity);
//		 Print.print(neiborWeightNum);
//		 Print.print(neiborIntensityOverlap);
//		 Print.print(neiborWeightNumOverlap);
		 Print.print(communityDetectionHighdgree);
		 Print.print(communityDetectionGreedy);
		 String X1="̰��";
		 String Y1="��ֵ";
		 String M1="����̰��";
		 String Z1="������ֵ";
		 String title="ICMģ���������㷨�Ĵ������(p="+p+")";
		
//		 Plot a = new Plot();
//		 a.draw_curve_auto(greedy,highDegree,communityDetectionHighdgree,communityDetectionGreedy,X1,Y1,Z1,M1,title);
//		 OutPut.write2File(threshold+"_ltm_greedy_"+set, greedy);
//		 OutPut.write2File(threshold+"_ltm_highDegree_"+set, highDegree);
//		 OutPut.write2File(threshold+"_ltm_communityDetectionHighdgree_"+set, communityDetectionHighdgree);
//		 OutPut.write2File(threshold+"_ltm_communityDetectionGreedy_"+set, communityDetectionGreedy);
		 System.out.println("������ִ�к�ʱ ===>"+(System.currentTimeMillis()-begin)/1000f+" �� ");
	}

//	private static void write2Database(double[] array,
//			String string) {
//		for (int i = 0; i < array.length; i++) {
//			database.write2Database(i+1,array[i],null, dataName+"_"+string+suffix);
//		}
//		
//	}

	private static double[] format(double[] greedy2) {
		double[] greedy = new double[greedy2.length-1];
		for (int i = 0; i < greedy.length; i++) {
			greedy[i] = greedy2[i+1];
		}
		return greedy;
	}

	private boolean distanceCan(Set<Integer> initSet, int i, NetProperty np, int numOfNeibor) {
		for (Integer in : initSet) {
			if(np.getDistance(in, i) <= numOfNeibor){
				return true;
			}
		}
		return false;
	}
	

}
