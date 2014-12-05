package io;

import irlab.dbOperate.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;



 /**
  * @author Hongten
  * 
  * @time 2011-12-12 2011
  */
 public class FileOp {
   

     private static String path = "txt/";
     private static String filenameTemp;

     /**
      * 创建文件
      * 
      * @throws IOException
      */
     public static boolean creatTxtFile(String name) throws IOException {
         boolean flag = false;
         filenameTemp = path + name + ".txt";
         File filename = new File(filenameTemp);
         if (!filename.exists()) {
             filename.createNewFile();
             flag = true;
         }
         return flag;
     }

     /**
      * 写文件
     * @param url 
      * 
      * @param newStr
      *            新内容
      * @throws IOException
     * @throws SQLException 
      */
     public static boolean writeTxtFile(String table,String url) throws IOException, SQLException {
    	 File file = new File(url);
         if (!file.exists()) {
        	 file.createNewFile();
         }
         // 先读取数据库内容，然后进行写入操作
    	 Data data = new Data();
    	 double[][] relations = data.getRelationship(table);
         boolean flag = false;
//         String filein = newStr + "\r\n";
         String temp = "";

         FileOutputStream fos = null;
         PrintWriter pw = null;
         try {
             StringBuffer buf = new StringBuffer();

             // 保存该文件原有的内容
             for (int j = 0; j<relations.length; j++) {
//            	 System.out.println(relations[0]+" "+relations[1]);
                 buf = buf.append((int)relations[j][0]+" "+(int)relations[j][1]);
                 // System.getProperty("line.separator")
                 // 行与行之间的分隔符 相当于“\n”
                 buf = buf.append(System.getProperty("line.separator"));
             }
//             buf.append(filein);

             fos = new FileOutputStream(file);
             pw = new PrintWriter(fos);
             pw.write(buf.toString().toCharArray());
             pw.flush();
             flag = true;
         } catch (IOException e1) {
             // TODO 自动生成 catch 块
             throw e1;
         } finally {
      
         }
         return flag;
     }

     /**
      * 读取数据
      */
     public void readData1() {
         try {
             FileReader read = new FileReader(filenameTemp);
             BufferedReader br = new BufferedReader(read);
             String row;
             while ((row = br.readLine()) != null) {
                 System.out.println(row);
             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public String readDate() {
         // 定义一个待返回的空字符串
         String strs = "";
         try {
             FileReader read = new FileReader(new File(filenameTemp));
             StringBuffer sb = new StringBuffer();
             char ch[] = new char[1024];
             int d = read.read(ch);
             while (d != -1) {
                 String str = new String(ch, 0, d);
                 sb.append(str);
                 d = read.read(ch);
             }
             System.out.print(sb.toString());
             String a = sb.toString().replaceAll("@@@@@", ",");
             strs = a.substring(0, a.length() - 1);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return strs;
     }
     
     public double[][] readTxtFile2Mem(String filePath,int numColumn){
         try {
             String encoding="gbk";
             File file=new File(filePath);
             if(file.isFile() && file.exists()){ //判断文件是否存在
                 InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
                 BufferedReader bufferedReader = new BufferedReader(read);
                 ArrayList<String[]> list = new ArrayList<String[]>();
                 String lineTxt = null;
                 while((lineTxt = bufferedReader.readLine()) != null){
                     list.add(lineTxt.split(" "));
                 }
                 read.close();
                 return list2Array(list,numColumn);
 	        }else{
 	            System.out.println("找不到指定的文件");
 	        }
         } catch (Exception e) {
             System.out.println("读取文件内容出错");
             e.printStackTrace();
         }
 		return null;
      
     }
     
 	
 	private double[][] list2Array(ArrayList<String[]> list, int numColumn) {
 		String[] temp = list.get(0);
		int i = 0;
		while(temp[i].equals("")){
			i++;
		}
		int a = i;
		i++;
		while(temp[i].equals("")){
			i++;
		}
		int b = i;
		int weight = -1;
		if(numColumn == 3){
			i++;
			while(temp[i].equals("")){
				i++;
			}
			weight = i;
		}
		
		
		ArrayList<int[]> graph = list2int(list,a,b,weight);
		double[][] matrix = new double[graph.size()][2];
		for (int j = 0; j < graph.size(); j++) {
			matrix[j][0] = graph.get(j)[0]-1;
			matrix[j][1] = graph.get(j)[1]-1;
		}
//				Print.print(matrix);
		return matrix;
	}

			
 	private int getMaxNodeNum(ArrayList<int[]> graph) {
		int max = graph.get(0)[0];
		for (int i = 0; i < graph.size(); i++) {
			if(graph.get(i)[0] > max)
				max = graph.get(i)[0];
			if(graph.get(i)[1] > max)
				max = graph.get(i)[1];
		}
		return max;
	}

 	
	private ArrayList<int[]> list2int(ArrayList<String[]> list, int a, int b, int weight) {
		ArrayList<int[]> returnList = new ArrayList<int[]>();
		for (int i = 0; i < list.size(); i++) {
			int a1 = Integer.parseInt(list.get(i)[a]);
			int b1 = Integer.parseInt(list.get(i)[b]);
			if(weight != -1){
				int weight1 = Integer.parseInt(list.get(i)[weight]);
				returnList.add(new int[]{a1,b1,weight1});
			}else{
				returnList.add(new int[]{a1,b1,-1});
			}
			
		}
		return returnList;
	}



	public static String readFile2String(String filePath) {
		 try {
             String encoding="gbk";
             File file=new File(filePath);
             if(file.isFile() && file.exists()){ //判断文件是否存在
                 InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
                 BufferedReader bufferedReader = new BufferedReader(read);
                 ArrayList<String[]> list = new ArrayList<String[]>();
                 StringBuffer sb = new StringBuffer("");
                 String lineTxt = null;
                 while((lineTxt = bufferedReader.readLine()) != null){
                     sb.append(lineTxt);
                     sb.append(" ");
                 }
                 read.close();
                 return sb.toString();
 	        }else{
 	            System.out.println("找不到指定的文件");
 	        }
         } catch (Exception e) {
             System.out.println("读取文件内容出错");
             e.printStackTrace();
         }
 		return null;
	}
	public static String readFile2String(String filePath, String encoding) {
		 try {
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                ArrayList<String[]> list = new ArrayList<String[]>();
                StringBuffer sb = new StringBuffer("");
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                    sb.append("\n");
                }
                read.close();
                return sb.toString();
	        }else{
	            System.out.println("找不到指定的文件");
	        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
		return null;
	}
	
	public static void readFileOneLine(String filePath, String encoding) {
		 try {
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                ArrayList<String[]> list = new ArrayList<String[]>();
                StringBuffer sb = new StringBuffer("");
                String lineTxt = bufferedReader.readLine();
                System.out.println(lineTxt);
                read.close();
	        }else{
	            System.out.println("找不到指定的文件");
	        }
	        } catch (Exception e) {
	            System.out.println("读取文件内容出错");
	            e.printStackTrace();
	        }
	}
	
	public static void convertCSV2Txt(String filePath, String targetFile, String encoding) {
		 try {
           File file=new File(filePath);
           if(file.isFile() && file.exists()){ //判断文件是否存在
               InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
               BufferedReader bufferedReader = new BufferedReader(read);
               ArrayList<String[]> list = new ArrayList<String[]>();
               StringBuffer sb = new StringBuffer("");
               String lineTxt = null;
               while((lineTxt = bufferedReader.readLine()) != null){
                   String[] ss = lineTxt.split(",");
                   String a = ss[1].substring(1,ss[1].length()-1);
                   String b = ss[2].substring(1,ss[2].length()-1);
                   sb.append(a+" "+b+"\n");
               }
               AppendFile.append(targetFile, sb.toString());
               read.close();
	        }else{
	            System.out.println("找不到指定的文件");
	        }
	        } catch (Exception e) {
	            System.out.println("读取文件内容出错");
	            e.printStackTrace();
	        }
	}
	
	
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
   	 FileOp myFile = new FileOp();
//   	double[][] relation = myFile.readTxtFile2Mem("G:\\研究僧\\数据集\\数据集\\gh1.txt",2);
//   	 Data data = new Data();
//   	 data.createTable("cdmsm");
//   	 data.write2Database(relation,"cdmsm");
//   	 myFile.writeTxtFile("cdmsm","G:\\研究僧\\数据集\\数据集\\gh2.txt");
//   	 readFileOneLine("C:\\Users\\qyd\\Desktop\\digg_friends.csv", "utf-8");
//   	 convertCSV2Txt("C:\\Users\\qyd\\Desktop\\digg_votes1.csv", "dataset\\digVotes.txt", "utf-8");
       readFileFast("EmailEuAlllog.txt");
    }

	public static void deletFile(String url) {
		File file = new File(url);
		if(file.exists())
			file.delete();
		
	}

	public static void mergeFile(String[] ss, String url) {
		try {
			for (int i = 0; i < ss.length; i++) {
				String filePath = ss[i];
				System.out.println("正在merge=》"+filePath);
				File file = new File(filePath);
				if(file.isFile() && file.exists()){ //判断文件是否存在
	                InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");//考虑到编码格式
	                BufferedReader bufferedReader = new BufferedReader(read);
	                ArrayList<String[]> list = new ArrayList<String[]>();
	                StringBuffer sb = new StringBuffer("");
	                String lineTxt = null;
	                int count = 0;
	                while((lineTxt = bufferedReader.readLine()) != null){
	                    sb.append(lineTxt);
	                    sb.append("\n");
	                    count ++;
	                    if(count == 10000){
	                    	AppendFile.append(url, sb.toString());
	                    	sb = new StringBuffer("");
	                    	count = 0;
	                    }
	                }
	                read.close();
		        }
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	public static void readFileFast(String filePath){
		int bigSize = 128*1024;
		int smallSize = 128*1024;
		try {FileInputStream f = new FileInputStream( filePath );
		FileChannel ch = f.getChannel( );
		ByteBuffer bb = ByteBuffer.allocateDirect( bigSize );
		byte[] barray = new byte[smallSize];
		int nRead, nGet;
		while ( (nRead=ch.read( bb )) != -1 )
		{
		    if ( nRead == 0 )
		        continue;
		    bb.position( 0 );
		    bb.limit( nRead );
		    while( bb.hasRemaining( ) )
		    {
		        nGet = Math.min( bb.remaining( ), smallSize );
		        bb.get( barray, 0, nGet );
		        String s = new String(barray,"utf-8");
		        System.out.println(s);
		    }
		    bb.clear( );
		}} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void printFile(String filePath, int intervalTime) {
		 try {
	           File file=new File(filePath);
	           if(file.isFile() && file.exists()){ //判断文件是否存在
	               InputStreamReader read = new InputStreamReader( new FileInputStream(file),"utf-8");//考虑到编码格式
	               BufferedReader bufferedReader = new BufferedReader(read);
	               ArrayList<String[]> list = new ArrayList<String[]>();
	               StringBuffer sb = new StringBuffer("");
	               String lineTxt = null;
	               while((lineTxt = bufferedReader.readLine()) != null){
	                   System.out.println(lineTxt);
	                   Thread.sleep(intervalTime);
	               }
	               read.close();
		        }else{
		            System.out.println("找不到指定的文件");
		        }
		        } catch (Exception e) {
		            System.out.println("读取文件内容出错");
		            e.printStackTrace();
		        }
		
	}
 }
 
 
 
 
 
 
 
 
 