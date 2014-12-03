package irlab.main;

import io.AppendFile;
import irlab.dbOperate.Data;

import java.io.File;

import sdu.ir.util.Util;
import text.Print;

public class CreateMatlabInput {

	/**
	 * @param args
	 */
//	static String[] tables = new String[]{"_nWtNumGEfficient",
//			"_nWtNumGEfficientcelf","_degreeDiscountIC","_random"};
	static String[] tables = new String[]{"_greedy","_nWtNumGEfficient","_nWtNumGEfficientcelf",
		"_degreeDiscountIC","_random"};
	public static void main(String[] args) {
		CreateMatlabInput cmi = new CreateMatlabInput();
		String dataName = "20000ca_HepPh1";
		int executions= 4000;
		int set = 50;
		double[] p = new double[]{0.5,0.3,0.1};

		
//		cmi.createCompareMatalabInput(dataName,executions,set,p);
		System.out.println(100*cmi.getDifference(dataName,executions,set,p)+"%");
	}

	private double getDifference(String dataName, int executions, int set, double[] p) {
		String suffix = Util.calSuffix(executions,set,p);
		String table_greedy = dataName+tables[0]+suffix;
		String table_compare = dataName+tables[1]+suffix;
		double[] greedy = getDouble(table_greedy);
		double[] compare = getDouble(table_compare);
		Print.print(greedy);
		Print.print(compare);
		double count = 0;
		for (int i = 0; i < compare.length; i++) {
			count += (greedy[i]-compare[i])/greedy[i];
		}
		count = count/50;
		return count;
	}

	private double[] getDouble(String tableName) {
		Data data = new Data();
		String content = data.getMatalabContent(tableName);
		String[] contents = content.split("\t");
//		System.out.println(contents.length);
//		Print.print(contents);
		double[] temp = new double[contents.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = Double.parseDouble(contents[i]);
		}
		return temp;
	}

	private void createCompareMatalabInput(String dataName, int executions, int set, double[] p) {
		String suffix = Util.calSuffix(executions,set,p);
		File file = new File("matlab\\"+dataName+suffix+".txt");
		if(file.exists())file.delete();
		for (int i = 0; i < tables.length; i++) {
			String tableName = dataName+tables[i]+suffix;
			create_matlab_drawing_input(tableName,"matlab\\"+dataName+suffix+".txt");
		}
	}

	private void create_matlab_drawing_input(String tableName, String url) {
		Data data = new Data();
		String content = data.getMatalabContent(tableName);
		AppendFile.append(url, content);
	}

}
