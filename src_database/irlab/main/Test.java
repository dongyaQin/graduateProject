package irlab.main;

import java.sql.Savepoint;

import irlab.renren.dao.DAOSupport;
import irlab.util.Util;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Savepoint s = null,s1=null;
		try {
			s = DAOSupport.beginTransaction();
			boolean a = DAOSupport.save("user", "*", " (1,1)");
			s1 = DAOSupport.getSavePoint();
			boolean b = DAOSupport.save("user", "*", " (2,1)");
			System.out.println(a+" "+b);
//			DAOSupport.rollbackTransaction(s);
//			if(!b)
				
			;
		} catch (Exception e) {
			System.out.println("出现回滚了！！！！！！！！！！！！！");
			
		}finally{
//			Util.block();
//		DAOSupport.commitTransaction();
			System.out.println("wancheng");
		}

	}

}
