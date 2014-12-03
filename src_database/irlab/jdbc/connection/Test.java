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
			// 2.��������
			conn = JDBCConnection.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.�������
			st = conn.createStatement();
			// 4.ִ�����
			rs = st.executeQuery("select * from user");
			// 5.������
			while (rs.next()) {
				// �����е�1,2,3,4��ָsql�е�������
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
