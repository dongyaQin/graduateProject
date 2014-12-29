package sdu.ir.diffusionmodel;


import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Constant;
import sdu.ir.util.Util;
import test.Print;
//�����ԣ���ȷ��
public class ICM3MultiThread implements DiffusionModel{
	private double p1 = 0.5;//��Ⱦ�ھӵĸ���
	private double p2 = 0.5;//��Ⱦ�ھӵĸ���
	private double p3 = 0.5;//��Ⱦ�ھӵĸ���
	private int count = 10;//�̹߳���
	private int threads = 1;//�̲߳�����
	private double totalSum = 0;//�̹߳���
	private double[] result;
	//count��ָģ����ٴΣ���ΪICMģ����ÿ�ν����һ����������Ҫ��������ʹ�ý����׼ȷ
	public ICM3MultiThread(int count,int threads,double p1,double p2,double p3) {
		this.count = count;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.threads = threads;
		result = new double[threads];
		
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		for (int i = 0; i < result.length; i++) {
			result[i] = -1;
		}
		for (int i = 0; i < threads; i++) {
			new Thread(new TempThread(graph,initialSet,count/threads,i)).start();
		}
		while(resultIsOver()){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Print.print(result);
		}
//		System.out.println(totalSum+"����"+count);
//		System.out.println(count);
		double sum = Util.calArraySum(result);
		return sum/(double)count;
		
	}
	

	private boolean resultIsOver() {
		for (int i = 0; i < result.length; i++) {
			if(result[i] <= 0){
				return true;
			}
		}
		return false;
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
	
	public static void main(String[] args) {
//		 String filePath = "E:\\���ݼ�\\gh1.txt";
//		 String filePath = "E:\\���ݼ�\\temp.txt";
		String filePath = Constant.filePathWindows+"ca_HepPh1.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2);
//		Print.print(graph);System.out.println(graph.size());
		ICM3MultiThread icm = new ICM3MultiThread(20000,2,0.1,0.05,0.01);
		Set initSet = new HashSet();
		initSet.add(0);
		initSet.add(107);
		initSet.add(686);
//		initSet.add(1684);
//		initSet.add(1912);
//		initSet.add(3437);
//		initSet.add(414);
//		initSet.add(3830);
//		initSet.add(3980);
//		initSet.add(348);
//		initSet.add(2313);
//		initSet.add(698);
//		initSet.add(56);
		initSet.add(1505);
//		initSet.add(19);
//		initSet.add(25);
//		initSet.add(41);
//		initSet.add(3256);
//		initSet.add(3982);
		initSet.add(72);
		long a = System.currentTimeMillis();
		System.out.println(icm.diffusion(graph, initSet));
		long b = System.currentTimeMillis();
		System.out.println((b-a)/1000);
//		NetProperty net = new NetProperty(graph);
//		System.out.println(net.getNumOfDistance(1123, 10));
	}

	class TempThread implements Runnable{
		Graph graph;
		Set initialSet= new HashSet();
		private boolean[] activated ;
		private int record = 0;
		private int result_index;
		private int local_count;
		private double local_sum = 0;
		public TempThread(Graph graph, Set initialSet, int count, int i) {
			this.graph = graph;
			this.initialSet.addAll(initialSet);
			result_index = i;
			local_count = count;
		}

		@Override
		public void run() {
			Set temp = new HashSet();
			temp.addAll(initialSet);
			for (int i = 0; i < local_count; i++){
				//��ʼ������֮ǰ�ı������
				
				initialSet.removeAll(initialSet);
				initialSet.addAll(temp);
				initial(graph,initialSet);
				int a = spread(graph,initialSet);
				local_sum += a;
			}
			result[result_index] = local_sum;
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
		private int spread(Graph graph, Set<Integer> initialSet) {
			record = 0;
			Set newInfected = new HashSet();
			boolean haveNewInfected = true;
			while(haveNewInfected){
				haveNewInfected = false;
				record ++;
//				System.out.println("���ǵ�"+record+"����Ⱦ");
				for (Integer obj: initialSet) {  
					int i= obj; 
					Set<Integer> temp = graph.getNeighbor(i);
					if(temp != null){
						for (Integer in : temp) { 
							int i1= in;  
//									System.out.println("��ǰ�����ýڵ�"+i+"��Ⱦ-->"+i1);
							if(!activated[i1] && checkCanInfect(record)){
//										System.out.println("�ɹ���Ⱦ�ڵ�"+i1);
								newInfected.add(i1);
								activated[i1] = true;
								haveNewInfected = true;
							}else if(!activated[i1]){
//										System.out.println("��Ⱦ�ڵ�"+i1+"ʧ��");
							}else{
//										System.out.println(activated[i1]);
							}
							
						}
					}
				}
//				System.out.println("��"+record+"����Ⱦ����");
//				System.out.println("��Ⱦ��---��"+newInfected);
				initialSet.removeAll(initialSet);
				initialSet.addAll(newInfected);
				newInfected.removeAll(newInfected);
			}
			
			int out = 0;
			for (int i = 0; i < activated.length; i++) {
				if(activated[i])out++;
			}
//			Print.print(activated,1);
//			System.out.println("���ո�Ⱦ����"+out);
			return out;
		}
		
		
	}

}
