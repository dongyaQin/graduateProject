package irlab.jdbc.connection;

import irlab.util.Constant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public final class JDBCConnection {
	private static String url = Constant.url;
	//private static String url = Constant.url;
	private static String user = Constant.user;
	private static String password = Constant.password;

	static{
		if(Constant.CONSTANT.containsKey("jdbcuri"))
			url=Constant.CONSTANT.get("jdbcuri");
		if(Constant.CONSTANT.containsKey("dbaccount"))
			user=Constant.CONSTANT.get("dbaccount");
		if(Constant.CONSTANT.containsKey("dbpass"))
			password=Constant.CONSTANT.get("dbpass");
	}
	private static List<Connection> connectionPool = new ArrayList<Connection>();
 
	private JDBCConnection() {
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() {
		try {
			if (connectionPool.size() > 0) {
				Connection con = connectionPool.get(0);
				if (con.isValid(1000))
					return con;
				else
					connectionPool.remove(con);
			}
			Connection newcon = DriverManager
					.getConnection(url, user, password);
			newcon.setAutoCommit(false);
			connectionPool.add(newcon);
			return newcon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
						connectionPool.remove(conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public static void free(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void free(Statement st) {
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void free(Connection conn) {
		if (conn != null)
			try {
				conn.close();
				connectionPool.remove(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	public static boolean isConnectionValid(Connection con) {
		try {
			if (con == null)
				return false;
			if (con.isClosed())
				return false;
			if (!con.isValid(2000))
				return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}
}
