package sdu.ir.util;

import java.io.*;
public class OutPut {
 
	public static void write2File(String fileName, String out) {
		  try {
		   File f = new File("F:\\��ҵ����\\experiment\\"+fileName+".txt");
		   if(f.exists()){
		    System.out.print("�ļ�����");
		   }else{
		    System.out.print("�ļ�������");
		    f.createNewFile();//�������򴴽�
		   }

		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		   output.write(out);
		   output.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }

	}
	
	public static void write2File(String fileName,double[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]+",");
		}
		write2File(fileName,sb.toString());
	}
	
 public static void main(String[] args){
	 write2File("abc", "dong");
 }
 
}