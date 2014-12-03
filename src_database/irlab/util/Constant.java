package irlab.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 共用的系统或者用户常�?
 * 
 * @author wukai
 * 
 */
public final class Constant {
	/**
	 * 用户常量
	 */
	public static Map<String, String> CONSTANT = new HashMap<String, String>();
	public static String HOST;// host
	public static int PORT;// port
	public static String url = "jdbc:mysql://211.87.234.38:3306/experiment_result";
	// public static String url = "jdbc:mysql://211.87.234.244:3306/renren";
	public static String user = "root";
	public static String password = "root";
}
