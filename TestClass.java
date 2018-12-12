package com.jdbc.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestClass extends Thread {

	@Override
	public void run() {
		getCSTUser();
	}

	public Connection getConn() {
		return JDBCUtilities.conn();
	}

	public static void closeConn(Connection conn) {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public void getCSTUser() {
		Connection conn = getConnectionsFromPool();
		try {
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt
					.executeQuery("select user_id,user_password from users_v3 where user_id= upper('e5570443')");
			if (resultSet.next()) {
				String userID = (String) resultSet.getString(1);
				String userPass = (String) resultSet.getString(2);
				System.out.println(userID + "   " + userPass);
			}
		} catch (Exception g) {
			g.printStackTrace();
		} finally {
			closeConn(conn);
		}
	}

	static Connection getConnectionsFromPool() {
		Connection conn = JDBCUtilities.getConnFromPool();
		return conn;
	}

	public static void main(String[] args) {
		// getConnectionsFromPool();

		TestClass class1 = new TestClass();
		TestClass class2 = new TestClass();
		TestClass class3 = new TestClass();
		TestClass class4 = new TestClass();
		class1.setName("Thread 1");
		class2.setName("Thread 2");
		class3.setName("Thread 3");
		class4.setName("Thread 4");
		class1.start();
		class2.start();
		class3.start();
		class4.start();

	}

}
