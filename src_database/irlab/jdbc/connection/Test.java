package irlab.jdbc.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JDBCConnection.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			st = conn.createStatement();
			// 4.执行语句
			rs = st.executeQuery("select * from user");
			// 5.处理结果
			while (rs.next()) {
				// 参数中的1,2,3,4是指sql中的列索引
				System.out.println(rs.getObject(1) + "/t" + rs.getObject(2)
						+ "/t" + rs.getObject(3) + "/t" + rs.getObject(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.free(rs, st, conn);
		}
	}

}
