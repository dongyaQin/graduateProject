package sdu.ir.diffusion_distance;

import io.AppendFile;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.diffusionmodel.IndependentCascadeModel_RecordDistance;
import sdu.ir.diffusionmodel.IndependentCascadeModel_RecordDistance.Node;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.simulate.DiffusionSimulate_test;
import sdu.ir.util.Util;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dataName = "EmailEuAll";
		double begin = System.currentTimeMillis();
		 String filePath = "E:\\dataset\\ccir2014\\"+dataName+".txt";
		 ReadGraph rd = new ReadGraph();
		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2,"\t");
//		 Print.print(gh);
//		 Util.block();
		 int executions = 2000;//icm中模拟次数
		 int set = 50;//集合大小
		 double[] p = new double[]{0.5,0.3,0.1};
		 IndependentCascadeModel_RecordDistance icm = new IndependentCascadeModel_RecordDistance(executions,0.05);
		 DiffusionSimulate_test ds = new DiffusionSimulate_test(set);//参数为初始集合大小
		 DiffusionModel dm = icm;//选择要使用的传播模型
		 Set set1 = new HashSet();
//		 int i = 875;
		 double pp = 0.01;
//		 while(pp < 0.2){
			 icm.setDiffusionProbability(pp);
			 StringBuffer sb = new StringBuffer();
			 for (int i = 20000; i < 30000; i++) {
				 set1.add(i);
				 Node node = icm.diffusion1(gh, set1);
				 System.out.println(i+"-->"+node.num_infect+"==>"+node.distance);
				 set1.remove(i);
				 sb.append(node.num_infect+" "+node.distance+"\n");
			}
			AppendFile.append("icm_apd/"+dataName+pp+".txt", sb.toString());
			System.out.println("totalTime ===>"+(System.currentTimeMillis()-begin)/1000f+"seconds");	
			Util.block("输入。。。");
			pp = pp + 0.02;
//		 }
		 
		 System.out.println("totalTime ===>"+(System.currentTimeMillis()-begin)/1000f+"seconds");
	

	}

}
