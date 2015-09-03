package com.thoughtworks.youthzone.helper;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
		JSONArray records = sendRequest("SELECT " + requiredField + " FROM Projects__c");
		List<String> projectList = new ArrayList<String>();

		for (int i = 0; i < records.length(); i++) {
			projectList.add(records.getJSONObject(i).getString(requiredField));
		}
		return projectList;
	}

	public List<ProjectMember> getMembersForProject(String projectName) throws Exception {
		JSONArray records = sendRequest(
				"SELECT Id, Member__r.Name, Member__r.Birthdate__c, Member__r.Member_Id__c, Member__r.Id  FROM Project_Members__c WHERE Project__r.Name = '"
						+ projectName + "'");
		List<ProjectMember> projectMembers = new ArrayList<ProjectMember>();

		for (int i = 0; i < records.length(); i++) {

			String birthDate = records.getJSONObject(i).getJSONObject("Member__r").getString("Birthdate__c");
			String formattedBirthDate = formatDate(birthDate);

			ProjectMember projectMember = new ProjectMember(records.getJSONObject(i).getString("Id"),
					records.getJSONObject(i).getJSONObject("Member__r").getString("Name"), formattedBirthDate,
					records.getJSONObject(i).getJSONObject("Member__r").getString("Member_Id__c"),
					records.getJSONObject(i).getJSONObject("Member__r").getString("Id"));
			projectMembers.add(projectMember);
		}

		return projectMembers;
	}

	private String formatDate(String birthDate) throws ParseException {
		String formattedDate = "N/A";
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(birthDate);
			formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return formattedDate;
	}

	public Map<String, String> getQuestionsToOutcomes(String project) throws Exception {
		Map<String, String> questionsToOutcomes = new LinkedHashMap<String, String>();

		JSONArray records = sendRequest(
				"SELECT Aspirations_Indicator_1__c,Aspirations_Indicator_2__c,Aspirations_Indicator_3__c,Citizenship_Indicator_1__c,Citizenship_Indicator_2__c,Citizenship_Indicator_3__c,Cohesion_Indicator_1__c,Cohesion_Indicator_2__c,Cohesion_Indicator_3__c,Communication_Skills_Indicator_1__c,Communication_Skills_Indicator_2__c,Communication_Skills_Indicator_3__c,Confidence_Indicator_1__c,Confidence_Indicator_2__c,Confidence_Indicator_3__c,Determination_Indicator_1__c,Determination_Indicator_2__c,Determination_Indicator_3__c,Empathy_Indicator_1__c,Empathy_Indicator_2__c,Empathy_Indicator_3__c,Leadership_Skills_Indicator_1__c,Leadership_Skills_Indicator_2__c,Leadership_Skills_Indicator_3__c,Life_Skills_Indicator_1__c,Life_Skills_Indicator_2__c,Life_Skills_Indicator_3__c,Managing_Feelings_Indicator_1__c,Managing_Feelings_Indicator_2__c,Managing_Feelings_Indicator_3__c,Mental_Wellbeing_Indicator_1__c,Mental_Wellbeing_Indicator_2__c,Mental_Wellbeing_Indicator_3__c,Physical_Health_Indicator_1__c,Physical_Health_Indicator_2__c,Physical_Health_Indicator_3__c,Positive_Health_Choices_Indicator_1__c,Positive_Health_Choices_Indicator_2__c,Positive_Health_Choices_Indicator_3__c,Problem_Solving_Indicator_1__c,Problem_Solving_Indicator_2__c,Problem_Solving_Indicator_3__c,Ready_for_Work_LLL_Indicator_1__c,Ready_for_Work_LLL_Indicator_2__c,Ready_for_Work_LLL_Indicator_3__c,Resilience_Indicator_1__c,Resilience_Indicator_2__c,Resilience_Indicator_3__c,Self_Awareness_Indicator_1__c,Self_Awareness_Indicator_2__c,Self_Awareness_Indicator_3__c,Self_Efficiency_Indicator_1__c,Self_Efficiency_Indicator_2__c,Self_Efficiency_Indicator_3__c,Self_Esteem_Indicator_1__c,Self_Esteem_Indicator_2__c,Self_Esteem_Indicator_3__c,Social_Skills_Indicator_1__c,Social_Skills_Indicator_2__c,Social_Skills_Indicator_3__c FROM Projects__c WHERE Name = '"
						+ project + "'");

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
	public boolean uploadNewOutcome(ProjectMember projectMember, Evaluation evaluation, String interviewerName)
			throws Exception {
		
		Log.d("***** evaluation", evaluation.toString());
		for(Object rating : evaluation.getOutcomesToRatings().values()){
			Log.d("***** rating", ((Float)rating).toString());
		}

		for(String comment : evaluation.getMemberComments().values()){
			Log.d("***** comment", comment);
		}
		
		Log.d("***** evaluation", evaluation.getComment());
		Log.d("***** evaluation", evaluation.getStatus());
		Log.d("***** interviewer", interviewerName);

		Map<String, Object> uploadData = new LinkedHashMap<String, Object>();
		uploadData.putAll(evaluation.getOutcomesToRatings());
		uploadData.putAll(evaluation.getMemberComments());
		uploadData.put("Member_Name__c", projectMember.getSalesForceId());
		uploadData.put("Project_Member__c", projectMember.getProjectMemberId());
		uploadData.put("Status__c", evaluation.getStatus());
		uploadData.put("Youth_Zone_Comments__c", evaluation.getComment());
		uploadData.put("Staff_Name__c", interviewerName);
		RestRequest restRequest = RestRequest.getRequestForCreate(apiVersion, "Outcome__c", uploadData);

		RestResponse result = client.sendSync(restRequest);
		return result.isSuccess();
	}

	@Override
	public boolean updateExistingOutcome(Evaluation evaluation, String interviewerName) throws Exception {
		
		Log.d("***** evaluation", evaluation.getSalesForceId());
		Log.d("***** evaluation", evaluation.toString());
		for(Object rating : evaluation.getOutcomesToRatings().values()){
			Log.d("***** rating", ((Float)rating).toString());
		}

		for(String comment : evaluation.getMemberComments().values()){
			Log.d("***** comment", comment);
		}
		
		Log.d("***** evaluation", evaluation.getComment());
		Log.d("***** evaluation", evaluation.getStatus());
		Log.d("***** interviewer", interviewerName);
		
		Map<String, Object> uploadData = new LinkedHashMap<String, Object>();
		uploadData.putAll(evaluation.getOutcomesToRatings());
		uploadData.putAll(evaluation.getMemberComments());
		uploadData.put("Status__c", evaluation.getStatus());
		uploadData.put("Youth_Zone_Comments__c", evaluation.getComment());
		uploadData.put("Staff_Name__c", interviewerName);
		
		RestRequest restRequest = RestRequest.getRequestForUpdate(apiVersion, "Outcome__c",
				evaluation.getSalesForceId(), uploadData);

		RestResponse result = client.sendSync(restRequest);
		return result.isSuccess();
	}

	@Override
	public List<Evaluation> getInProgressEvaluations(String projectName, String memberName) throws Exception {
		JSONArray records = sendRequest(
				"SELECT Date__c, Name, Youth_Zone_Comments__c, Id FROM Outcome__c WHERE Status__c = 'In Progress' AND Project1__c = '"
						+ projectName + "' AND Member_Name__r.Name = '" + memberName + "'");
		List<Evaluation> inProgressEvaluations = new ArrayList<Evaluation>();

		for (int i = 0; i < records.length(); i++) {
			Evaluation evaluation = new Evaluation();
			String date = records.getJSONObject(i).getString("Date__c");
			evaluation.setDate(formatDate(date));
			evaluation.setName(records.getJSONObject(i).getString("Name"));
			String comment = records.getJSONObject(i).getString("Youth_Zone_Comments__c");
			if (comment.equals("null")) {
				comment = "";
			}
			evaluation.setComment(comment);
			evaluation.setSalesForceId(records.getJSONObject(i).getString("Id"));
			inProgressEvaluations.add(evaluation);
		}

		return inProgressEvaluations;
	}

	@Override
	public Map<String, Object> getRatingsForInProgressEvaluation(Evaluation evaluation) throws Exception {
		JSONArray record = sendRequest(
				"SELECT Aspirations_Outcome_1__c,Aspirations_Outcome_2__c,Aspirations_Outcome_3__c,Citizenship_Outcome_1__c,Citizenship_Outcome_2__c,Citizenship_Outcome_3__c,Cohesion_Outcome_1__c,Cohesion_Outcome_2__c,Cohesion_Outcome_3__c,Communication_Skills_Outcome_1__c,Communication_Skills_Outcome_2__c,Communication_Skills_Outcome_3__c,Confidence_Outcome_1__c,Confidence_Outcome_2__c,Confidence_Outcome_3__c,Determination_Outcome_1__c,Determination_Outcome_2__c,Determination_Outcome_3__c,Empathy_Outcome_1__c,Empathy_Outcome_2__c,Empathy_Outcome_3__c,Leadership_Skills_Outcome_1__c,Leadership_Skills_Outcome_2__c,Leadership_Skills_Outcome_3__c,Life_Skills_Outcome_1__c,Life_Skills_Outcome_2__c,Life_Skills_Outcome_3__c,Managing_Feelings_Outcome_1__c,Managing_Feelings_Outcome_2__c,Managing_Feelings_Outcome_3__c,Mental_Wellbeing_Outcome_1__c,Mental_Wellbeing_Outcome_2__c,Mental_Wellbeing_Outcome_3__c,Physical_Health_Outcome_1__c,Physical_Health_Outcome_2__c,Physical_Health_Outcome_3__c,Positive_Health_Choices_Outcome_1__c,Positive_Health_Choices_Outcome_2__c,Positive_Health_Choices_Outcome_3__c,Problem_Solving_Outcome_1__c,Problem_Solving_Outcome_2__c,Problem_Solving_Outcome_3__c,Ready_for_Work_LLL_Outcome_1__c,Ready_for_Work_LLL_Outcome_2__c,Ready_for_Work_LLL_Outcome_3__c,Resilience_Outcome_1__c,Resilience_Outcome_2__c,Resilience_Outcome_3__c,Self_Awareness_Outcome_1__c,Self_Awareness_Outcome_2__c,Self_Awareness_Outcome_3__c,Self_Efficiency_Outcome_1__c,Self_Efficiency_Outcome_2__c,Self_Efficiency_Outcome_3__c,Self_Esteem_Outcome_1__c,Self_Esteem_Outcome_2__c,Self_Esteem_Outcome_3__c,Social_Skills_Outcome_1__c,Social_Skills_Outcome_2__c,Social_Skills_Outcome_3__c FROM Outcome__c WHERE Id = '"
						+ evaluation.getSalesForceId() + "'");

		Map<String, Object> outcomesToRatings = new LinkedHashMap<String, Object>();

		JSONObject jsonObject = record.getJSONObject(0);
		Iterator<?> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (jsonObject.get(key) != null && jsonObject.get(key) instanceof String) {
				Float value = Float.valueOf((String) jsonObject.get(key));
				outcomesToRatings.put(key, value);
			}
		}

		return outcomesToRatings;
	}

	@Override
	public Map<String, String> getMemberCommentsForInProgressEvaluation(Evaluation evaluation) throws Exception {
		JSONArray record = sendRequest(
				"SELECT Aspirations_Comments_1__c,Aspirations_Comments_2__c,Aspirations_Comments_3__c,Citizenship_Comments_1__c,Citizenship_Comments_2__c,Citizenship_Comments_3__c,Cohesion_Comments_1__c,Cohesion_Comments_2__c,Cohesion_Comments_3__c,Communication_Skills_Comments_1__c,Communication_Skills_Comments_2__c,Communication_Skills_Comments_3__c,Confidence_Comments_1__c,Confidence_Comments_2__c,Confidence_Comments_3__c,Determination_Comments_1__c,Determination_Comments_2__c,Determination_Comments_3__c,Empathy_Comments_1__c,Empathy_Comments_2__c,Empathy_Comments_3__c,Leadership_Skills_Comments_1__c,Leadership_Skills_Comments_2__c,Leadership_Skills_Comments_3__c,Life_Skills_Comments_1__c,Life_Skills_Comments_2__c,Life_Skills_Comments_3__c,Managing_Feelings_Comments_1__c,Managing_Feelings_Comments_2__c,Managing_Feelings_Comments_3__c,Mental_Wellbeing_Comments_1__c,Mental_Wellbeing_Comments_2__c,Mental_Wellbeing_Comments_3__c,Physical_Health_Comments_1__c,Physical_Health_Comments_2__c,Physical_Health_Comments_3__c,Positive_Health_Choices_Comments_1__c,Positive_Health_Choices_Comments_2__c,Positive_Health_Choices_Comments_3__c,Problem_Solving_Comments_1__c,Problem_Solving_Comments_2__c,Problem_Solving_Comments_3__c,Ready_for_Work_LLL_Comments_1__c,Ready_for_Work_LLL_Comments_2__c,Ready_for_Work_LLL_Comments_3__c,Resilience_Comments_1__c,Resilience_Comments_2__c,Resilience_Comments_3__c,Self_Awareness_Comments_1__c,Self_Awareness_Comments_2__c,Self_Awareness_Comments_3__c,Self_Efficiency_Comments_1__c,Self_Efficiency_Comments_2__c,Self_Efficiency_Comments_3__c,Self_Esteem_Comments_1__c,Self_Esteem_Comments_2__c,Self_Esteem_Comments_3__c,Social_Skills_Comments_1__c,Social_Skills_Comments_2__c,Social_Skills_Comments_3__c FROM Outcome__c WHERE Id = '"
						+ evaluation.getSalesForceId() + "'");

		Map<String, String> memberComments = new LinkedHashMap<String, String>();

		JSONObject jsonObject = record.getJSONObject(0);
		Iterator<?> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (jsonObject.get(key) != null && jsonObject.get(key) instanceof String) {
				String value = (String) jsonObject.get(key);
				memberComments.put(key, value);
			}
		}

		return memberComments;
	}

}
