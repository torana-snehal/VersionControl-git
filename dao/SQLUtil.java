package com.icedq.versioncontrol.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLUtil {
	
	public SQLUtil() {

	}

	public static Connection getConnection() {
		try {
			//register driver before retrieving connection
			Class.forName(Constants.CLASS_NAME).getConstructor().newInstance();
			return DriverManager.getConnection(Constants.SQL_URL);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
