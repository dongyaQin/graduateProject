package sdu.ir.interfaces;

import java.util.Set;

public interface Graph {
	public int getWeight(int i, int j);
	public void setWeight(int i, int j, int weight);
	public int size();
	public Set<Integer> getNeighbor(int i);
	public int getIndegree(int i);
	public int getOutdegree(int i);
	public Set<Integer> getInNeighbor(int i);
}
