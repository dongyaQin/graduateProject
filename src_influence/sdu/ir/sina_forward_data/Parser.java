package sdu.ir.sina_forward_data;

import io.FileOp;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sdu.ir.util.Util;
import text.Print;

public class Parser {
	private String reg = "";
	public class Node{
		public Node(int denominator, double apd2) {
			this.num_infect = denominator;
			this.apd = apd2;
		}
		int num_infect;
		double apd;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public ArrayList<String[]> parse(File file) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		String content = FileOp.readFile2String(file.getAbsolutePath(),"utf-8");
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(content);
		while(m.find()){
			String s = m.group(1);
//			System.out.println(s);
			String[] temp = s.split("\t");
			list.add(temp);
		}
		return list;
	}
	//APD average propagation distance
	public Node calAPD(File file) {
		ArrayList<String[]> list = this.parse(file);
		int[] record = new int[100];
		for (String[] ss : list) {
			record[ss.length] ++;
		}
		int i = 1;
		int numerator = 0;
		int denominator = 0;
		while(record[i] != 0){
			numerator += record[i]*i;
			denominator += record[i];
			i++;
//			if(i>18){
//				Util.block();
//			}
		}
		if(denominator > 500){
			int j = 1;
			int fenzi = 0;
			int fenmu = 0;
			while(record[j] != 0){
				fenmu += record[j];
				if(j > 3){
					fenzi += record[j];
				}
				System.out.print(record[j]+",");
				j++;
			}
			System.out.print("-->"+(double)fenzi/fenmu);
			if((double)fenzi/fenmu > 0.2){
				System.out.println(file.getAbsolutePath());
			}
			System.out.println();
		}
		denominator ++;//初始传播节点
		double apd = numerator/(double)denominator;
		return new Node(denominator,apd);
		
		
	}

	public double calProportion(File file, int num) {
		ArrayList<String[]> list = this.parse(file);
		int[] record = new int[100];
		for (String[] ss : list) {
			record[ss.length] ++;
		}
		int i = 1;
		int numerator = 0;
		int denominator = 0;
		while(record[i] != 0){
			denominator += record[i];
			if(i>=num){
				numerator += record[i];
			}
			i++;
		}
		if(denominator != 0){
//			if((double)numerator/(double)denominator >0.1){
//				System.err.println(file.getName());
//				Util.block();
//			}
			return (double)numerator/(double)denominator;
		}

		
		return -1;
	}

}
