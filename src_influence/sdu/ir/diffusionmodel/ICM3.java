package sdu.ir.diffusionmodel;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.NetProperty;
import test.Print;
//经测试，正确！
public class ICM3 implements DiffusionModel{
	private double p1 = 0.5;//感染邻居的概率
	private double p2 = 0.5;//感染邻居的概率
	private double p3 = 0.5;//感染邻居的概率
	private boolean[] activated ;
	private int record = 0;
	private int count = 10;
	private double[] record_p;
	//count是指模拟多少次，因为ICM模型下每次结果不一样，所以需要试验多次以使得结果更准确
	public ICM3(int count,double p1,double p2,double p3) {
		this.count = count;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public double diffusion(Graph graph,Set initialSet) {
		record_p = new double[graph.size()];
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
		for (int i = 0; i < record_p.length; i++) {
			if(record_p[i] > 0){
				System.out.println(i+"--->"+record_p[i]/(double)count);
			}
		}
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
		record = 0;
		Set newInfected = new HashSet();
		boolean haveNewInfected = true;
		while(haveNewInfected){
			haveNewInfected = false;
			record ++;
//			System.out.println("这是第"+record+"步感染");
			for (Object obj: initialSet) {  
				if(obj instanceof Integer){  
					int i= (Integer)obj; 
					Set temp = graph.getNeighbor(i);
					if(temp != null){
						for (Object obj1 : temp) {
							if(obj1 instanceof Integer){  
								int i1= (Integer)obj1;  
//								System.out.println("当前正在用节点"+i+"感染-->"+i1);
								if(!activated[i1] && checkCanInfect(record)){
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
//			System.out.println("第"+record+"步感染结束");
//			System.out.println("感染了---》"+newInfected);
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
//		Print.print(activated,1);
//		System.out.println("最终感染个数"+out);
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
//		System.out.print("当前的感染概率为"+p);
		if(Math.random() <= p)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
//		 String filePath = "E:\\数据集\\gh1.txt";
//		 String filePath = "E:\\数据集\\temp.txt";
		 String filePath = "G:\\研究僧\\数据集\\数据集\\gh3.txt";
		ReadGraph rd = new ReadGraph();
//		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithoutweight",2);
		Print.print(graph);System.out.println(graph.size());
		ICM3 icm = new ICM3(10000000,0.5,0.5,0);
		Set set = new HashSet();
//		set.add(1);
//		set.add(2);
//		set.add(3);
//		set.add(4);
//		set.add(5);
//		set.add(6);
		set.add(1);
//		set.add(333);
//		set.add(8);
//		set.add(9);
		System.out.println(icm.diffusion(graph, set));
		NetProperty net = new NetProperty(graph);
//		System.out.println(net.getNumOfDistance(1123, 10));
	}

}
