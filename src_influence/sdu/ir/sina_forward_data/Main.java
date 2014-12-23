package sdu.ir.sina_forward_data;

import io.AppendFile;
import io.ExcelOp;

import java.io.File;
import java.util.ArrayList;

import sdu.ir.sina_forward_data.Parser.Node;
import sdu.ir.util.Util;

public class Main {

	private void createAPDFile() {
		long time1 = System.currentTimeMillis();
		String filePath = "E:\\dataset\\dataset\\weibodata";
		File root = new File(filePath);
		File[] files = root.listFiles();
		Parser parser = new Parser();
		parser.setReg("rusers:(.*?)\\|rdate");
		StringBuffer sb = new StringBuffer();
		for(File file:files){
		    String fileName = file.getName();
		    File[] files2 = file.listFiles();
//		    write2File(files2,"experimentsResult\\sina"+fileName,parser);
		    write2Excel(files2,"experimentsResult\\1sina"+fileName+".xls",parser);
		}
		long time2 = System.currentTimeMillis();
		System.out.println("totalTime ===>"+(time2-time1)/1000f+"seconds");
		
	}
	
	/**
	 * @param files2
	 * @param fileName
	 * @param parser
	 */
	private void write2Excel(File[] files2, String fileName, Parser parser) {
		ExcelOp.createExcel(fileName);
		for (File file2 : files2) {
			double[] record = parser.calDiffusionDetail(file2);
			ExcelOp.recordPrepar(record);
//			Util.block("write over one file!!!!!!!");
		}
		ExcelOp.writeAndcloseExcel();
		
	}

	/**
	 * @param files2
	 * @param fileName
	 * @param parser 
	 */
	private void write2File(File[] files2, String fileName, Parser parser) {
		StringBuffer sb = new StringBuffer();
		for (File file2 : files2) {
			double[] record = parser.calDiffusionDetail(file2);
//			System.out.println(file2.getName()+"\t"+node.num_infect+"\t"+node.apd);
			for (int j = 0; j < record.length; j++) {
				sb.append(record[j]+" ");
			}
			sb.append("\n");
		}
	    AppendFile.append(fileName,sb.toString());
	}

	private void calProportion() {
		long time1 = System.currentTimeMillis();
		String filePath = "dataSet//weibodata";
		File root = new File(filePath);
		File[] files = root.listFiles();
		Parser parser = new Parser();
		parser.setReg("rusers:(.*?)\\|rdate");
		int i = 0;
		int j = 0;
		for(File file:files){
		    String fileName = file.getName();
		    File[] files2 = file.listFiles();
		    for (File file2 : files2) {
		    	i++;
		    	double re = parser.calProportion(file2,10);
		    	if(re == 0)j++;
		    	System.out.println(re);
		    }
		}
		System.out.println(j+"\t"+i);
		System.out.println("±ÈÀýÎª==>"+(double)j/(double)i);
		long time2 = System.currentTimeMillis();
		System.out.println("totalTime ===>"+(time2-time1)/1000f+"seconds");
		
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.createAPDFile();
//		m.calProportion();
			
	}

	



}
