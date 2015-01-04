package sdu.ir.util;

import io.FileOp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sdu.ir.interfaces.Graph;

public class Util {
	static Scanner scan = new Scanner(System.in);
	public static int findMaxIndex(int[] degree,int begin) {
		int max = degree[begin];
		int out = begin;
		for (int i = begin; i < degree.length; i++) {
			if(degree[i] > max){
				max = degree[i];
				out = i;
			}
		}
		return out;
	}
	
	public static int findMaxIndex(double[] degree,int begin) {
		double max = degree[begin];
		int out = begin;
		for (int i = begin; i < degree.length; i++) {
			if(degree[i] > max){
				max = degree[i];
				out = i;
			}
		}
		return out;
	}
	
	public static int random(int i, int j) {
		Random r = new Random();
		return r.nextInt(j-i+1)+i;
	}
	
	public static int findMax(int[] degree,Set<Integer> no,int begin) {
		int max = -1;
		int out = -1;
		for (int i = begin; i < degree.length; i++) {
			if(!no.contains(i)){
				if(degree[i] > max){
					max = degree[i];
					out = i;
				}
			}
		}
		return out;
	}
	
	//根据isFindedCom的指示，找到最大的数组下标
	public static int findMax(int[] degree, boolean[] isFindedCom, int begin) {
		int max = Integer.MIN_VALUE;
		int out = -1;
		for (int i = begin; i < degree.length; i++) {
			if(!isFindedCom[i] && degree[i] > max){
				max = degree[i];
				out = i;
			}
		}
		return out;
	}
	
	public static void bfs(Graph graph,int i, int biaoji, int[] mark) {
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(i);
		mark[i] = biaoji;
		while(!q.isEmpty()){
			int u = q.poll();
			Set<Integer> set = graph.getNeighbor(u);
			for (Integer v : set) {
				if(mark[v] == 0){
					mark[v] = biaoji;
					q.add(v);
				}
			}
		}
		
	}

	public static int findMaxIndegree(Graph graph) {
		int max = -1;
		int out = -1;
		for (int i = 0; i < graph.size(); i++) {
			if(graph.getNeighbor(i) != null){
				int temp = graph.getNeighbor(i).size();
				if(temp > max){
					max = temp;
					out = i;
				}
				
			}
		}
		return out;
	}

	public static void block() {
		System.out.println("wait to input...");
		scan.next();
		
	}
	
	public static <T extends Comparable<T>> void quickSort(T[][] a,int i,int j) {
		if(j > i){
			int youbiao = partition(a,i,j);
			quickSort(a, i, youbiao-1);
			quickSort(a, youbiao+1, j);
		}
		
	}

	private static <T extends Comparable<T>> int partition(T[][] a, int i, int j) {
		int p = i;
		T bb = a[j][1];
		for (int k = i; k < j; k++) {
			if(a[k][1].compareTo(bb) < 0){
				swap(a,k,p);
				p++;
			}
		}
		swap(a,p,j);
		return p;
	}

	private static <T> void swap(T[][] a, int k, int p) {
		T[] i = a[k];
		a[k] = a[p];
		a[p] = i;
		
	}
	
	public static void quickSort(int[] a,int i,int j) {
		if(j > i){
			int youbiao = partition(a,i,j);
			quickSort(a, i, youbiao-1);
			quickSort(a, youbiao+1, j);
		}
		
	}

	private static int partition(int[] a, int i, int j) {
		int p = i;
		int bb = a[j];
		for (int k = i; k < j; k++) {
			if(a[k] < bb){
				swap(a,k,p);
				p++;
			}
		}
		swap(a,p,j);
		return p;
	}

	private static void swap(int[] a, int k, int p) {
		int i = a[k];
		a[k] = a[p];
		a[p] = i;
		
	}
	//判断数组中时候含有某个数
	public static boolean contains(int[] arrays, int i) {
		for (int j = 0; j < arrays.length; j++) {
			if(arrays[j] == i)
				return true;
		}
		return false;
	}
	public static void invert(int[] retru) {
		int[] temp = new int[retru.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = retru[retru.length-i-1];
		}
		for (int i = 0; i < temp.length; i++) {
			retru[i] = temp[i];
		}
		
	}
	public static String calSuffix(int executions, int setSize, double[] p) {
		StringBuffer suffix = new StringBuffer("");
		suffix.append("_"+executions+"_"+setSize);
		for (int i = 0; i < p.length; i++) {
			suffix.append("_"+(int)(p[i]*100));
		}
		return suffix.toString();
	}
	public static double calArraySum(double[] result) {
		double re = 0;
		for (int i = 0; i < result.length; i++) {
			re += result[i];
		}
		return re;
	}
	public static void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void load(String url, double[] record, double last_max) {
		// TODO Auto-generated method stub
		String content = FileOp.readFile2String(url);
		Pattern p = Pattern.compile("([0-9]+)->([0-9]+\\.[0-9]+)");
		Matcher m = p.matcher(content);
		while(m.find()){
			int index = Integer.parseInt(m.group(1));
			double num = Double.parseDouble(m.group(2));
			record[index] = num - last_max;;
			
//			Util.block();
		}
	}

	public static Set<Integer> random(int i, int size, int j) {
		Set<Integer> set = new HashSet<Integer>();
		int node = -1;
		for (int k = 0; k < j; k++) {
			while(set.contains(node = Util.random(i,size))){
				
			}
			set.add(node);
		}
		return set;
	}

	public static void block(String string) {
		System.out.println(string);
		block();
		
	}

	/**
	 * @param number
	 * @param i
	 * @param j
	 * @param testedSet
	 */
	public static void randoms(int number, int i, int j, Set<Integer> testedSet) {
		if(number > (j-i+1)/2){
			for (int k = i; k <= j; k++) {
				testedSet.add(k);
			}
			while(testedSet.size()>number){
				testedSet.remove(random(i, j));
			}
		}else{
			while(testedSet.size()<number){
				testedSet.add(random(i, j));
			}
		}
		
	}

	/**
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日HH时mm分");
		return formatter.format(new Date());


	}
	
	public static void main(String[] args) {
		System.out.println(getCurrentTime());
	}

}
