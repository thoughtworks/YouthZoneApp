package com.thoughtworks.youthzone.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;

import android.util.Log;

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
	
	public List<ProjectMember> getMembersForProject(String project) throws Exception {
		JSONArray records = sendRequest("SELECT Id, Member__r.Name, Member__r.Birthdate__c, Member__r.Member_Id__c, Member__r.Id  FROM Project_Members__c WHERE Project__r.Name = '"+project+"'");
		List<ProjectMember> projectMembers = new ArrayList<ProjectMember>();
		
		for (int i = 0; i < records.length(); i++) {
			
			ProjectMember projectMember = new ProjectMember(
					records.getJSONObject(i).getString("Id"),
					records.getJSONObject(i).getJSONObject("Member__r").getString("Name"),
					records.getJSONObject(i).getJSONObject("Member__r").getString("Birthdate__c"),
					records.getJSONObject(i).getJSONObject("Member__r").getString("Member_Id__c"),
					records.getJSONObject(i).getJSONObject("Member__r").getString("Id")
			);
			projectMembers.add(projectMember);
		}
		
		return projectMembers;
	}
	
	public Map<String, String> getIndicatorsForProject(String project) throws Exception {
		Map<String, String> questionsToOutcomes = new LinkedHashMap<String, String>();
		
		JSONArray records = sendRequest("SELECT Aspirations_Indicator_1__c,Aspirations_Indicator_2__c,Aspirations_Indicator_3__c,Citizenship_Indicator_1__c,Citizenship_Indicator_2__c,Citizenship_Indicator_3__c,Cohesion_Indicator_1__c,Cohesion_Indicator_2__c,Cohesion_Indicator_3__c,Communication_Skills_Indicator_1__c,Communication_Skills_Indicator_2__c,Communication_Skills_Indicator_3__c,Confidence_Indicator_1__c,Confidence_Indicator_2__c,Confidence_Indicator_3__c,Determination_Indicator_1__c,Determination_Indicator_2__c,Determination_Indicator_3__c,Empathy_Indicator_1__c,Empathy_Indicator_2__c,Empathy_Indicator_3__c,Leadership_Skills_Indicator_1__c,Leadership_Skills_Indicator_2__c,Leadership_Skills_Indicator_3__c,Life_Skills_Indicator_1__c,Life_Skills_Indicator_2__c,Life_Skills_Indicator_3__c,Managing_Feelings_Indicator_1__c,Managing_Feelings_Indicator_2__c,Managing_Feelings_Indicator_3__c,Mental_Wellbeing_Indicator_1__c,Mental_Wellbeing_Indicator_2__c,Mental_Wellbeing_Indicator_3__c,Physical_Health_Indicator_1__c,Physical_Health_Indicator_2__c,Physical_Health_Indicator_3__c,Positive_Health_Choices_Indicator_1__c,Positive_Health_Choices_Indicator_2__c,Positive_Health_Choices_Indicator_3__c,Problem_Solving_Indicator_1__c,Problem_Solving_Indicator_2__c,Problem_Solving_Indicator_3__c,Ready_for_Work_LLL_Indicator_1__c,Ready_for_Work_LLL_Indicator_2__c,Ready_for_Work_LLL_Indicator_3__c,Resilience_Indicator_1__c,Resilience_Indicator_2__c,Resilience_Indicator_3__c,Self_Awareness_Indicator_1__c,Self_Awareness_Indicator_2__c,Self_Awareness_Indicator_3__c,Self_Efficiency_Indicator_1__c,Self_Efficiency_Indicator_2__c,Self_Efficiency_Indicator_3__c,Self_Esteem_Indicator_1__c,Self_Esteem_Indicator_2__c,Self_Esteem_Indicator_3__c,Social_Skills_Indicator_1__c,Social_Skills_Indicator_2__c,Social_Skills_Indicator_3__c FROM Projects__c WHERE Name = '"+project+"'");
	    
	    for (int i = 0; i < records.length(); i++) {
	    	JSONObject jsonObject = records.getJSONObject(i);
	    	Iterator<?> keys = jsonObject.keys();
	    	while (keys.hasNext()) {
	    		String key = (String) keys.next();
	    		if (jsonObject.get(key) instanceof String) {
	    			String value = jsonObject.get(key).toString();
	    			questionsToOutcomes.put(value, key.replace("_Indicator_", "_Outcome_"));
	    		}
	    	}
		}
	    
		return questionsToOutcomes;
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
	

	@Override
	public void uploadOutcome(String memberSalesforceId, String projectMemberNumber, Map<String, Object> outcomeToRating) throws Exception {
		outcomeToRating.put("Member_Name__c", memberSalesforceId);
		outcomeToRating.put("Project_Member__c", projectMemberNumber);
		RestRequest restRequest = RestRequest.getRequestForCreate(apiVersion, "Outcome__c", outcomeToRating);
		client.sendAsync(restRequest, new AsyncRequestCallback() {

            @Override
            public void onError(Exception e) {
            	e.printStackTrace();
                Log.d("*** uploadOutcome", "Error Occurred! ");
            }

            @Override
            public void onSuccess(RestRequest request, RestResponse response) {
            	Log.d("*** uploadOutcome", "Success!");
            }
        });
		
	}
	
}
