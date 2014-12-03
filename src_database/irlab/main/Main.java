package irlab.main;

import irlab.dbOperate.Data;
import irlab.util.Print;
import irlab.util.Util;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
	

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Data data = new Data();
		double[] userIds = data.getUserIds();
		Arrays.sort(userIds);
		Print.print(userIds);
		Util.block();
		Map<Double,Integer> map = new HashMap<Double,Integer>();
		for (int i = 0; i < userIds.length; i++) {
			map.put(userIds[i], i);
		}
		data.getRelationship("relationship");
		data.createTable("normrealations");
		int count = 0;
		int result = 0;
		while(count < data.relationLength){
			System.out.println(count);
			double[][] relation = data.rela2mem(count,count+50000);
			count = count + 50001;
//			Print.print(relation);
			for (int i = 0; i < relation.length; i++) {
				System.out.println(map.get(relation[i][0])+"*********");
				relation[i][0] = map.get(relation[i][0]);
				relation[i][1] = map.get(relation[i][1]);
			}
//			Print.print(relation);
			//将relation关系写到文件中

//			data.write2Database(relation,"normrealations");
			result+=relation.length;
		}
		System.out.println(result);
	}

}
