package com.thoughtworks.youthzone.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import android.util.Log;

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
	
	public List<String> getIndicatorsForProject(String project) throws Exception {
		JSONArray records = sendRequest("SELECT Aspirations_Indicator_1__c,Aspirations_Indicator_2__c,Aspirations_Indicator_3__c,Citizenship_Indicator_1__c,Citizenship_Indicator_2__c,Citizenship_Indicator_3__c,Cohesion_Indicator_1__c,Cohesion_Indicator_2__c,Cohesion_Indicator_3__c,Communication_Skills_Indicator_1__c,Communication_Skills_Indicator_2__c,Communication_Skills_Indicator_3__c,Confidence_Indicator_1__c,Confidence_Indicator_2__c,Confidence_Indicator_3__c,Determination_Indicator_1__c,Determination_Indicator_2__c,Determination_Indicator_3__c,Empathy_Indicator_1__c,Empathy_Indicator_2__c,Empathy_Indicator_3__c,Leadership_Skills_Indicator_1__c,Leadership_Skills_Indicator_2__c,Leadership_Skills_Indicator_3__c,Life_Skills_Indicator_1__c,Life_Skills_Indicator_2__c,Life_Skills_Indicator_3__c,Managing_Feelings_Indicator_1__c,Managing_Feelings_Indicator_2__c,Managing_Feelings_Indicator_3__c,Mental_Wellbeing_Indicator_1__c,Mental_Wellbeing_Indicator_2__c,Mental_Wellbeing_Indicator_3__c,Physical_Health_Indicator_1__c,Physical_Health_Indicator_2__c,Physical_Health_Indicator_3__c,Positive_Health_Choices_Indicator_1__c,Positive_Health_Choices_Indicator_2__c,Positive_Health_Choices_Indicator_3__c,Problem_Solving_Indicator_1__c,Problem_Solving_Indicator_2__c,Problem_Solving_Indicator_3__c,Ready_for_Work_LLL_Indicator_1__c,Ready_for_Work_LLL_Indicator_2__c,Ready_for_Work_LLL_Indicator_3__c,Resilience_Indicator_1__c,Resilience_Indicator_2__c,Resilience_Indicator_3__c,Self_Awareness_Indicator_1__c,Self_Awareness_Indicator_2__c,Self_Awareness_Indicator_3__c,Self_Efficiency_Indicator_1__c,Self_Efficiency_Indicator_2__c,Self_Efficiency_Indicator_3__c,Self_Esteem_Indicator_1__c,Self_Esteem_Indicator_2__c,Self_Esteem_Indicator_3__c,Social_Skills_Indicator_1__c,Social_Skills_Indicator_2__c,Social_Skills_Indicator_3__c FROM Projects__c WHERE Name = '"+project+"'");
	    List<String> indicatorsForProjectList = new ArrayList<String>();
	    
	    for (int i = 0; i < records.length(); i++) {
	    	JSONObject jsonObject = records.getJSONObject(i);
	    	Iterator<?> keys = jsonObject.keys();
	    	while (keys.hasNext()) {
	    		String key = (String) keys.next();
	    		if (jsonObject.get(key) instanceof String) {
	    			String value = jsonObject.get(key).toString();
	    			indicatorsForProjectList.add(value);
	    		}
	    	}
		}
	    
		return indicatorsForProjectList;
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
