package sdu.ir.storegraph;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.interfaces.Graph;
import sdu.ir.util.ChainNode;

public class AdjacencyListWithoutWeight implements Graph{
	Set<Integer>[] list;
	
	public AdjacencyListWithoutWeight(int length) {
		list = new HashSet[length];
		for (int i = 0; i < list.length; i++) {
			list[i] = new HashSet<Integer>();
		}
	}
	
	@Override
	public int getWeight(int i, int j) {
		Set<Integer> temp = list[i];
		if(temp.contains(j))
			return 1;
		else
			return 0;
	}

	@Override
	public void setWeight(int i, int j, int weight) {
	}

	@Override
	public int size() {
		return list.length;
	}

	@Override
	public Set<Integer> getNeighbor(int i) {
		return list[i];
	}

	@Override
	public int getIndegree(int i) {
		// TODO Auto-generated method stub
		return getInNeighbor(i).size();
	}

	@Override
	public int getOutdegree(int i) {
		return list[i].size();
	}

	public void add(int i, int j, int k) {
		list[i].add(j);
		
	}

	@Override
	public Set<Integer> getInNeighbor(int i) {
		Set<Integer> r = new HashSet<Integer>();
		for (int j = 0; j < list.length; j++) {
			if(j != i){
				Set<Integer> temp = list[j];
				if(temp.contains(i)){
					r.add(j);
				}
			}
		}
		return r;
	}

}
