package sdu.ir.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sdu.ir.interfaces.Graph;
import sdu.ir.storegraph.AdjacencyListWithWeight;
import sdu.ir.storegraph.AdjacencyListWithoutWeight;
import sdu.ir.storegraph.AdjacentMatrix;

public class ReadGraph {

	
	//storeMethodΪͼ�Ĵ洢��ʽ����Ϊ"AdjacentMatrix"��"AdjacentList"��"Edges"
	public Graph readTxtFile2Graph(String filePath,String storeMethode,int numColumn, String fenge){
        try {
            String encoding="gbk";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                ArrayList<String[]> list = new ArrayList<String[]>();
                String lineTxt = null;
                int count = 0;
                while((lineTxt = bufferedReader.readLine()) != null){
                	if(lineTxt.equals(""))continue;
                    list.add(lineTxt.split(fenge));
                    System.out.println(count);
                    count++;
                }
                read.close();
                return list2Graph(list,storeMethode,numColumn);
	        }else{
	            System.out.println("file can't find");
	        }
        } catch (Exception e) {
            System.out.println("file error");
            e.printStackTrace();
        }
		return null;
     
    }

	
	//���ļ��ж���Ĵ洢��list�У�Ȼ���ٽ�Listת��ΪGraph����
	private Graph list2Graph(ArrayList<String[]> list, String storeMethode, int numColumn) {

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
		int max = getMaxNodeNum(graph);
		if(storeMethode != null){
			if(storeMethode.equalsIgnoreCase("AdjacentMatrix")){
				AdjacentMatrix matrix = new AdjacentMatrix(max+1,max+1);
				for (int j = 0; j < graph.size(); j++) {
					matrix.setWeight(graph.get(j)[0], graph.get(j)[1], graph.get(j)[2]==-1?1:graph.get(j)[2]);
//					matrix.setWeight(graph.get(j)[1], graph.get(j)[0], graph.get(j)[2]==-1?1:graph.get(j)[2]);
				}
//				Print.print(matrix);
				return matrix;
			}else if(storeMethode.equalsIgnoreCase("AdjacentListWithoutWeight")){
				AdjacencyListWithoutWeight adjList = new AdjacencyListWithoutWeight(max+1);
				for (int j = 0; j < graph.size(); j++) {
					adjList.add(graph.get(j)[0], graph.get(j)[1], graph.get(j)[2]==-1?1:graph.get(j)[2]);
//					adjList.add(graph.get(j)[1], graph.get(j)[0], graph.get(j)[2]==-1?1:graph.get(j)[2]);
				}
				return adjList;
			}else if(storeMethode.equalsIgnoreCase("AdjacentListWithWeight")){
				AdjacencyListWithWeight adjList = new AdjacencyListWithWeight(max+1);
				for (int j = 0; j < graph.size(); j++) {
					adjList.add(graph.get(j)[0], graph.get(j)[1], graph.get(j)[2]==-1?1:graph.get(j)[2]);
//					adjList.add(graph.get(j)[1], graph.get(j)[0], graph.get(j)[2]==-1?1:graph.get(j)[2]);
				}
				return adjList;
			}else if(storeMethode.equalsIgnoreCase("Edges")){
				
			}else{
				
			}
		}
		return null;
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

	public static void readTxtFile(String filePath){
        try {
            String encoding="gbk";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                StringBuffer sb = new StringBuffer();
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                }
                read.close();
	        }else{
	            System.out.println("�Ҳ���ָ�����ļ�");
	        }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݳ���");
            e.printStackTrace();
        }
     
    }
	
	public static void main(String[] args) {
		

	}


	public Graph readTxtFile2Graph(String filePath, String string, int i) {
		return this.readTxtFile2Graph(filePath, string, i," ");
	}

}
