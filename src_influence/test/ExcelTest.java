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
	    //打开文件 
	    WritableWorkbook book= Workbook.createWorkbook(new File("E:\\测试.xls")); 
	    //生成名为“第一页”的工作表，参数0表示这是第一页 
	    WritableSheet sheet=book.createSheet("第er页",0); 
	    //在Label对象的构造子中指名单元格位置是第一列第一行(0,0) 
	    //以及单元格内容为test 
	    Label label=new Label(0,0,"测试"); 
	    //将定义好的单元格添加到工作表中 
	    sheet.addCell(label);  
	    book.write(); 
	    jxl.write.Number number = new jxl.write.Number(1,2,789.123); 
	    sheet.addCell(number); 
	    jxl.write.Label s=new jxl.write.Label(2, 3, "三十三");
	    sheet.addCell(s); 
//	    sheet2.addCell(s);
	    //写入数据并关闭文件 
	    book.write(); 
	    book.close(); //最好在finally中关闭，此处仅作为示例不太规范
	    }
	    catch(Exception e) 
	    { 
	        System.out.println(e); 
	    } 
	}

}
