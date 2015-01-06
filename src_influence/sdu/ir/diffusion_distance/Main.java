package sdu.ir.diffusion_distance;

import io.AppendFile;
import io.FileOp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import sdu.ir.diffusionmodel.IndependentCascadeModel_RecordDistance;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import sdu.ir.simulate.DiffusionSimulate_test;
import sdu.ir.util.Constant;
import sdu.ir.util.PropagationProbability;
import sdu.ir.util.Util;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dataName = "dealedsocpokec";
		String fenge = " ";
		double begin = System.currentTimeMillis();
//		 String filePath = "/home/qinyadong/dataset/ccir2014/"+dataName+".txt";
		 String filePath = Constant.filePathLinux+dataName+".txt";
		 String oneLine = FileOp.readFileOneLine(filePath);
		 if(oneLine.contains("\t"))fenge = "\t";
		 ReadGraph rd = new ReadGraph();
		 Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2,fenge);
//		 Print.print(gh);
//		 Util.block();
		 int executions = 8000;//icm中模拟次数
		 int set = 50;//集合大小

		 IndependentCascadeModel_RecordDistance icm = new IndependentCascadeModel_RecordDistance(executions,0.01, PropagationProbability.Constant);
		 DiffusionSimulate_test ds = new DiffusionSimulate_test(set);//参数为初始集合大小
		 DiffusionModel dm = icm;//选择要使用的传播模型
		 int number = gh.size();
		 Set<Integer> seedSet = new HashSet<Integer>();
		 Set<Integer> testedSet = new HashSet<Integer>();
//		 Util.randoms(number,0,gh.size()-1,testedSet);
//		 testedSet.add(outdegrees[gh.size()-1][1]);
		 int[][] outdegrees = getOutDegrees(gh);
		int j = 0;
		for (int i = outdegrees.length-1; i > 0 ; i--) {
			System.out.println(outdegrees[i][0]+" "+outdegrees[i][1]);
			testedSet.add(outdegrees[i][1]);
			j ++;
			if(j == 1000)break;
		}
		icm.setPp(PropagationProbability.Constant);
		double[] ps = new double[]{0.015};
		for (int i = 0; i < ps.length; i++) {
			icm.setDiffusionProbability(ps[i]);
			for (Integer in : testedSet) {
				seedSet.add(in);
				icm.diffusion(gh, seedSet);
				recordResult(icm.getRecord(),dataName+ps[i],executions);
				seedSet.remove(in);
			}
		}
		
		 
		 System.out.println("totalTime ===>"+(System.currentTimeMillis()-begin)/1000f+"seconds");
	

	}

	/**
	 * @param gh
	 * @return
	 */
	private static int[][] getOutDegrees(Graph graph) {
		int[][] degree = new int[graph.size()][2];
		for (int i = 0; i < graph.size(); i++) {
			degree[i][0] = graph.getOutdegree(i);
			degree[i][1] = i;
		}
		Arrays.sort(degree, new Comparator<int[]>() {

			@Override
			public int compare(int[] o1, int[] o2) {
				if(o1[0]<o2[0])
					return -1;
				if(o1[0]>o2[0])
					return 1;
				return 0;
			}
		});
		return degree;
	}

	/**
	 * @param dataName
	 * @param pw
	 * @param pp
	 * @return
	 */
	private static String getFileName(String dataName,
			PropagationProbability pw, double pp) {
		switch (pw) {
		case Constant:
			return dataName+pw+pp;
		case InDegree:
			return dataName+pw;
		case Random:
			return dataName+pw;
		}
		return null;
	}

	/**
	 * 
	 */
	private static void recordResult(double[] record,String fileName, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < record.length; i++) {
			sb.append(record[i]/count+" ");
		}
		AppendFile.append(fileName, sb.toString());
	}

}
