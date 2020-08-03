package com.icedq.versioncontrol.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLUtil {

	public static final String sqlUrl = "jdbc:sqlserver://toranainc.database.windows.net:1433;database=testdata;user=toranasqladmin;password=This15icedq@zur3;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	
	public SQLUtil() {

	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(sqlUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String escape(String str) {
		return str.replaceAll("'", "''");
	}
}
