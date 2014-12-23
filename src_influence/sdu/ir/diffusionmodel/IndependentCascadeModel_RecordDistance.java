package sdu.ir.diffusionmodel;

import io.AppendFile;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.PropagationProbability;
import sdu.ir.util.Util;
import text.Print;

public class IndependentCascadeModel_RecordDistance implements DiffusionModel{
	private PropagationProbability pp;
	private double p = 0.5;//��Ⱦ�ھӵĸ���
	private double[] ps = new double[]{0.1,0.01,0.001};//Random�еĸ���
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
	private double[] record = new double[100];
	//count��ָģ����ٴΣ���ΪICMģ����ÿ�ν����һ����������Ҫ��������ʹ�ý����׼ȷ
	public IndependentCascadeModel_RecordDistance(int count,double p,PropagationProbability pp) {
		this.count = count;
		this.p = p;
		this.pp = pp;
	}
	
	private void initialRecordArray(Graph graph) {		
		for (int i = 0; i < record.length; i++) {
			record[i] = 0;
		}
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		if(pp==PropagationProbability.InDegree){
			initialIndegreeArray(graph);
		}
		initialRecordArray(graph);
		Set temp = new HashSet();
		temp.addAll(initialSet);
		for (int i = 0; i < count; i++) {
//			System.out.println("��"+(i+1)+"�β���-------------------------");
			//��ʼ������֮ǰ�ı������	
			initialSet.removeAll(initialSet);
			initialSet.addAll(temp);
			initial(graph,initialSet);
			spread(graph,initialSet);
		}
		//�ָ�ԭ����initialSet���ⷢ������Ҫ�Ĵ���
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

	//�ݹ����ICM����ģ�ʹ���
	private int spread(Graph graph, Set initialSet) {
		Set newInfected = new HashSet();
		boolean haveNewInfected = true;
		int distance = 0;
		double totalDistance = 0;
		int totalInfluence = 0;
		while(haveNewInfected){
//			System.out.print(initialSet.size()+",");
//			System.err.println("�Ѿ���Ⱦ����"+distance+"��������Ϊ--��"+initialSet.size());
			totalDistance += distance*initialSet.size();
			totalInfluence += initialSet.size();
			record[distance+3] += initialSet.size();
			distance++;
			haveNewInfected = false;
			for (Object obj: initialSet) {  
				if(obj instanceof Integer){  
					int i= (Integer)obj; 
//					System.out.println("��ǰ�����ýڵ�"+i+"��Ⱦ������");
					Set temp = graph.getNeighbor(i);
					if(temp != null){
						for (Object obj1 : temp) {
							if(obj1 instanceof Integer){  
								int i1= (Integer)obj1;  
								if(!activated[i1] && checkCanInfect(i,i1)){
//									System.out.println("�ɹ���Ⱦ�ڵ�"+i1);
									newInfected.add(i1);
									activated[i1] = true;
									haveNewInfected = true;
								}else if(!activated[i1]){
//									System.out.println("��Ⱦ�ڵ�"+i1+"ʧ��");
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
//		 String filePath = "E:\\���ݼ�\\temp.txt";
		 String filePath = "E:\\dataset\\ccir2014\\email2.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2," ");
//		Print.print(graph);System.out.println(graph.size());
		IndependentCascadeModel_RecordDistance icm = new IndependentCascadeModel_RecordDistance(2000,0.1,PropagationProbability.Constant);
		Set set = new HashSet();
//		set.add(4);
		int[] temp = new int[]{3481,112659, 217467,54846};
		for (int i = 0; i < 1000; i++) {
			set.add(i);
			icm.diffusion(graph, set);
			set.remove(i);
//			Util.block();
		}
		System.out.println(set.size());
//		double a = System.currentTimeMillis();
//		double x = icm.diffusion(graph, set);
//		double b = System.currentTimeMillis();
		
//		System.out.println(x);
//		System.out.println((b-a)/1000+"S");
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
