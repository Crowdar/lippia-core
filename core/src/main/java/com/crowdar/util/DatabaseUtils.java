package com.crowdar.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowdar.core.PropertyManager;

public class DatabaseUtils {

	private static Connection getConnection(String database) throws SQLException {
		return DriverManager.getConnection(	
											PropertyManager.getProperty("db.connection.string") + database, 
											PropertyManager.getProperty("db.connection.user"),
											PropertyManager.getProperty("db.connection.pass") 
									       );
	}
	
	public static List<Map<String, Object>> executeQuery(String databaseName, String queryString) throws SQLException {
		List<Map<String, Object>> records = null;
		ResultSet rs = null;
		try {
			try (Statement st = getConnection(databaseName).createStatement()) {
				rs = st.executeQuery(queryString);
				if(rs!=null) {
					ResultSetMetaData md = rs.getMetaData();
					int columns = md.getColumnCount();
					// list to have all rows
					records = new ArrayList<>(columns);
					// load data for each column / record
					while (rs.next()) {
						HashMap<String, Object> row = new HashMap<>(columns);
						// add column/value for a record
				        for (int i = 1; i <= columns; ++i) {
				        	row.put(md.getColumnName(i), rs.getObject(i));
				        }
				        records.add(row);
					}
				}
			}
		} catch (SQLException e) {
			throw e;
		}finally {
			try {
				getConnection(databaseName).close();
			} catch (SQLException e) {
				throw e;
			}
		}
		
		return records;
	}

	public static String uniqueResult(String databaseName, String queryString) throws SQLException {
		ResultSet rs = null;
		try {
			try (Statement st = getConnection(databaseName).createStatement()) {
				rs = st.executeQuery(queryString);
				rs.next();
				return rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			throw e;
		}finally {
			try {
				getConnection(databaseName).close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	
	public static boolean executeUpdate(String databaseName, String ddlString) throws SQLException {
		int recAffected = 0;
		try (Statement st = getConnection(databaseName).createStatement()) {
			recAffected = st.executeUpdate(ddlString);
		} catch (SQLException e) {
			throw e;
		}finally {
			try {
				getConnection(databaseName).close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return recAffected>0;	
	}
}
