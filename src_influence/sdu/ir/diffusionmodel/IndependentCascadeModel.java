package sdu.ir.diffusionmodel;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import test.Print;

public class IndependentCascadeModel implements DiffusionModel{
	private double p = 0.5;//��Ⱦ�ھӵĸ���
	private boolean[] activated ;
	private int count = 10;
	//count��ָģ����ٴΣ���ΪICMģ����ÿ�ν����һ����������Ҫ��������ʹ�ý����׼ȷ
	public IndependentCascadeModel(int count,double p) {
		this.count = count;
		this.p = p;
	}
	
	public double diffusion(Graph graph,Set initialSet) {
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
		double out = (double)sum/(double)count;
		return out;
		
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
		while(haveNewInfected){
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
		for (int i = 0; i < activated.length; i++) {
			if(activated[i])out++;
		}
//		Print.print(activated,1);
//		System.out.println("���ո�Ⱦ����"+out);
		return out;
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
		 String filePath = "E:\\dataset\\ccir2014\\test.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2," ");
//		Print.print(graph);System.out.println(graph.size());
		IndependentCascadeModel icm = new IndependentCascadeModel(2000000,0.5);
		Set set = new HashSet();
		set.add(0);

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

}
