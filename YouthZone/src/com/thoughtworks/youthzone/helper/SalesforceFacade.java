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
	private List<String> projectList;
	private List<String> membersForProjectList;
	
	public SalesforceFacade(RestClient client, String apiVersion) {
		super();
		this.apiVersion = apiVersion;
		this.client = client;
		projectList = new ArrayList<String>();
		membersForProjectList = new ArrayList<String>();
	}

	public List<String> getProjects() throws UnsupportedEncodingException {

		sendRequest("SELECT Name FROM Projects__c", projectList);
		return projectList;
	}
	
	public List<String> getMembersForProject(String project) throws UnsupportedEncodingException {

		sendRequest("SELECT Name FROM Project_Members__c", membersForProjectList);
		return membersForProjectList;
	}
	
	private void sendRequest(String soql, List<String> data) throws UnsupportedEncodingException {
		RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);

		try {
			RestResponse result = client.sendSync(restRequest);
			data.clear();
			JSONArray records = result.asJSONObject().getJSONArray("records");
			for (int i = 0; i < records.length(); i++) {
				data.add(records.getJSONObject(i).getString("Name"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
