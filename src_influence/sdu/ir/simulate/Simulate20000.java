package sdu.ir.simulate;

import irlab.dbOperate.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import sdu.ir.diffusionmodel.ICM3MultiThread;
import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Constant;
import sdu.ir.util.Util;
import test.Print;

public class Simulate20000 {
	
	static String[] tables1 = new String[]{"_tllfgreedy",
		"_degreediscountic","_random","_neibornumgreedye"};
	static String[] tables = new String[]{"_neibornumgreedye"};
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stubca_HepPh1.txt
		Data data = new Data();
		String dataName = "dealedsocpokec";
		double begin = System.currentTimeMillis();
		String filePath = Constant.filePathLinux+dataName+".txt";
		ReadGraph rd = new ReadGraph();
		Graph gh = rd.readTxtFile2Graph(filePath, "Adjacentlistwithoutweight",2," ");
		int executions = 20000;//icm中模拟次数
		int set = 50;//集合大小
		double[] p = new double[]{0.1,0.05,0.01};
		ICM3MultiThread icm3 = new ICM3MultiThread(5000,4, p[0], p[1], p[2]);
		String suffix = Util.calSuffix(executions,set,p);
		for (int i = 0; i < tables.length; i++) {
			String tableName = "0"+dataName+tables[i]+suffix;
			System.out.println(tableName+"------");
			ArrayList<Set<Integer>> list = data.getSeedSet(tableName);
//			for (int j = 0; j < list.size(); j++) {
//				System.out.println(list.get(j));
//			}
			for (int j = 0; j < list.size(); j++) {
				Set<Integer> set1 = list.get(j);
				double temp = icm3.diffusion(gh, set1);
				System.out.println(set1.size()+"--->"+temp);
				data.update2Database(set1.size(),temp,set1, tableName);
				
			}
		}
	}

}
