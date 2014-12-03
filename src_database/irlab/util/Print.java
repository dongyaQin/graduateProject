package irlab.util;


public class Print {

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
	
	public static void print(float[][] edges) {
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

	public static void print(int[] father, int begin, int end) {
		System.out.println("------------------------------------");
		for (int i = begin; i < end; i++) {
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
	
}
