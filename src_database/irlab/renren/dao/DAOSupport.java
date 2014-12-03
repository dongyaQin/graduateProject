package irlab.renren.dao;

import irlab.jdbc.connection.JDBCConnection;
import irlab.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class DAOSupport {
	private static int sqlCount = 0;
	private static int MAXSQL = 10000;
	private static Statement st = null;

	public static Savepoint beginTransaction() {
		checkSqlCount();
		// if (!isConnectionValid(con))
		// return null;

		try {
			return JDBCConnection.getConnection().setSavepoint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void commitTransaction() {
		// if (!isConnectionValid(con))
		// return;

		try {
			JDBCConnection.getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollbackTransaction(Savepoint savepoint) {
		// if (!isConnectionValid(con))
		// return;

		if (savepoint == null)
			return;
		try {
			JDBCConnection.getConnection().rollback(savepoint);
			JDBCConnection.getConnection().releaseSavepoint(savepoint);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save,delete,update
	 * 
	 * @param con
	 * @param sql
	 * @throws SQLException 
	 */
	public static boolean executeUpdate(String sql) throws SQLException {
		sqlCount++;
		// if (!isConnectionValid(con))
		// return;

		// System.err.println(sql);
		
			// 锟斤拷锟斤拷锟斤拷锟�
			st = JDBCConnection.getConnection().createStatement();
			// 执锟斤拷锟斤拷锟�
			st.executeUpdate(sql);
			JDBCConnection.free(st);

		return true;
	}

	/**
	 * batch save,update,delete
	 * 
	 * @param con
	 * @param sql
	 */
	public static void executeBatch(String[] sql) {
		sqlCount++;
		try {
			// 锟斤拷锟斤拷锟斤拷锟�
			st = JDBCConnection.getConnection().createStatement();
			// 执锟斤拷锟斤拷锟�
			for (String s : sql){
				st.addBatch(s);
			}
			st.executeBatch();
			JDBCConnection.free(st);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * select
	 * 
	 * @param con
	 * @param sql
	 * @return
	 */
	public static ResultSet executeQuery(String sql) {
		sqlCount++;
		checkSqlCount();
		// if (!isConnectionValid(con))
		// return null;
		try {
			// 锟斤拷锟斤拷锟斤拷锟�
			st = JDBCConnection.getConnection().createStatement();
			// 执锟斤拷锟斤拷锟�
			ResultSet rs = st.executeQuery(sql);
			// JDBCConnection.free(st);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 某些锟斤拷锟斤拷锟�支锟街该凤拷锟斤拷
	 * 
	 * @param tablename
	 * @param columns
	 * @param values
	 */
	// public static void save_delayed(String tablename, String columns, String
	// values) {
	// String sql = "";
	// if (columns.equalsIgnoreCase("*"))
	// sql = "INSERT DELAYED INTO " + tablename + " values"
	// + values + ";";
	// else
	// sql = "INSERT DELAYED INTO " + tablename + " (" + columns + ") values"
	// + values + ";";
	// //System.out.println(sql);
	// executeUpdate(sql);
	// }
	/**
	 * 
	 * @param tablename
	 * @param columns
	 * @param values
	 * @return 
	 * @throws SQLException 
	 */
	public static boolean save(String tablename, String columns, String values) throws SQLException {
		String sql = "";
		if (columns.equalsIgnoreCase("*"))
			sql = "INSERT INTO " + tablename + " values" + values + ";";
		else
			sql = "INSERT INTO " + tablename + " (" + columns + ") values"
					+ values + ";";
		System.err.println(sql);
		return executeUpdate(sql);
	}

	/**
	 * 
	 * @param tablename
	 * @param where
	 * @throws SQLException 
	 */
	public static void delete(String tablename, String where) throws SQLException {
		String sql = "DELETE FROM " + tablename + " WHERE " + where + ";";
		executeUpdate(sql);
	}

	/**
	 * 
	 * @param tablename
	 * @param values
	 * @param where
	 * @throws SQLException 
	 */
	public static void update(String tablename, String values, String where) throws SQLException {
		String sql = "UPDATE " + tablename + " SET " + values + " WHERE "
				+ where + ";";
		// System.out.println(sql);
		executeUpdate(sql);
	}

	/**
	 * 
	 * @param tablename
	 * @param where
	 * @return
	 */
	public static ResultSet find(String tablename, String columns,
			String where, int begin, int n, String orderby) {
		String sql = "SELECT " + columns + " FROM " + tablename + " WHERE "
				+ where + " " + orderby + "  limit " + begin + "," + n + ";";
		// System.err.println(sql);
		ResultSet rs = executeQuery(sql);
		return rs;
	}

	/**
	 * 
	 * @param md5
	 * @param columns
	 * @param cls
	 * @return
	 */
//	public List<ExtractRs> findByMd5(String md5, String tablename,
//			String columns, Class<?> cls, int begin, int n, String orderby) {
//		sqlCount++;
//		List<ExtractRs> list = new ArrayList<ExtractRs>();
//		try {
//			String sql = "SELECT " + columns + " FROM " + tablename
//					+ " WHERE md5=? " + orderby + "  limit " + begin + "," + n
//					+ ";";
//			PreparedStatement ps = JDBCConnection.getConnection()
//					.prepareStatement(sql);
//			ps.setString(1, md5);
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				ExtractRs tmp = (ExtractRs) cls.newInstance();
//				tmp.build(rs);
//				list.add(tmp);
//			}
//			JDBCConnection.free(ps);
//			JDBCConnection.free(rs);
//			checkSqlCount();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return list;
//		}
//		return list;
//	}

	public static void freeStatement() {
		// long begin = System.currentTimeMillis();
		try {
			if (st != null && !st.isClosed())
				JDBCConnection.free(st);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// long end = System.currentTimeMillis();
		//
		// System.out.println("free statement cost:" + (end - begin));
	}

	private static void checkSqlCount() {
		if (sqlCount > MAXSQL) {
			JDBCConnection.free(JDBCConnection.getConnection());
			sqlCount = 0;
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void createTable(String sql) throws SQLException {
		executeUpdate(sql);
		
	}

	public static Savepoint getSavePoint() throws SQLException {
		return JDBCConnection.getConnection().setSavepoint();
	}

	public static void deleteTable(String sql) {
		try {
			executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
