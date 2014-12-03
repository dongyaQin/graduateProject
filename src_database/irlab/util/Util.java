package irlab.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class Util {
	static Scanner scan = new Scanner(System.in);
	static public void block() {
		System.out.println("ÇëÊäÈë×Ö·û");
		scan.next();

	}

	public static int random(int i, int j) {
		Random r = new Random();
		return r.nextInt(j-i+1)+i;
	}
	
	private static boolean panduan(int[] a, int[] b) {
		for (int i = 0; i < b.length; i++) {
			if(a[i] != b[i])return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		int[] a = {0, 136, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 130, 12, 13, 14, 15, 17, 16, 19, 18, 21, 20, 23, 22, 25, 24, 27, 26, 29, 28, 31, 30, 34, 35, 32, 33, 38, 39, 36, 37, 42, 40, 315, 41, 45, 428, 332, 90};
		int[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 16, 19, 18, 21, 20, 23, 22, 25, 24, 27, 26, 29, 28, 31, 30, 34, 35, 32, 33, 38, 39, 36, 37, 42, 40, 41, 45, 90, 136, 130, 315, 332, 428};
		Arrays.sort(a);
		Arrays.sort(b);
		Print.print(a);
		Print.print(b);
		System.out.println(panduan(a,b));
			
	}
}
