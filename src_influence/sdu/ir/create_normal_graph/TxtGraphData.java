package sdu.ir.create_normal_graph;

import io.AppendFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
import text.Print;

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
		System.out.println("correspondMap�Ĵ�СΪ:"+correspondMap.size());
//		Util.block();
	}
	
	public void load(String url, String fenge, String encoding) {
		this.relations.clear();
		 try {
	        File file=new File(url);
	        if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
		        InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//���ǵ������ʽ
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
	        System.out.println("�Ҳ���ָ�����ļ�");
	    }
	} catch (Exception e) {
	    System.out.println("��ȡ�ļ����ݳ���");
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
	        if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
		        InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//���ǵ������ʽ
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
		        System.out.println("�Ҳ���ָ�����ļ�");
		        return null;
		    }
		} catch (Exception e) {
		    System.out.println("��ȡ�ļ����ݳ���");
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
		System.out.println("story"+story+"��users");
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
			System.out.println(user+"���ھ��ǡ�����");
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
		System.out.println("�ܹ�Ӱ��-��"+record_users.size());
	}

	public void write2File(String url) {
		StringBuffer sb = new StringBuffer();
		for (int[] is : relations) {
			if(correspondMap.containsKey(is[0]))
				sb.append(correspondMap.get(is[0])+" "+is[1]+"\n");
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

	public void printUserNumbers(String filePath, String fenge) {
		 try {	
			Set<String> set = new HashSet<String>(10000000);
	        File file=new File(filePath);
	        if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
		        InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");//���ǵ������ʽ
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
		        		System.out.println("��ǰset��СΪ��"+set.size()+"\tռ�ÿռ�Ϊ��"+SizeOf.humanReadable(SizeOf.deepSizeOf(set)));
		        		count = 0;
//		        		Util.block();
		        	}
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
	
}
