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
	
	public SalesforceFacade(RestClient client, String apiVersion) {
		super();
		this.apiVersion = apiVersion;
		this.client = client;
		projectList = new ArrayList<String>();
	}

	public List<String> getProjects() throws UnsupportedEncodingException {

		sendRequest("SELECT Name FROM Projects__c");
		return projectList;
	}
	
	private void sendRequest(String soql) throws UnsupportedEncodingException {
		RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);

		try {
			RestResponse result = client.sendSync(restRequest);
			projectList.clear();
			JSONArray records = result.asJSONObject().getJSONArray("records");
			for (int i = 0; i < records.length(); i++) {
				projectList.add(records.getJSONObject(i).getString("Name"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
