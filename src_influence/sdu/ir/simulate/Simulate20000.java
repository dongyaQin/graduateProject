package sdu.ir.simulate;

import irlab.dbOperate.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import sdu.ir.diffusionmodel.ICM3MultiThread;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Util;
import text.Print;

public class Simulate20000 {
	
	static String[] tables = new String[]{"_greedy","_nWtNumGEfficient","_nWtNumGEfficientcelf",
		"_neiborNumGreedyEcelf","_degreeDiscountIC","_random"};
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Data data = new Data();
		String dataName = "ca_HepPh1";
		double begin = System.currentTimeMillis();
		String filePath = "ccir2014\\"+dataName+".txt";
		ReadGraph rd = new ReadGraph();
		Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2," ");
		int executions = 4000;//icm中模拟次数
		int set = 50;//集合大小
		double[] p = new double[]{0.3,0.1,0.01};
		ICM3MultiThread icm3 = new ICM3MultiThread(20000,2, p[0], p[1], p[2]);
		String suffix = Util.calSuffix(executions,set,p);
		for (int i = 0; i < tables.length; i++) {
			String tableName = dataName+tables[i]+suffix;
			System.out.println(tables[i]+"------");
			data.createTable(20000+tableName);
			ArrayList<Set<Integer>> list = data.getSeedSet(tableName);
			for (int j = 0; j < list.size(); j++) {
				System.out.println(list.get(j));
			}
			Util.block();
			for (int j = 0; j < list.size(); j++) {
				Set<Integer> set1 = list.get(j);
				double temp = icm3.diffusion(gh, set1);
				System.out.println(set1.size()+"--->"+temp);
				data.write2Database(set1.size(),temp,set1, 20000+tableName);
				
			}
		}
	}

}
