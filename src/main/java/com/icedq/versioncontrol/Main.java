package com.icedq.versioncontrol;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icedq.versioncontrol.dao.ICERuleDao;
import com.icedq.versioncontrol.fileOperations.FileOperations;
import com.icedq.versioncontrol.gitFlow.GitOperations;
import com.icedq.versioncontrol.gitFlow.RuleMetadata;
import com.icedq.versioncontrol.model.Customer;
import com.icedq.versioncontrol.model.ICERule;
import com.icedq.versioncontrol.model.ICERuleClass;
import com.icedq.versioncontrol.model.ICERuleResponse;
import com.icedq.versioncontrol.model.RuleVersions;

public class Main {

	public static void main(String[] args) throws IOException {
		
		
		//SQL
		ICERuleDao dao = new ICERuleDao();
		dao.getData();
		
		
		
		
		
		
		//GitOperations opr = new GitOperations();

		// get all versions
		// List<RuleVersions> allVer = opr.getAllRuleVersions();

//		for (RuleVersions ver : allVer) {
//			System.out.println(ver.getVersion());
//		}

		// Read file from git
		//ByteArrayOutputStream stream = opr.readFileContentsFromCommitId("2", "Ruledata.json");

		// String filename = "Ruledata.json";
		// ByteArrayOutputStream stream = opr.getFileData(filename);

		/*
		 * System.out.println(stream); ICERuleResponse response = null; ObjectMapper
		 * objMapper = new ObjectMapper(); try { response =
		 * objMapper.readValue(stream.toString(), ICERuleResponse.class);
		 * System.out.println(response.toString()); } catch (JsonMappingException e) {
		 * e.printStackTrace(); } catch (JsonProcessingException e) {
		 * e.printStackTrace(); }
		 * 
		 * System.out.println(response.getRule().toString());
		 */

		// Add file to Git

		/*
		 * Customer cust = new Customer("John", "Cook", "testing@gmail.com"); ICERule
		 * rule = new ICERule("111", "Customer Test Rule", "Records customer Test", new
		 * Date(), new Date()); ICERuleClass ruleClass = new ICERuleClass("100",
		 * "Test Mode", "Testing@909");
		 * 
		 * opr.addFileToRepo(cust, new ICERuleResponse(rule, ruleClass));
		 */
	}

}
