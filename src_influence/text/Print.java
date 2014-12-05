package text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import sdu.ir.interfaces.Graph;

public class Print{
	static Scanner scan = new Scanner(System.in);
	public static void print(int[][] edges) {
		System.out.println("------------------------------------");
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges[0].length; j++) {
				System.out.print(edges[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
		
	}
	
	public static void print(byte[][] edges) {
		System.out.println("------------------------------------");
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges[0].length; j++) {
				System.out.print(edges[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
		
	}

	public static void print(double[][] edges) {
		System.out.println("------------------------------------");
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges[0].length; j++) {
				System.out.print(edges[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
		
	}

	public static void print(int[] father) {
		System.out.println("------------------------------------");
		for (int i = 0; i < father.length; i++) {
			System.out.print(father[i]+" ");
		}
		System.out.println();
		System.out.println("------------------------------------");
		
	}

	public static void print(double[] father) {
		System.out.println("------------------------------------");
		for (int i = 0; i < father.length; i++) {
			System.out.print(father[i]+" ");
		}
		System.out.println();
		System.out.println("------------------------------------");
		
	}

	public static void print(int[] num, int j) {
		System.out.println("------------------------------------");
		for (int i = j; i < num.length; i++) {
			System.out.print(num[i]+" ");
		}
		System.out.println();
		System.out.println("------------------------------------");
		
	}

	public static void print(char[][] graph) {
		System.out.println("------------------------------------");
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[0].length; j++) {
				System.out.print(graph[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
		
	}

	public static void print(Graph matrix) {
		System.out.println("------------------------------------");
		for (int i = 0; i < matrix.size(); i++) {
			System.out.print((i)+"--->"+matrix.getNeighbor(i));
			
//			scan.next();
			System.out.println();
		}
		System.out.println("------------------------------------");
		
		
	}
	
	public static void print(Graph matrix, String ma) {
		System.out.println("------------------------------------");
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.size(); j++) {
				System.out.print(matrix.getWeight(i, j)+" ");
			}
			
//			scan.next();
			System.out.println();
		}
		System.out.println("------------------------------------");
		
		
	}

	public static void print(boolean[] num,int j) {
		System.out.println("------------------------------------");
		for (int i = j; i < num.length; i++) {
			System.out.print(num[i]+" ");
		}
		System.out.println();
		System.out.println("------------------------------------");
		
	}

	public static void print(Collection collection) {
		System.out.println("------------------------------------");
		System.out.println(collection);
		System.out.println("------------------------------------");
		
		
	}

	public static void print(ArrayList<String[]> list) {
		System.out.println("------------------------------------");
		for (Object[] objects : list) {
			for (Object object : objects) {
				System.out.print(object.toString()+",");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
	}
	
	public static void print(double[] thresholds, int b, int e) {
		System.out.println("------------------------------------");
		for (int i = b; i <= e; i++) {
			System.out.println(thresholds[i]);
		}
		System.out.println("------------------------------------");
		
	}

	public static void print(Object[] contents) {
		System.out.println("------------------------------------");
		for (int i = 0; i < contents.length; i++) {
			System.out.print(contents[i]+"*");
		}
		System.out.println();
		System.out.println("------------------------------------");
	}
	
	
}
