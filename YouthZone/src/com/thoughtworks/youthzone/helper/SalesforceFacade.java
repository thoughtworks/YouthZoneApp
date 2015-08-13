package com.thoughtworks.youthzone.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class SalesforceFacade implements DatastoreFacade {

	private RestClient client;
	private String apiVersion;
	
	public SalesforceFacade(RestClient client, String apiVersion) {
		super();
		this.apiVersion = apiVersion;
		this.client = client;
	}

	public List<String> getProjects() throws Exception {

		String requiredField = "Name";
		JSONArray records = sendRequest("SELECT "+requiredField+" FROM Projects__c");
		List<String> projectList = new ArrayList<String>();
		
		for (int i = 0; i < records.length(); i++) {
			projectList.add(records.getJSONObject(i).getString(requiredField));
		}
		return projectList;
	}
	
	public List<String> getMembersForProject(String project) throws Exception {

		String requiredField = "Name";
		JSONArray records = sendRequest("SELECT Member__r.Name FROM Project_Members__c WHERE Project__r.Name = '"+project+"'");
		List<String> membersForProjectList = new ArrayList<String>();
		
		for (int i = 0; i < records.length(); i++) {
			membersForProjectList.add(records.getJSONObject(i).getJSONObject("Member__r").getString(requiredField));
		}
		
		return membersForProjectList;
	}
	
	private JSONArray sendRequest(String soql) throws UnsupportedEncodingException {
		RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);
		
		try {
			RestResponse result = client.sendSync(restRequest);
			return result.asJSONObject().getJSONArray("records");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
