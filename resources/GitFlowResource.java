package com.icedq.versioncontrol.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icedq.versioncontrol.dao.ICERuleDao;
import com.icedq.versioncontrol.gitFlow.GitOperations;
import com.icedq.versioncontrol.model.Customer;
import com.icedq.versioncontrol.model.ICERuleResponse;
import com.icedq.versioncontrol.model.RuleVersions;


public class GitFlowResource {

	GitOperations gitOperations;
	ICERuleDao iceRuleDao;
	
	public GitFlowResource() throws JsonMappingException, JsonProcessingException {
		gitOperations = new GitOperations();
		iceRuleDao = new ICERuleDao();
	}

	public ICERuleResponse getData(String id) {
		ICERuleResponse response = null;
		response = iceRuleDao.getData(id);
		/*
		 * ObjectMapper objMapper = new ObjectMapper(); try { //get data from db
		 * ByteArrayOutputStream stream = gitOperations.getFileData(filename); response
		 * = objMapper.readValue(stream.toString(), ICERuleResponse.class); } catch
		 * (JsonMappingException e) { e.printStackTrace(); } catch
		 * (JsonProcessingException e) { e.printStackTrace(); }
		 */

		return response;
	}
	
	public void addRuleDataToRepo(ICERuleResponse ruleResponse) {
		Customer cust = new Customer("John", "Cook", "testing@gmail.com");
		List<String> latestVerList = gitOperations.addFileToRepo(cust, ruleResponse);
		iceRuleDao.setData(ruleResponse, latestVerList);
	}

	public List<RuleVersions> getAllRuleVersions() {
		List<RuleVersions> versionList = gitOperations.getAllRuleVersions();
		return versionList;
	}

	public ICERuleResponse getDataFromCommitId(String verNo) throws IOException {
		ICERuleResponse response = null;

		ObjectMapper objMapper = new ObjectMapper();
		try {
			String fileName = "Ruledata.json";
			ByteArrayOutputStream stream = gitOperations.readFileContentsFromCommitId(verNo, fileName);
			response = objMapper.readValue(stream.toString(), ICERuleResponse.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return response;
	}
}
