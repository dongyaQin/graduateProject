package sdu.ir.diffusionmodel;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import text.Print;

public class IndependentCascadeModel_RecordDistance implements DiffusionModel{
	private double p = 0.5;//��Ⱦ�ھӵĸ���
	private boolean[] activated ;
	private int count = 1000;
	public class Node{
		public Node(int i, int j) {
			this.num_infect = i;
			this.distance = j;
		}
		public double num_infect;
		public double distance;
		public void add(Node a) {
			this.num_infect += a.num_infect;
			this.distance += a.distance;
			
		}
	}
	//count��ָģ����ٴΣ���ΪICMģ����ÿ�ν����һ����������Ҫ��������ʹ�ý����׼ȷ
	public IndependentCascadeModel_RecordDistance(int count,double p) {
		this.count = count;
		this.p = p;
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		return 0;
	}
	
	public Node diffusion1(Graph graph,Set initialSet) {
		Set temp = new HashSet();
		temp.addAll(initialSet);
		Node sum = new Node(0,0);
		for (int i = 0; i < count; i++) {
//			System.out.println("��"+(i+1)+"�β���-------------------------");
			//��ʼ������֮ǰ�ı������
			
			initialSet.removeAll(initialSet);
			initialSet.addAll(temp);
			initial(graph,initialSet);
//			Print.print(initialSet);
			Node a = spread(graph,initialSet);
//			System.out.println("==========================");
			sum.add(a);
//			System.out.println("��"+(i+1)+"�β��Խ��"+a);
//			System.out.println();
		}
		//�ָ�ԭ����initialSet���ⷢ������Ҫ�Ĵ���
		initialSet.removeAll(initialSet);
		initialSet.addAll(temp);
		sum.distance = sum.distance/(double)count;
		sum.num_infect = sum.num_infect/(double)count;
		return sum;
		
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
	private Node spread(Graph graph, Set initialSet) {
		Set newInfected = new HashSet();
		boolean haveNewInfected = true;
		int distance = 0;
		double totalDistance = 0;
		while(haveNewInfected){
//			System.out.print(initialSet.size()+",");
//			System.err.println("�Ѿ���Ⱦ����"+distance+"��������Ϊ--��"+initialSet.size());
			totalDistance += distance*initialSet.size();
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
								if(!activated[i1] && checkCanInfect()){
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
		int out = 0;
		Node re = new Node(0,0);
		for (int i = 0; i < activated.length; i++) {
			if(activated[i])out++;
		}
//		Print.print(activated,1);
//		System.out.println(totalDistance+"\t"+out);
		double average_distance = totalDistance/(double)out;
//		System.out.println("���ո�Ⱦ����"+out+"\t���յ�ƽ����������->"+totalDistance/out);
		re.distance = average_distance;
//		System.out.println("average_distance-->"+average_distance);
		re.num_infect = out;
//		System.out.println();
		return re;
	}

	private boolean checkCanInfect() {
		if(Math.random() <= p)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
//		 String filePath = "E:\\���ݼ�\\gh1.txt";
//		 String filePath = "E:\\���ݼ�\\temp.txt";
		 String filePath = "ccir2014\\email2.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2," ");
//		Print.print(graph);System.out.println(graph.size());
		IndependentCascadeModel_RecordDistance icm = new IndependentCascadeModel_RecordDistance(200000,0.3);
		Set set = new HashSet();
//		set.add(4);
		int[] temp = new int[]{3481,112659, 217467,54846};
		for (int i = 0; i < 1; i++) {
			set.add(1);
			Node x = icm.diffusion1(graph, set);
			System.out.println((i+1)+"=>"+x);
			set.remove(i);
		}
		System.out.println(set.size());
//		set.add(2);
//		set.add(3);
//		set.add(4);
//		set.add(5);
//		set.add(6);
//		set.add(105);
//		set.add(333);
//		set.add(8);
//		set.add(9);
		double a = System.currentTimeMillis();
		double x = icm.diffusion(graph, set);
		double b = System.currentTimeMillis();
		
		System.out.println(x);
		System.out.println((b-a)/1000+"S");
	}

	public void setDiffusionProbability(double pp) {
		this.p = pp;
		
	}

}
