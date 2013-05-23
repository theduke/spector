package at.theduke.spector.server.db;

import java.sql.*;

import at.theduke.spector.server.ConfigData;

public class Persistor {

	String SQL_CREATE_TABLE = "CREATE TABLE SESSIONS ("
		+ "id VARCHAR(255) NOT NULL,"
		+ "PRIMARY KEY (id),"
		+ "host VARCHAR(255),"
		+ "user VARCHAR(255) NOT NULL"
		+ "ip VARCHAR(200),"
		+ "start_time INT UNSIGNED,"
		+ "end_time INT UNSIGNED,"
		+ "log LONGTEXT;";

	Connection con;

	Connection getConnection(ConfigData config) {
		Connection conn = null;

		String driver = "com.mysql.jdbc.Driver";

		String url = "jdbc:mysql://" + config.mysqlHost + ":" +
		  config.mysqlPort + "/";

		String dbName = config.mysqlDb;

		String userName = config.mysqlUser;
		String password = config.mysqlPassword;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);

			System.out.println("Connected to the database");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	boolean createTable() {
		try {
			if (!MysqlHelper.doesTableExist(con, "sessions")) {
				Statement s = con.createStatement();
				s.executeUpdate(SQL_CREATE_TABLE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public boolean initialize(ConfigData config) {
		con = getConnection(config);

		if (con == null) {
			System.out.println("Could not create MySQL connection");
			return false;
		}

		if (!createTable()) {
			System.out.println("MysqlSessionTable does not exist and could not be created.");
			return false;
		}

		return true;
	}

	public boolean ensureSessionExists() {
		return false;
	}
}
