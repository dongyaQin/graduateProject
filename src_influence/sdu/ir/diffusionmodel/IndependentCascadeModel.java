package sdu.ir.diffusionmodel;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import test.Print;

public class IndependentCascadeModel implements DiffusionModel{
	private double p = 0.5;//感染邻居的概率
	private boolean[] activated ;
	private int count = 10;
	//count是指模拟多少次，因为ICM模型下每次结果不一样，所以需要试验多次以使得结果更准确
	public IndependentCascadeModel(int count,double p) {
		this.count = count;
		this.p = p;
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		Set temp = new HashSet();
		temp.addAll(initialSet);
		int sum = 0;
		for (int i = 0; i < count; i++) {
//			System.out.println("第"+(i+1)+"次测试-------------------------");
			//初始化传播之前的标记数组
			
			initialSet.removeAll(initialSet);
			initialSet.addAll(temp);
			initial(graph,initialSet);
//			Print.print(initialSet);
			int a = spread(graph,initialSet);
//			System.out.println(a);
			sum += a;
//			System.out.println("第"+(i+1)+"次测试结果"+a);
//			System.out.println();
		}
		//恢复原来的initialSet以免发生不必要的错误
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

	//递归的用ICM传播模型传播
	private int spread(Graph graph, Set initialSet) {
		Set newInfected = new HashSet();
		boolean haveNewInfected = true;
		while(haveNewInfected){
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
								if(!activated[i1] && checkCanInfect()){
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
		
		int out = 0;
		for (int i = 0; i < activated.length; i++) {
			if(activated[i])out++;
		}
//		Print.print(activated,1);
//		System.out.println("最终感染个数"+out);
		return out;
	}

	private boolean checkCanInfect() {
		if(Math.random() <= p)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
//		 String filePath = "E:\\数据集\\gh1.txt";
//		 String filePath = "E:\\数据集\\temp.txt";
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
