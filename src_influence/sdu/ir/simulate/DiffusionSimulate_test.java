package sdu.ir.simulate;

import io.AppendFile;
import io.FileOp;
import irlab.dbOperate.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sdu.ir.diffusionmodel.ICM3MultiThread;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.IMMethod;
import sdu.ir.util.NodeInfluenceAbility;
import sdu.ir.util.NodeInfluenceAbilityEfficient;
import sdu.ir.util.Util;

import com.mathworks.toolbox.javabuilder.MWException;

public class DiffusionSimulate_test {
	int beginNode = 0;//记录最小的节点是0还是1
	static int targetSize = 2;//初始传播集合的大小
	static Data database = new Data();//Data对象用来往数据库写东西
	static String suffix;//本次测试数据库表的后缀
	static String dataName;//本次测试数据文件名
	static String file2Store;
	static Map<IMMethod,String> map= new HashMap<IMMethod, String>();
	//下面分别记录用三种不同的选择初始集合方式，分别在初始集合大小从1-targetSize大小的情况下，最终影响的个数
	
	
	static double[] communityDetectionHighdgree ;
	static double[] communityDetectionGreedy ;
	private static int canWrite2DB = 0;
	public DiffusionSimulate_test(int targetSize) {
		this.targetSize = targetSize;
		
		communityDetectionHighdgree = new double[targetSize+1];
		communityDetectionGreedy = new double[targetSize+1];
	}
	
	//每次贪心选择使影响最大化的节点
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private double[] greedy(Graph graph,DiffusionModel dm) {
		double[] record = new double[graph.size()];//记录增益值
		for (int i = 0; i < record.length; i++) {
			record[i] = Double.MAX_VALUE;
		}
		double[] greedy = new double[targetSize];
		Set initSet = new HashSet();
		double max = 0;
		double last_max = -1;//记录上一次影响的最大值
		double last_max_gain = -1;
		int selectedNode = -1;
		System.out.println(beginNode+" "+selectedNode+" "+max);
		while(initSet.size() < targetSize){
			long a=System.currentTimeMillis();
			for (int i = beginNode; i < graph.size(); i++) {
				if(!initSet.contains(i)){
					if(record[i] <= last_max_gain)continue;
					initSet.add(i);
					double temp = dm.diffusion(graph, initSet);
					record[i] = temp - last_max;
					System.out.println(i+"->"+temp);
//					AppendFile.append("gainResult", i+"->"+temp);
					if(temp > max){
						max = temp;
						selectedNode = i;
						last_max_gain = record[i];
					}
					initSet.remove(i);
				}
			}
			last_max_gain = -1;
			last_max = max;
			max = 0;
//			System.out.println("selectedNode---->"+selectedNode);
			initSet.add(selectedNode); 
			selectedNode = -1;
			System.out.println(initSet); 
			AppendFile.append(file2Store, "set size "+initSet.size()+"->"+initSet);
			write2DB(initSet,IMMethod.Greedy);
//			double temp = dm.diffusion(graph, initSet);
//			greedy[initSet.size()-1] = temp;
//			database.write2Database(initSet.size(),temp,initSet, dataName+"_greedy"+suffix);
			System.out.println("excute time ===>"+(System.currentTimeMillis()-a)/1000f+" s ");
		}
		System.out.println("opt initial set"+initSet);
		return greedy;
	}

