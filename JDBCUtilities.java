package com.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class JDBCUtilities {
	static Connection conn;
	static boolean flag = true;

	static Connection conn() {
		try {
			System.out.println(Thread.currentThread().getName() + " enters into JDBCUtilites conn");
			if (conn == null) {
				synchronized (JDBCUtilities.class) {
					if (flag) {
						flag = false;
						System.out.println(
								Thread.currentThread().getName() + " enters into synchronized JDBCUtilites conn");
						Properties props = new Properties();
						props.put("user", "e5570443");
						props.put("password", "Nay#ka2n");
						Class.forName("oracle.jdbc.driver.OracleDriver");
						conn = DriverManager.getConnection("jdbc:oracle:thin:@d05jbdbs04.metavante.com:1550:EB01",
								props);
					}
				}
			}
		} catch (Exception g) {
			g.printStackTrace();
		}
		return conn;
	}

	static Connection getConnFromPool() {
		Connection connection = null;
		ConnectionFactory factory = new ConnectionFactory("oracle.jdbc.driver.OracleDriver",
				"jdbc:oracle:thin:@d05jbdbs04.metavante.com:1550:EB01", "e5570443", "Nay#ka2n");
		System.out.println(Thread.currentThread().getName() + " enters into JDBCUtilites getConnFromPool");
		try {
			connection = factory.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
}

class ConnectionFactory {
	private GenericObjectPool connectionPool;

	public ConnectionFactory(String driverClass, String url, String user, String pass) {
		PoolableConnectionFactory connFactory = new PoolableConnectionFactory(driverClass, url, user, pass);
		connectionPool = new GenericObjectPool(connFactory);
		connectionPool.setMaxActive(10);
		connectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
		connectionPool.setMaxWait(1);
	}

	public Connection getConnection() throws Exception {
		Connection conn = null;
		try {
			conn = ((Connection) connectionPool.borrowObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}

class PoolableConnectionFactory extends BasePoolableObjectFactory {
	private String driverClass, url, user, pwd, encryptionClient, encryptionTypesClient;

	public PoolableConnectionFactory(String driverClass, String url, String user, String pwd) {
		this.driverClass = driverClass;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.encryptionClient = encryptionClient;
		this.encryptionTypesClient = encryptionTypesClient;
	}

	public Object makeObject() throws Exception {
		Properties props = new Properties();
		props.put("user", user);
		props.put("password", pwd);
		Connection conn = DriverManager.getConnection(url, props);
		return conn;
	}
}
