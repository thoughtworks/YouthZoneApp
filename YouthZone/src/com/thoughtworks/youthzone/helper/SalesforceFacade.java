package com.thoughtworks.youthzone.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.thoughtworks.youthzone.MainActivity;
import com.thoughtworks.youthzone.R;

import android.widget.ArrayAdapter;
import android.widget.Toast;

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

		client.sendAsync(restRequest, new AsyncRequestCallback() {
			@Override
			public void onSuccess(RestRequest request, RestResponse result) {
				try {
					projectList.clear();
					JSONArray records = result.asJSONObject().getJSONArray("records");
					for (int i = 0; i < records.length(); i++) {
						projectList.add(records.getJSONObject(i).getString("Name"));
					}					
				} catch (Exception e) {
					onError(e);
				}
			}

			@Override
			public void onError(Exception exception) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
	}
}
