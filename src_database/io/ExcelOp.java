/**
 * 
 */
package io;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author qyd
 *
 */
public class ExcelOp {

	/**
	 * @param args
	 */
	private static WritableWorkbook book;
	private static WritableSheet sheet;
	private static int line = 0;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	/**
	 * @param fileName
	 */
	public static void createExcel(String fileName) {
		try {
			book = Workbook.createWorkbook(new File(fileName));
		    sheet=book.createSheet("第一页",0); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    //生成名为“第一页”的工作表，参数0表示这是第一页 
		
	}



	/**
	 * @param record
	 */
	public static void recordPrepar(double[] record) {
		try {
			for (int i = 0; i < record.length; i++) {
				jxl.write.Number number = new jxl.write.Number(i,line,record[i]); 
				sheet.addCell(number); 
				System.out.println(line+" "+i);
			}
			line++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	/**
	 * 
	 */
	public static void writeAndcloseExcel() {
		// TODO Auto-generated method stub
		try {
			book.write();
			book.close();
			line = 0;
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
