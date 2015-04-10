package sdu.ir.simulate;

import irlab.dbOperate.Data;

import java.util.Set;

import sdu.ir.diffusionmodel.ICM3MultiThread;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.NodeInfluenceAbility;
import sdu.ir.util.Util;
import test.Print;

public class SimilarityOfGreedyAndHeuristics {

	
	static Data database = new Data();
	static String dataName;
	public static void main(String[] args) {
		
		SimilarityOfGreedyAndHeuristics s = new SimilarityOfGreedyAndHeuristics();
		dataName = "email2";
		double begin = System.currentTimeMillis();
		String filePath = "E:\\dataset\\ccir2014\\"+dataName+".txt";
		ReadGraph rd = new ReadGraph();
		Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2," ");
		double[] p = new double[]{0.1,0.05,0.01};
//		database.createTables(dataName);
		s.begin(gh,50,p);
		System.out.println("totalTime ===>"+(System.currentTimeMillis()-begin)/1000f+"seconds");
	}

	private void begin(Graph g,int k,double[] p) {
		
		
		double[] greedy = new double[k];
		double[] lbm = new double[k];
//		double[] ubm = new double[k];
		ICM3MultiThread icm3 = new ICM3MultiThread(20000,1, p[0], p[1], p[2]);
		NodeInfluenceAbility nb = new NodeInfluenceAbility(g, k);
		double time_greedy = 0;
		double time_lbm = 0;
		double time_ubm = 0;
		for (int i = 0; i <k; i++) {
			System.out.println((i+1)+"nodes");
			Set<Integer> set = Util.random(0,g.size()-1,i+1);
			double a = System.currentTimeMillis();
			greedy[i] = icm3.diffusion(g, set);
			double b = System.currentTimeMillis();
			System.out.println("greedy"+(b-a)/1000);
//			database.write2Database(set.size(),greedy[i],set, dataName+"_greedy");
			time_greedy += b-a;
			double c = System.currentTimeMillis();
			lbm[i] = nb.influenceSimulate(set, p);
			double d = System.currentTimeMillis();
			System.out.println("lbm"+(d-c)/1000);
//			database.write2Database(set.size(),lbm[i],set, dataName+"_lbm");
			time_lbm += d-c;
//			double e = System.currentTimeMillis();
//			ubm[i] = nb.influenceSimulate(set, new double[]{1,1,1});
//			double f = System.currentTimeMillis();
//			System.out.println("ubm"+(f-e)/1000);
//			database.write2Database(set.size(),ubm[i],set, dataName+"_ubm");
//			time_ubm += f-e;
		}
		Print.print(greedy);       
		Print.print(lbm);     
//		Print.print(ubm);
		System.out.println("greedy total time->"+time_greedy);
		System.out.println("lbm total time->"+time_lbm);
//		System.out.println("ubm total time->"+time_ubm);
	}

}
