package sdu.ir.storegraph;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.interfaces.Graph;

public class AdjacentMatrix implements Graph{

	private int[][] graph;
	public AdjacentMatrix() {
		// TODO Auto-generated constructor stub
	}
	
	public AdjacentMatrix(int row, int column) {
		graph = new int[row][column];
	}
	
	public int getWeight(int i, int j){
		return graph[i][j];
	}
	
	public void setWeight(int i, int j, int weight){
		graph[i][j] = weight;
	}
	
	public double getEdges() {
		double temp = 0;
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[i].length; j++) {
				if(graph[i][j] > 0){
					temp++;
				}
			}
		}
		return temp;
	}
	
	public boolean isSymmetry(){
		boolean b = true;
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[0].length; j++) {
				if(graph[i][j] == 1 && graph[j][i] != 1){
					b = false;
				}
			}
		}
		return b;
	}
	
	public int[][] getGraph() {
		return graph;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		return graph.length;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set getNeighbor(int i) {
		Set temp = new HashSet();
		for (int j = 0; j < graph.length; j++) {
			if(graph[i][j] > 0)
				temp.add(j);
		}
		return temp;
	}

	@Override
	public int getIndegree(int i) {
		int count = 0;
		for (int j = 0; j < graph[i].length; j++) {
			if(graph[j][i] > 0)
				count++;
		}
		return count;
	}

	@Override
	public int getOutdegree(int i) {
		int count = 0;
		for (int j = 0; j < graph.length; j++) {
			if(graph[i][j] > 0)
				count++;
		}
		return count;
	}

	@Override
	public Set<Integer> getInNeighbor(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
