package sdu.ir.sina_forward_data;

import io.AppendFile;

import java.io.File;
import java.util.ArrayList;

import sdu.ir.sina_forward_data.Parser.Node;
import sdu.ir.util.Util;

public class Main {

	private void createAPDFile() {
		long time1 = System.currentTimeMillis();
		String filePath = "dataSet//weibodata";
		File root = new File(filePath);
		File[] files = root.listFiles();
		Parser parser = new Parser();
		parser.setReg("rusers:(.*?)\\|rdate");
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for(File file:files){
		    String fileName = file.getName();
		    File[] files2 = file.listFiles();
		    for (File file2 : files2) {
				Node node = parser.calAPD(file2);
				if(node != null){
//					System.out.println(file2.getName()+"\t"+node.num_infect+"\t"+node.apd);
					//sb.append(node.num_infect+" "+node.apd+"\n");
				}
				
			}
//		    AppendFile.append("sina"+i+".txt",sb.toString());
//		    sb = new StringBuffer();
		    i++;
		}
		AppendFile.append("icm_apd//sinaForwardAPD.txt", sb.toString());
		long time2 = System.currentTimeMillis();
		System.out.println("totalTime ===>"+(time2-time1)/1000f+"seconds");
		
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
