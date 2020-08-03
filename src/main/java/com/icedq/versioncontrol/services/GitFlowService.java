package com.icedq.versioncontrol.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.icedq.versioncontrol.model.ICERuleResponse;
import com.icedq.versioncontrol.model.RuleVersions;
import com.icedq.versioncontrol.resources.GitFlowResource;


@Path("/resource")
public class GitFlowService {
	
	GitFlowResource gitFlow;
	
	public GitFlowService() throws JsonMappingException, JsonProcessingException {
		gitFlow = new GitFlowResource();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ICERuleResponse getData() {
		System.out.println("in get data");
		String filename = "Ruledata.json";
		ICERuleResponse obj = null;
		try {
			obj = gitFlow.getData(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(obj);
		return obj;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)	
	public ICERuleResponse setData(final ICERuleResponse resObj) {		
		System.out.println(resObj.getRule().toString());
		System.out.println(resObj.getRClass().toString());
		gitFlow.addRuleDataToRepo(resObj);
		return resObj;
	}
	
	@GET
	@Path("/versions")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public List<RuleVersions> getAllRuleVersions(){
		return gitFlow.getAllRuleVersions();
	}
	
	@GET
	@Path("/version/V/{verNo}")
	@Produces(MediaType.APPLICATION_JSON)
	public ICERuleResponse getSpecificRuleVersion(@PathParam("verNo") String verNo) {
		System.out.println("in get specific rule");
		ICERuleResponse obj = null;
		System.out.println(verNo);
		try {
			obj = gitFlow.getDataFromCommitId(verNo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(obj.toString());
		return obj;
	}
}
