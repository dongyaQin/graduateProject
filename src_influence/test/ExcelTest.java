/**
 * 
 */
package test;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * @author qyd
 *
 */
public class ExcelTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
	    try 
	    { 
	    //���ļ� 
	    WritableWorkbook book= Workbook.createWorkbook(new File("E:\\����.xls")); 
	    //������Ϊ����һҳ���Ĺ���������0��ʾ���ǵ�һҳ 
	    WritableSheet sheet=book.createSheet("��erҳ",0); 
	    //��Label����Ĺ�������ָ����Ԫ��λ���ǵ�һ�е�һ��(0,0) 
	    //�Լ���Ԫ������Ϊtest 
	    Label label=new Label(0,0,"����"); 
	    //������õĵ�Ԫ����ӵ��������� 
	    sheet.addCell(label);  
	    book.write(); 
	    jxl.write.Number number = new jxl.write.Number(1,2,789.123); 
	    sheet.addCell(number); 
	    jxl.write.Label s=new jxl.write.Label(2, 3, "��ʮ��");
	    sheet.addCell(s); 
//	    sheet2.addCell(s);
	    //д�����ݲ��ر��ļ� 
	    book.write(); 
	    book.close(); //�����finally�йرգ��˴�����Ϊʾ����̫�淶
	    }
	    catch(Exception e) 
	    { 
	        System.out.println(e); 
	    } 
	}

}
