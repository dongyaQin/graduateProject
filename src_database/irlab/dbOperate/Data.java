package irlab.dbOperate;

import irlab.renren.dao.DAOSupport;
import irlab.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sdu.ir.util.IMMethod;

public class Data {

	public int relationLength;
	ArrayList<Double> list1;
	ArrayList<Double> list2;
	public double[] getUserIds() throws SQLException {
		ResultSet rs = DAOSupport.executeQuery("select userID from user");
		ArrayList<Double> list = new ArrayList<Double>();
		while (rs.next()) {
			double temp = Double.parseDouble(((String) rs.getObject("userID")));
			list.add(temp);
		}
		
		return list2Array(list);
	}

	private double[] list2Array(ArrayList<Double> list) {
		double[] arr = new double[list.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	public double[][] getRelationship(String table) throws SQLException {
		ResultSet rs = DAOSupport.executeQuery("select userID,friendID from "+table);
		list1 = new ArrayList<Double>();
		list2 = new ArrayList<Double>();
		while (rs.next()) {
			double temp1 = Double.parseDouble(((String) (rs.getObject("userID")+"")));;
			double temp2 = Double.parseDouble(((String) (rs.getObject("friendID")+"")));;
			list1.add(temp1);
			list2.add(temp2);
		}
		this.relationLength = list1.size();
		if(list1.size() < 100000)
			return list2Array(list1,list2);
		else
			return null;
	}

	private double[][] list2Array(ArrayList<Double> list1,
			ArrayList<Double> list2) {
		double[][] arr = new double[list1.size()][2];
		for (int i = 0; i < arr.length; i++) {
			arr[i][0] = list1.get(i);
			arr[i][1] = list2.get(i);
		}
		return arr;
	}

	public void createTable(String table) {
		String sql = "CREATE TABLE "+table+" (k int(20) NOT NULL,influence double ,nodes longtext, PRIMARY KEY (k)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		System.out.println(sql);
//		System.exit(0);
		try {
			DAOSupport.createTable(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void write2Database(int sizeOfInitSet, double influence, Set initSet, String table) {
		Savepoint s = null;
		try {
			s = DAOSupport.beginTransaction();
			DAOSupport.save(table, "*", " ("+sizeOfInitSet+","+influence+", '"+initSet.toString()+"')");
			DAOSupport.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			Util.block();
			DAOSupport.rollbackTransaction(s);
		}
		
		
	}
	
	public void update2Database(int sizeOfInitSet, double influence, Set initSet, String table) {
		Savepoint s = null;
		try {
			s = DAOSupport.beginTransaction();
			DAOSupport.update(table, "influence="+influence,"k="+sizeOfInitSet);
			DAOSupport.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			Util.block();
			DAOSupport.rollbackTransaction(s);
		}
		
		
	}

	public double[][] rela2mem(int i, int j) {
		if(j >= this.relationLength){
			j = this.relationLength-1;
		}
		double[][] arr = new double[j-i+1][2];
		int m = 0;
		for (int k = i; k <= j; k++) {
			arr[m][0] = list1.get(k);
			arr[m][1] = list2.get(k);
			m++;
		}
		return arr;
	}

	public int createTables(String dataName, String prefix, String suffix, Map<IMMethod, String> map) {
		String greedy = prefix+dataName+"_greedy"+suffix;
		String tllfGreedy = prefix+dataName+"_tllfgreedy"+suffix;
		String neiborNumGreedy = prefix+dataName+"_neiborNumGreedyE"+suffix;
		String degreeDiscount = prefix+dataName+"_degreeDiscountIC"+suffix;
		String random = prefix+dataName+"_random"+suffix;
		map.put(IMMethod.Greedy, greedy);
		map.put(IMMethod.NeiborNumGreedy, neiborNumGreedy);
		map.put(IMMethod.Random, random);
		map.put(IMMethod.SingleDiscount, degreeDiscount);
		map.put(IMMethod.TLLFGreedy, tllfGreedy);
		this.createTable(greedy);
		this.createTable(tllfGreedy);
		this.createTable(neiborNumGreedy);
		this.createTable(degreeDiscount);
		this.createTable(random);
		return 1;
	}
	
	public void deleteTables(String dataName, String suffix){
		
		DAOSupport.deleteTable("drop table "+dataName+"_greedy"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_highDegree"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_communityDetectionHighdgree"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_communityDetectionGreedy"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_neiborIntensity"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_neiborWeightNum"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_neiborIntensityOverlap"+suffix);
		DAOSupport.deleteTable("drop table "+dataName+"_neiborWeightNumOverlap"+suffix);
		
	}
	
	public static void main(String[] args) {
//		DiffusionSimulate ds = new DiffusionSimulate(5);
		Data data = new Data();
//		data.deleteTables("di_soc_Epinions1", "_4000_50_50_30_10");
	}

	public String getMatalabContent(String tableName) {
		String sql = "select k,influence from "+tableName+" order by k asc ";
		ResultSet rs = DAOSupport.executeQuery(sql);
		ArrayList<Double> list = new ArrayList<Double>();
		StringBuffer sb = new StringBuffer("");
		try {
			while (rs.next()) {
				double temp = (Double) rs.getObject("influence");
				sb.append(temp+"\t");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	public void createTables(String dataName) {
		this.createTable(dataName+"_greedy");
		this.createTable(dataName+"_lbm");
		this.createTable(dataName+"_ubm");
		
	}

	public ArrayList<Set<Integer>> getSeedSet(String tableName) {
		String sql = "select nodes from "+tableName+" order by k asc ";
		ResultSet rs = DAOSupport.executeQuery(sql);
		ArrayList<Set<Integer>> list = new ArrayList<Set<Integer>>();
		StringBuffer sb = new StringBuffer();
		try {
			while (rs.next()) {
				String temp = (String) rs.getObject("nodes");
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(temp);
//				System.out.println(temp);
				Set<Integer> set = new HashSet<Integer>();
				while(m.find()){
					int node = Integer.parseInt(m.group());
					set.add(node);
				}
				list.add(set);
			
//				System.out.println(temp);
//				sb.append(temp+"\t");
			}
//			Util.block();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}


}
