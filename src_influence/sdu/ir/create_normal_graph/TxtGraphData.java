package sdu.ir.create_normal_graph;

import io.AppendFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.sourceforge.sizeof.SizeOf;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.Graph;
import sdu.ir.util.Util;
import test.Print;

public class TxtGraphData {
	ArrayList<int[]> relations=new ArrayList<int[]>();
	Map<Integer,Integer> correspondMap = new HashMap<Integer,Integer>();
	public int relationLength;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void creatCorrespondMap(){
		int[] userIds = getUserIds();
		for (int i = 0; i < userIds.length; i++) {
			correspondMap.put(userIds[i], i);
		}
		System.out.println("correspondMap size:"+correspondMap.size());
		Util.block("input a char to continue...");
	}
	
	public void load(String url, String fenge, String encoding) {
		this.relations.clear();
		 try {
	        File file=new File(url);
	        if(file.isFile() && file.exists()){ //判断文件是否存在
		        InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
		        BufferedReader bufferedReader = new BufferedReader(read);
		        ArrayList<String[]> list = new ArrayList<String[]>();
		        StringBuffer sb = new StringBuffer("");
		        String lineTxt = null;
		        while((lineTxt = bufferedReader.readLine()) != null){
		        	String[] tmp = lineTxt.split(fenge);
		        	int[] a = new int[]{Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1])};
		        	relations.add(a);
		        }
		        read.close();
	    }else{
	        System.out.println("找不到指定的文件");
	    }
	    System.out.println("load finished!!!!!!!!!");
	    System.out.println("relationship size-->"+relations.size());
	    Util.block("input a char to continue...");
	} catch (Exception e) {
	    System.out.println("读取文件内容出错");
	    e.printStackTrace();
	}
	
	
	}

	public int[] getUserIds() {
		return set2Array(getUserIdSet());
	}

	private Set<Integer> getUserIdSet(){
		Set<Integer> set = new HashSet<Integer>();
		for (int[] relation : relations) {
			int user = relation[0];
			int friend = relation[1];
			if(!set.contains(user)){
				set.add(user);
			}
			if(!set.contains(friend)){
				set.add(friend);
			}
		}
		return set;
	}
	
	public ArrayList<int[]> getRelationship() {
		return relations;
	}

	private int[] set2Array(Set<Integer> set) {
		int[] arr = new int[set.size()];
		int count = 0;
		for (Integer d : set) {
			arr[count] = d;
			count ++;
		}
		return arr;
	}

	private Map<Integer,ArrayList<Integer>> getVoteMap(String url, String fenge, String encoding) {
		 try {
	        File file=new File(url);
	        if(file.isFile() && file.exists()){ //判断文件是否存在
		        InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
		        BufferedReader bufferedReader = new BufferedReader(read);
		        StringBuffer sb = new StringBuffer("");
		        String lineTxt = null;
		        Map<Integer,ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		        while((lineTxt = bufferedReader.readLine()) != null){
		        	String[] tmp = lineTxt.split(fenge);
		        	int a = Integer.parseInt(tmp[0]);
		        	int b = Integer.parseInt(tmp[1]);
		        	if(map.containsKey(b)){
		        		map.get(b).add(a);
		        	}else{
		        		ArrayList<Integer> list = new ArrayList<Integer>();
		        		list.add(a);
		        		map.put(b, list);
		        	}
		        		
		        }
		        read.close();
		        return map;
		    }else{
		        System.out.println("找不到指定的文件");
		        return null;
		    }
		} catch (Exception e) {
		    System.out.println("读取文件内容出错");
		    e.printStackTrace();
		}
		return null;
		
	}

	public void createVoteGraph(String url, String fenge, String encoding) {
		Map<Integer,ArrayList<Integer>> map = getVoteMap(url, fenge, encoding);
		ReadGraph rd = new ReadGraph();
		Graph gh = rd.readTxtFile2Graph("dataset\\finalDigRelation.txt", "Adjacentlistwithoutweight",2," ");
		Set<Integer> keySet = map.keySet();
		for (Integer story : keySet) {
			ArrayList<Integer> users = map.get(story);
			createVoteGraphOf(gh,story,users);
			Util.block("input to continue..");
		}
	}

	private void createVoteGraphOf(Graph gh, Integer story, ArrayList<Integer> users) {
		System.out.println("story"+story+"的users");
		Print.print(users);
		int firstUser = users.get(0);
		Set<Integer> record_users = new HashSet<Integer>();
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(firstUser);
		record_users.add(firstUser);
		int step = 1;
		while(!q.isEmpty()){
			int user = q.remove();
			Set<Integer> neibors = gh.getNeighbor(user);
			System.out.println(user+"的邻居们。。。");
			Print.print(neibors);
			if(neibors != null){
				neibors.retainAll(users);
				neibors.removeAll(record_users);
			}
			System.out.println("step "+step+++" -->"+neibors.size());
			Print.print(neibors);
			record_users.addAll(neibors);
			q.addAll(neibors);
		}
		System.out.println("总共影响-》"+record_users.size());
	}

	public void write2File(String url) {
		StringBuffer sb = new StringBuffer();
		for (int[] is : relations) {
			if(correspondMap.containsKey(is[0]))
				sb.append(correspondMap.get(is[0])+" "+correspondMap.get(is[1])+"\n");
		}
		AppendFile.append(url, sb.toString());
	}

	public void writeRelations2File(String url) {
		StringBuffer sb = new StringBuffer();
		for (int[] is : relations) {
			int a = is[0];
			int b = is[1];
			a = correspondMap.get(a);
			b  = correspondMap.get(b);
			sb.append(b+" "+a+"\n");
		}
		AppendFile.append(url, sb.toString());
		
	}

	public int printUserNumbers(String filePath, String fenge) {

    	long time1 = System.currentTimeMillis();
		 try {	
			Set<String> set = new HashSet<String>(20000000);
	        File file=new File(filePath);
	        if(file.isFile() && file.exists()){ //判断文件是否存在
		        InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");//考虑到编码格式
		        BufferedReader bufferedReader = new BufferedReader(read);
		        String lineTxt = null;
		        Map<Integer,ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		        int count = 0;
		        while((lineTxt = bufferedReader.readLine()) != null){
		        	String[] tmp = lineTxt.split(fenge);
		        	for (int i = 0; i < tmp.length; i++) {
						set.add(tmp[i]);
					}
//		        	Util.block();
		        	count ++;
		        	if(count == 10000){
		        		long time2 = System.currentTimeMillis();
		        		System.out.println("set size->："+set.size()+"\ttime cost->"+(time2-time1)/1000f+"seconds");
//		        		AppendFile.append("/home/qinyadong/dataset/tempfile","set size->："+set.size()+"\ttime cost->"+(time2-time1)/1000f+"seconds");
//		        		time1 = time2;
		        		count = 0;
//		        		Util.block();
		        	}
		        }
		        read.close();
		        long time22 = System.currentTimeMillis();
		        AppendFile.append("/home/qinyadong/dataset/tempfile","finalSize->"+set.size()+"total time->"+(time22-time1)/1000f+"seconds");
		        return set.size();
		    }else{
		        System.out.println("file does not exist");
		    }
		} catch (Exception e) {
		    System.out.println("somethign is wrong");
		    e.printStackTrace();
		}
		return 0;
	}
	
	public static void readFileFast(String filePath,String fenge){
		long time1 = System.currentTimeMillis();
		Set<String> set = new HashSet<String>(20000000);
		int bigSize = 128*1024;
		int smallSize = 128*1024;
		try {
			FileInputStream f = new FileInputStream( filePath );
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
//			        System.out.println(s);
			        String[] lines = s.split("\n");
			        for (int i = 0; i < lines.length; i++) {
			        	String[] ids = lines[i].split(fenge);
						for (int j = 0; j < ids.length; j++) {
							set.add(ids[j]);
						}
					}
			        int i = 0;
//			        for (String string : set) {
//						System.out.println(i+++"-->"+string);
//					}
//			        Util.block();
//			        System.out.println("size->"+set.size());
			    }
			    bb.clear( );
			    System.out.println(set.size());
//			    Util.block();
			}
			System.out.println("finale size=>"+set.size());
		long time2 = System.currentTimeMillis();
		System.out.println("totalTime->"+(time2-time1)/1000f+"seconds");
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
}