	//根据度值的从大到小的顺序选择节点
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
			System.out.println("excute time ===>"+(System.currentTimeMillis()-a)/1000f+" s");
		}
		System.out.println(("opt initial set"+initSet));
		return highDegree;

	}

	private double[] neiborNumGreedy(Graph graph, DiffusionModel dm, int numOfNeibor) {
		NodeInfluenceAbilityEfficient nia = new NodeInfluenceAbilityEfficient(graph, numOfNeibor);
		double[] p = new double[numOfNeibor];
		for (int i = 0; i < p.length; i++) {
			p[i] = 1;
		}
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
//		nia.calculate();
		for (int i = 0; i < targetSize; i++) {
			int max_gain = nia.get_max_gain_node_efficient_CELF(initSet);
			initSet.add(max_gain);
			write2DB(initSet,IMMethod.NeiborNumGreedy);
			AppendFile.append(file2Store, "set size "+initSet.size()+"->"+initSet);
//			result[initSet.size()-1] = dm.diffusion(graph, initSet);
			System.out.println(initSet+"----->"+result[i]);
			
//			nia.refresh(max_gain,p);
//			database.write2Database(initSet.size(),result[initSet.size()-1],initSet, dataName+"_neiborNumGreedyECELF"+suffix);
		}
		return result;
	}
	
	public double[] neiborWeightNumGreedy(Graph graph,DiffusionModel dm,int numOfNeibor,double[] p,int targetSize){
		NodeInfluenceAbility nia = new NodeInfluenceAbility(graph, numOfNeibor);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
//		nia.calculate();
		for (int i = 0; i < targetSize; i++) {
			int max_gain = nia.get_max_gain_node(initSet,p);
			initSet.add(max_gain);
			result[initSet.size()-1] = dm.diffusion(graph, initSet);
//			System.out.println(initSet+"----->"+result[i]);
//			nia.refresh(max_gain,p);
			database.write2Database(initSet.size(),result[initSet.size()-1],initSet, dataName+"_neiborWeightNumGreedy"+suffix);
		}
		return result;
	}
	
	public double[] neiborWeightNumGreedyEfficient(Graph graph,DiffusionModel dm,int numOfNeibor,double[] p,int targetSize){
		NodeInfluenceAbilityEfficient nia = new NodeInfluenceAbilityEfficient(graph, numOfNeibor);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			int max_gain = nia.get_max_gain_node_efficient(initSet,p);
			initSet.add(max_gain);
//			result[initSet.size()-1] = dm.diffusion(graph, initSet);
//			System.out.println(initSet+"----->"+result[i]);
//			nia.refresh(max_gain,p);
//			database.write2Database(initSet.size(),result[initSet.size()-1],initSet, dataName+"_nWtNumGEfficientCELF"+suffix);
		}
		return result;
	}
	
	public double[] neiborWeightNumGreedyEfficientCelf(Graph graph,DiffusionModel dm,int numOfNeibor,double[] p){
		NodeInfluenceAbilityEfficient nia = new NodeInfluenceAbilityEfficient(graph, numOfNeibor);
		double[] result = new double[targetSize];
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			int max_gain = nia.get_max_gain_node_efficient_CELF(initSet,p);
			initSet.add(max_gain);
			write2DB(initSet,IMMethod.TLLFGreedy);
			AppendFile.append(file2Store, "set size "+initSet.size()+"->"+initSet);
//			result[initSet.size()-1] = dm.diffusion(graph, initSet);
			System.out.println("choose node----->"+max_gain);
//			database.write2Database(initSet.size(),result[initSet.size()-1],initSet, dataName+"_nWtNumGEfficientCELF"+suffix);
		}
		return result;
	}
	
	public double[] degreeDiscountIC(Graph graph,DiffusionModel dm,int numOfNeibor, int[] object){

		double[] degreeDiscount = new double[targetSize];
		int[] degree = new int[graph.size()];
		for (int i = 0; i < graph.size(); i++) {
			degree[i] = graph.getOutdegree(i);
		}
		Set initSet = new HashSet();
		for (int i = 0; i < targetSize; i++) {
			long a=System.currentTimeMillis();
			int max = Util.findMax(degree,initSet,0);
			System.out.println("maxdegree:"+max+"==>"+degree[max]);
//			Util.block();
			initSet.add(max);
			write2DB(initSet,IMMethod.SingleDiscount);
			AppendFile.append(file2Store, "set size "+initSet.size()+"->"+initSet);
//			degreeDiscount[i] = dm.diffusion(graph, initSet);
//			database.write2Database(initSet.size(),degreeDiscount[i],initSet, dataName+"_degreeDiscountIC"+suffix);
//			System.out.println(initSet+"  "+degreeDiscount[i]);
			Set<Integer> neibor = graph.getInNeighbor(max);
			for (Integer j : neibor) {
				degree[j] -- ;
			}
//			Print.print(degree);
//			System.out.println("excute time ===>"+(System.currentTimeMillis()-a)/1000f+" s ");
		}
		System.out.println(("opt initial set"+initSet));
		return degreeDiscount;

	}
	
	private double[] random(Graph gh, DiffusionModel dm) {
		Set<Integer> initSet = new HashSet<Integer>();
		double[] random = new double[targetSize];
		for (int i = 0; i < targetSize; i++) {
			long a=System.currentTimeMillis();
			int node = -1;
			while(initSet.contains(node = Util.random(0,gh.size()))){
			
			}
			initSet.add(node);
			System.out.println(initSet);
			write2DB(initSet,IMMethod.Random);
			AppendFile.append(file2Store, "set size "+initSet.size()+"->"+initSet);
//			random[i] = dm.diffusion(gh, initSet);
//			database.write2Database(initSet.size(),random[i],initSet, dataName+"_random"+suffix);
//			System.out.println("time ===>"+(System.currentTimeMillis()-a)/1000f+" s ");
		}
		return random;
	}
	
	public static void main(String[] args) throws MWException {
		 dataName = "ca_HepPh1";
		 file2Store = dataName+"log.txt";
		 double begin = System.currentTimeMillis();
		 String filePath = "/home/qinyadong/dataset/ccir2014/"+dataName+".txt";
		 String oneLine = FileOp.readFileOneLine(filePath);
		 String fenge = " ";
		if(oneLine.contains("\t"))fenge  = "\t";
		 ReadGraph rd = new ReadGraph();
		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2,fenge);
		 int executions = 20000;//icm中模拟次数
		 int set = 50;//集合大小
		 double[] p = new double[]{0.1,0.05,0.01};
		 ICM3MultiThread icm3 = new ICM3MultiThread(executions,8, p[0], p[1], p[2]);
		 DiffusionSimulate_test ds = new DiffusionSimulate_test(set);//参数为初始集合大小
		 DiffusionModel dm = icm3;//选择要使用的传播模型

		 suffix = Util.calSuffix(executions,set,p);
		 canWrite2DB  = database.createTables(dataName,"0",suffix,map);
		 
		 ds.recordGreedy(gh,dm,p);
		 ds.recordTLLFGreedy(gh,dm,p);
		 ds.recordSingleDiscount(gh,dm,p);
		 ds.recordRandom(gh,dm,p);
		 ds.recordNeiborNumGreedy(gh,dm,p);
		 
		 System.out.println("totalTime ===>"+(System.currentTimeMillis()-begin)/1000f+"seconds");
	}

	/**
	 * @param gh
	 * @param dm
	 * @param p 
	 */
	private void recordNeiborNumGreedy(Graph gh, DiffusionModel dm, double[] p) {
		 AppendFile.append(file2Store, Util.getCurrentTime()+"NeiborNumGreedy begin!");
		 double a = System.currentTimeMillis();
		 double[] NeiborNumGreedy = this.neiborNumGreedy(gh, dm, 3);
		 double b = System.currentTimeMillis();
		 AppendFile.append(file2Store, Util.getCurrentTime()+"NeiborNumGreedy spend->"+(b-a)/1000f+"seconds");

		
	}

	/**
	 * @param gh
	 * @param dm
	 * @param p 
	 */
	private void recordRandom(Graph gh, DiffusionModel dm, double[] p) {
		 AppendFile.append(file2Store, Util.getCurrentTime()+"random begin!");
		 double a = System.currentTimeMillis();
		 double[] random = this.random(gh, dm);
		 double b = System.currentTimeMillis();
		 AppendFile.append(file2Store, Util.getCurrentTime()+"random spend->"+(b-a)/1000f+"seconds");

		
	}

	/**
	 * @param gh
	 * @param dm
	 * @param p 
	 */
	private void recordSingleDiscount(Graph gh, DiffusionModel dm, double[] p) {
		 AppendFile.append(file2Store, Util.getCurrentTime()+"SingleDiscount begin!");
		 double a = System.currentTimeMillis();
		 double[] DegreeDiscount = this.degreeDiscountIC(gh, dm, 3, null);
		 double b = System.currentTimeMillis();
		 AppendFile.append(file2Store, Util.getCurrentTime()+"SingleDiscount spend->"+(b-a)/1000f+"seconds");

		
	}

	/**
	 * @param gh
	 * @param dm
	 * @param p 
	 */
	private void recordTLLFGreedy(Graph gh, DiffusionModel dm, double[] p) {
		 AppendFile.append(file2Store, Util.getCurrentTime()+"TLLFGreedy begin!");
		 double a = System.currentTimeMillis();
		 double[] TLLFGreedy = this.neiborWeightNumGreedyEfficientCelf(gh, dm, 3, p);
		 double b = System.currentTimeMillis();
		 AppendFile.append(file2Store, Util.getCurrentTime()+"TLLFGreedy spend->"+(b-a)/1000f+"seconds");

		
	}

	/**
	 * @param gh
	 * @param dm
	 * @param p 
	 */
	private void recordGreedy(Graph gh, DiffusionModel dm, double[] p) {
		 AppendFile.append(file2Store, Util.getCurrentTime()+"greedy begin!");
		 double a = System.currentTimeMillis();
		 double[] greedy = this.greedy(gh, dm);
		 double b = System.currentTimeMillis();
		 AppendFile.append(file2Store, Util.getCurrentTime()+"greedy spend->"+(b-a)/1000f+"seconds");

		
	}

	/**
	 * @param initSet
	 * @param greedy
	 */
	private void write2DB(Set initSet, IMMethod method) {
		if(canWrite2DB != 1)return;
		database.write2Database(initSet.size(),0,initSet,map.get(method));
		
	}

	
	

}
