package sdu.ir.diffusionmodel;

import io.AppendFile;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Constant;
import sdu.ir.util.NetProperty;
import sdu.ir.util.Util;
import test.Print;
//�����ԣ���ȷ��
public class ICM3_RecordDistance implements DiffusionModel{
	private double p1 = 0.5;//��Ⱦ�ھӵĸ���
	private double p2 = 0.5;//��Ⱦ�ھӵĸ���
	private double p3 = 0.5;//��Ⱦ�ھӵĸ���
	private boolean[] activated ;
	private int record = 0;
	private int count = 10;
	private double[] record_p;
	private double[] records = new double[100];


	//count��ָģ����ٴΣ���ΪICMģ����ÿ�ν����һ����������Ҫ��������ʹ�ý����׼ȷ
	public ICM3_RecordDistance(int count,double p1,double p2,double p3) {
		this.count = count;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		initialRecordArray();
		record_p = new double[graph.size()];
		Set temp = new HashSet();
		temp.addAll(initialSet);
		int sum = 0;
		for (int i = 0; i < count; i++) {
//			System.out.println("��"+(i+1)+"�β���-------------------------");
			//��ʼ������֮ǰ�ı������
			
			initialSet.removeAll(initialSet);
			initialSet.addAll(temp);
			initial(graph,initialSet);
//			Print.print(initialSet);
			int a = spread(graph,initialSet);
//			System.out.println(a);
			sum += a;
//			System.out.println("��"+(i+1)+"�β��Խ��"+a);
//			System.out.println();
		}
		//�ָ�ԭ����initialSet���ⷢ������Ҫ�Ĵ���
		initialSet.removeAll(initialSet);
		initialSet.addAll(temp);
//		for (int i = 0; i < record_p.length; i++) {
//			if(record_p[i] > 0){
//				System.out.println(i+"--->"+record_p[i]/(double)count);
//			}
//		}
		double out = (double)sum/(double)count;
		return out;
		
	}
	
	private void initialRecordArray() {		
		for (int i = 0; i < records.length; i++) {
			records[i] = 0;
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
		record = 0;
		Set newInfected = new HashSet();
		boolean haveNewInfected = true;
		while(haveNewInfected){
			haveNewInfected = false;
			records[record+3] += initialSet.size();
			record ++;
//			System.out.println("���ǵ�"+record+"����Ⱦ");
			for (Object obj: initialSet) {  
				if(obj instanceof Integer){  
					int i= (Integer)obj; 
					Set temp = graph.getNeighbor(i);
					if(temp != null){
						for (Object obj1 : temp) {
							if(obj1 instanceof Integer){  
								int i1= (Integer)obj1;  
//								System.out.println("��ǰ�����ýڵ�"+i+"��Ⱦ-->"+i1);
								if(!activated[i1] && checkCanInfect(record)){
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
//			System.out.println("��"+record+"����Ⱦ����");
//			System.out.println("��Ⱦ��---��"+newInfected);
			initialSet.removeAll(initialSet);
			initialSet.addAll(newInfected);
			newInfected.removeAll(newInfected);
		}
		
		int out = 0;
		for (int i = 0; i < activated.length; i++) {
			if(activated[i]){
				out++;
				record_p[i] ++;
			}
		}
		records[2] += out;
//		Print.print(activated,1);
//		System.out.println("���ո�Ⱦ����"+out);
		return out;
	}

	private boolean checkCanInfect(int r) {
		
		double p = 0;
		if(r == 1){
			p = p1;
		}else if(r == 2){
			p = p2;
		}else if(r == 3){
			p = p3;
		}
//		System.out.print("��ǰ�ĸ�Ⱦ����Ϊ"+p);
		if(Math.random() <= p)
			return true;
		else
			return false;
	}
	
	public double[] getRecords() {
		return records;
	}
	
	
	public static void main(String[] args) {
//		 String filePath = "E:\\���ݼ�\\gh1.txt";
//		 String filePath = "E:\\���ݼ�\\temp.txt";

		String newFileName = "ca_HepPh1";
		String filePath = Constant.filePathWindows+newFileName+".txt";
		ReadGraph rd = new ReadGraph();
		Graph gh = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2," ");
		int executions = 2000;
		ICM3_RecordDistance icm = new ICM3_RecordDistance(executions,0.1,0.05,0.01);
		Set<Integer> seedSet = new HashSet<Integer>();
		Set<Integer> testedSet = new HashSet<Integer>();
		int number = gh.size();
		if(gh.size()>1000)
			 number = 1000;
		Util.randoms(number,0,gh.size()-1,testedSet);
		for (Integer in : testedSet) {
			System.out.println(newFileName+"-node-"+in);
			seedSet.add(in);
			icm.diffusion(gh, seedSet);
			recordResult(icm.getRecords(),newFileName+".txt",executions);
			seedSet.remove(in);
		}
		
//		System.out.println(net.getNumOfDistance(1123, 10));
	}
	
	private static void recordResult(double[] record,String fileName, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < record.length; i++) {
			sb.append(record[i]/count+" ");
		}
		AppendFile.append(fileName, sb.toString());
	}


}
