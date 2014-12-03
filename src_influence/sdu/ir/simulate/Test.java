package sdu.ir.simulate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sdu.ir.diffusionmodel.ICM3MultiThread;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.NetProperty;
import sdu.ir.util.Util;
import text.Print;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		 String dataName = "gyjs";
//		 double begin = System.currentTimeMillis();
////		 String filePath = "E:\\software\\数据集\\度最大是否传播最多.txt";
//		 String filePath = "G:\\研究僧\\数据集\\数据集\\"+dataName+".txt";
//		 ReadGraph rd = new ReadGraph();
//		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2);
//		 int executions = 4000;//icm中模拟次数
//		 int set = 2;//集合大小
//		 double threshold = 0.3;
////		 double p = 0.5;
//		 double[] p = new double[]{0.5,0.3,0.1};
//		 ICM3MultiThread icm3m = new ICM3MultiThread(executions, 2, p[0], p[1], p[2]);
//		 Set initSet = new HashSet();
//		 NetProperty np = new NetProperty(gh);
//		 double[][] temp = np.getNeiborWeightNumber(3, p);
//		 System.out.println(gh.getNeighbor(14));
//		 for (int i = 0; i < temp.length; i++) {
//			initSet.add((int)temp[i][0]);
//			System.out.println(initSet);
//			System.out.println(icm3m.diffusion(gh, initSet)+" "+temp[i][1]);
//			initSet.remove((int)temp[i][0]);
//			Util.sleep(300);
//		}
////		 System.out.println(icm3m.diffusion(gh, initSet));
//		System.out.println(1-Math.pow(0.85, 9));
//		for (int i = 0; i < 50; i++) {
//			if(i%5 == 0)
//				System.out.print("'"+(i)+"',");
//			else
//				System.out.print("'',");
//		}
		int[] a = new int[]{136, 548, 9, 133, 13, 15, 134, 395, 563, 20, 23, 22, 307, 40, 41, 51, 48, 426, 428, 57, 182, 68, 611, 71, 340, 203, 197, 72, 467, 195, 75, 459, 218, 321, 92, 332, 331, 577, 236, 232, 589, 104, 105, 119, 597, 353, 354, 355, 240, 482};
		int[] b = new int[]{136, 548, 9, 133, 13, 15, 134, 395, 563, 20, 23, 22, 307, 40, 41, 51, 426, 428, 57, 182, 68, 204, 611, 71, 340, 203, 197, 72, 467, 195, 75, 459, 218, 92, 332, 331, 756, 577, 236, 232, 503, 589, 104, 105, 597, 353, 354, 355, 240, 482};
		Arrays.sort(a);
		Arrays.sort(b);
		Print.print(a);
		Print.print(b);
		for (int i = 0; i < b.length; i++) {
			if(a[i] != b[i]){
				System.out.println(false);
				return;
			}
		}
		System.out.println(true);
	}

}
