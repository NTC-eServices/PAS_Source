package lk.informatics.ntc.view.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {

	// Added to trace opened but unclosed jdbc connections
	public static Map<String, StringBuilder> connectionLog = new HashMap<String, StringBuilder>();

	public static Connection getConnection() {
		Connection con = null;
		String username = null;
		String password = null;
		String logSecKey = null; /*new*/

		try {
			logSecKey = PropertyReader.getPropertyValue("password.secretKey"); /*new*/
						
			//////////// Connection Url
			String connectionurl = PropertyReader.getPropertyValue("connection.url");
			if (connectionurl != null) {
				connectionurl = connectionurl.trim();
			}

			username = PropertyReader.getPropertyValue("connection.username");
			if (username != null) {
				username = username.trim();
				username = AES.decrypt(username, logSecKey); /*new*/
			}

			password = PropertyReader.getPropertyValue("connection.password");
			if (password != null) {
				password = password.trim();
				password = AES.decrypt(password, logSecKey); /*new*/
			}

			String driverclass = PropertyReader.getPropertyValue("driver.class");

			Class.forName(driverclass);

			con = DriverManager.getConnection(connectionurl, username, password);
			con.setAutoCommit(false);
			con.setSchema("public");

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			connectionLog.put(getConnectioId(con.toString()), getTraceString(stackTraceElements));
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
		}
		return con;
	}

	public static Connection getAdminConnection() {
		Connection con = null;
		String username = null;
		String password = null;

		try {
			//////////// Connection Url
			String connectionurl = PropertyReader.getPropertyValue("connection.url");
			if (connectionurl != null) {
				connectionurl = connectionurl.trim();
			}

			//////////// User Name
			username = PropertyReader.getPropertyValue("connection.username");
			if (username != null) {
				username = username.trim();
			}

			//////////// Password
			password = PropertyReader.getPropertyValue("connection.password");
			if (password != null) {
				password = password.trim();
			}

			String driverclass = PropertyReader.getPropertyValue("driver.class");

			Class.forName(driverclass);

			con = DriverManager.getConnection(connectionurl, username, password);
			con.setAutoCommit(false);
			con.setSchema("main");

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			connectionLog.put(getConnectioId(con.toString()), getTraceString(stackTraceElements));
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
		}
		return con;
	}

	/**
	 * Note: All opened connections should use this method to close its jdbc
	 * connection.
	 * 
	 */
	public static void close(Connection con) {
		try {
			if (con != null)
				connectionLog.remove(getConnectioId(con.toString()));
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rollBack(Connection con) {
		try {
			if (con != null)
				con.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void commit(Connection con) {
		try {
			if (con != null)
				con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getConnectioId(String connectionToString) {
		return connectionToString.substring(connectionToString.indexOf("@"));
	}

	private static StringBuilder getTraceString(StackTraceElement[] elements) {
		StringBuilder trace = new StringBuilder();
		try {
			for (int i = 2; i < 4; i++) {
				trace.append(" ClassName:").append(elements[i].getClassName()).append(" MethodName:")
						.append(elements[i].getMethodName()).append(" LineNo:").append(elements[i].getLineNumber())
						.append("\n");
			}
		} catch (Exception e) {
			trace.append("Unable to trace");
		}
		return trace;
	}

}
