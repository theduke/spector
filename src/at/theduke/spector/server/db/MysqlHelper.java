package at.theduke.spector.server.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlHelper {
	
	public static boolean doesTableExist(Connection con, String table) throws SQLException {
		DatabaseMetaData dbm = con.getMetaData();
		// check if "employee" table is there
		ResultSet tables = dbm.getTables(null, null, table, null);
		
		return tables.next();
	}
}
