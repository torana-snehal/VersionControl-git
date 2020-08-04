package com.icedq.versioncontrol.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.icedq.versioncontrol.model.ICERule;
import com.icedq.versioncontrol.model.ICERuleClass;
import com.icedq.versioncontrol.model.ICERuleResponse;

public class ICERuleDao {

	private Connection connection;
	private static final String RULEID = "RuleID";
	private static final String RULENAME = "RuleName";
	private static final String RULEDESC = "RuleDesc";
	private static final String RULECLASSID = "RuleClassID";
	private static final String PROPNAME = "PropName";
	private static final String PROPVALUE = "PropValue";
	private static final String VERSION_NO = "VersionNo";
	private static final String COMMITID = "CommitID";
	private static final String CREATION_DATE = "CreationDate";
	private static final String UPDATION_DATE = "UpdationDate";

	public ICERuleDao() {
		connection = SQLUtil.getConnection();
	}

	public void setData(ICERuleResponse response, List<String> latestVerList) {
		try {
			if (connection.isClosed()) {
				connection = SQLUtil.getConnection();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// add creation date and updation date
		try (Statement statement = connection.createStatement()) {

			String writeSyntax = "";
			if (checkRecordsExists(statement, response.getRule().getId()) == 0) {
				writeSyntax = String.format(
						"INSERT INTO dbo.%s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
						"RuleVersionControl", "RuleID", "RuleName", "RuleDesc", "RuleClassID", "PropName", "PropValue",
						"VersionNo", "CommitID", response.getRule().getId(), response.getRule().getName(),
						response.getRule().getDesc(), response.getRClass().getClassId(), response.getRClass().getProp(),
						response.getRClass().getValue(), latestVerList.get(0), latestVerList.get(1));
			} else {
				writeSyntax = String.format("UPDATE dbo.%s SET %s= %s, %s = '%s' WHERE RuleID = %s",
						"RuleVersionControl", "VersionNo", latestVerList.get(0), "CommitID", latestVerList.get(1),
						response.getRule().getId());
			}
			statement.executeUpdate(writeSyntax);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ICERuleResponse getData(String id) {
		ICERuleResponse response = null;
		try {
			if (connection.isClosed()) {
				connection = SQLUtil.getConnection();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try (Statement statement = connection.createStatement()) {
			String querySyntax = String.format("SELECT * FROM dbo.%s WHERE RuleID = %s", "RuleVersionControl", id);
			ResultSet result = statement.executeQuery(querySyntax);

			while (result.next()) {
				ICERule rule = null;
				ICERuleClass rclass = null;

				// new SimpleDateFormat("MM/dd/yyyy").parse(result.getString(CREATION_DATE)),
				// new SimpleDateFormat("MM/dd/yyyy").parse(result.getString(UPDATION_DATE))
				rule = new ICERule(result.getString(RULEID), result.getString(RULENAME), result.getString(RULEDESC),
						null, null);

				rclass = new ICERuleClass(result.getString(RULECLASSID), result.getString(PROPNAME),
						result.getString(PROPVALUE));

				response = new ICERuleResponse(rule, rclass);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	private int checkRecordsExists(Statement stmt, String id) {
		int rowCount = 0;
		try {
			if (connection.isClosed()) {
				connection = SQLUtil.getConnection();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		try {
			String sql = String.format("Select COUNT(*) from RuleVersionControl where RuleID = %s", id);
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			rowCount = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCount;
	}
}
