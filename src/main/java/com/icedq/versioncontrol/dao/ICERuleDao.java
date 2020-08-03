package com.icedq.versioncontrol.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ICERuleDao {

	private Connection connection;

	public ICERuleDao() {
		connection = SQLUtil.getConnection();
	}

	public void setData() {
		
		
	}
	
	public void getData() {
		try {
			if (connection.isClosed()) {
				connection = SQLUtil.getConnection();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try (Statement statement = connection.createStatement()) {
			String querySyntax = String.format("SELECT * FROM dbo.%s", "CUSTOMER");
			ResultSet result = statement.executeQuery(querySyntax);

			ResultSetMetaData rsmd = result.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			// Iterate through the data in the result set and display it.

			while (result.next()) {
				// Print one row
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print(result.getString(i) + " "); // Print one element of a row
				}
				System.out.println();// Move to the next line to print the next row.
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
