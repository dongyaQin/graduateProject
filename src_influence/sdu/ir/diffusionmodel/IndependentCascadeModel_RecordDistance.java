package sdu.ir.diffusionmodel;

import io.AppendFile;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Constant;
import sdu.ir.util.PropagationProbability;
import sdu.ir.util.Util;
import test.Print;

public class IndependentCascadeModel_RecordDistance implements DiffusionModel{
	private PropagationProbability pp;
	private double p = 0.5;//感染邻居的概率
	private double[] ps = new double[]{0.1,0.01,0.001};//Random中的概率
	private boolean[] activated ;
	private int count = 1000;
	private double[] inDegree;//diffusion probab equals to 1/indegree(v)
	/*record is an array which record the result of this time of 
	Monte-Calo simulation,the content of the array is below:
		index 0:sum of diffusion distance;
		index 1:sum of APD(average propagation distance);
		index 2:sum of number of nodes infected;
		others:number of nodes infected at step i+1
	*/
	private double[] record = new double[1000];
	//count是指模拟多少次，因为ICM模型下每次结果不一样，所以需要试验多次以使得结果更准确
	public IndependentCascadeModel_RecordDistance(int count,double p,PropagationProbability pp) {
		this.count = count;
		this.p = p;
		this.pp = pp;
	}
	
	private void initialRecordArray() {		
		for (int i = 0; i < record.length; i++) {
			record[i] = 0;
		}
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		if(pp==PropagationProbability.InDegree){
			initialIndegreeArray(graph);
		}
		initialRecordArray();
		Set temp = new HashSet();
		temp.addAll(initialSet);
		for (int i = 0; i < count; i++) {
//			System.out.println("第"+(i+1)+"次测试-------------------------");
			//初始化传播之前的标记数组	
			initialSet.removeAll(initialSet);
			initialSet.addAll(temp);
			initial(graph,initialSet);
			spread(graph,initialSet);
		}
		//恢复原来的initialSet以免发生不必要的错误
		initialSet.removeAll(initialSet);
		initialSet.addAll(temp);
		return -1;
		
	}
	/**
	 * @param graph
	 */
	private void initialIndegreeArray(Graph graph) {
		if(inDegree == null){
			inDegree = new double[graph.size()];
			for (int i = 0; i < inDegree.length; i++) {
				inDegree[i] = 1/(double)graph.getIndegree(i);
			}
		}
		
	}

	private void initial(Graph graph, Set initialSet) {
		if(activated==null){
			activated = new boolean[graph.size()+1];
		}else{
			for (int i = 0; i < activated.length; i++) {
				activated[i] = false;
			}
		}
		for (Object obj: initialSet) {  
		      if(obj instanceof Integer){  
		    	  int aa= (Integer)obj;  
		    	  activated[aa] = true;
		      }   
		}
		
	}

	//递归的用ICM传播模型传播
	private int spread(Graph graph, Set initialSet) {
		Set newInfected = new HashSet();
		boolean haveNewInfected = true;
		int distance = 0;
		double totalDistance = 0;
		int totalInfluence = 0;
		while(haveNewInfected){
//			System.out.print(initialSet.size()+",");
//			System.err.println("已经感染到第"+distance+"步，数量为--》"+initialSet.size());
			totalDistance += distance*initialSet.size();
			totalInfluence += initialSet.size();
			record[distance+3] += initialSet.size();
			distance++;
			haveNewInfected = false;
			for (Object obj: initialSet) {  
				if(obj instanceof Integer){  
					int i= (Integer)obj; 
//					System.out.println("当前正在用节点"+i+"感染。。。");
					Set temp = graph.getNeighbor(i);
					if(temp != null){
						for (Object obj1 : temp) {
							if(obj1 instanceof Integer){  
								int i1= (Integer)obj1;  
								if(!activated[i1] && checkCanInfect(i,i1)){
//									System.out.println("成功感染节点"+i1);
									newInfected.add(i1);
									activated[i1] = true;
									haveNewInfected = true;
								}else if(!activated[i1]){
//									System.out.println("感染节点"+i1+"失败");
								}else{
//									System.out.println(activated[i1]);
								}
							}
						}
					}
				
				}   
			}
			initialSet.removeAll(initialSet);
			initialSet.addAll(newInfected);
			newInfected.removeAll(newInfected);
		}
		record[0] += distance - 1;
		record[1] += totalDistance/totalInfluence;
		record[2] += totalInfluence;
		return totalInfluence;
	}

	private boolean checkCanInfect(int i, int j) {
		switch (pp) {
		case Constant:
			if(Math.random() <= p)
				return true;
			else
				return false;
		case InDegree:
			if(Math.random() <= inDegree[j])
				return true;
			else
				return false;
		case Random:
			int a = Util.random(0, 2);
			if(Math.random() <= ps[a])
				return true;
			else
				return false;
		}
		return false;
		
	}
	
	public static void main(String[] args) {
//		 String filePath = "E:\\数据集\\temp.txt";
		 String filePath = Constant.filePathWindows+"EmailEuAll.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2,"\t");
//		Print.print(graph);System.out.println(graph.size());
		IndependentCascadeModel_RecordDistance icm = new IndependentCascadeModel_RecordDistance(1000,0.02,PropagationProbability.Constant);
		Set set = new HashSet();
		
		
		int[] degree = new int[graph.size()];
		for (int i = 0; i < graph.size(); i++) {
			degree[i] = graph.getOutdegree(i);
		}
//		Print.print(degree);
		Set initSet = new HashSet();
		for (int i = 0; i < 30; i++) {
			long a=System.currentTimeMillis();
			int max = Util.findMaxIndex(degree,0);
			set.add(max);
			icm.diffusion(graph, set);
			double[] t = icm.getRecord();
			for (int j = 0; j < t.length; j++) {
				System.out.print(new java.text.DecimalFormat("#0.##").format(t[j]/1000)+" ");
			}
			System.out.println();
			set.remove(max);
			degree[max] = -1;
		}
		

		System.out.println(set.size());
	}

	public void setDiffusionProbability(double pp) {
		this.p = pp;
		
	}
	public double[] getRecord() {
		return record;
	}
	public void setPp(PropagationProbability pp) {
		this.pp = pp;
	}

}
