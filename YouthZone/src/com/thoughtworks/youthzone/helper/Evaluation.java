package com.thoughtworks.youthzone.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Evaluation {

	private String date;
	private String name;
	private String salesForceId;
	//private Map<String, Object> outcomesToRatings = new LinkedHashMap<String, Object>();
	private String status = "In Progress";
	private String comment = "";
    //private Map<String, String> memberComments = new LinkedHashMap<String, String>();
	private List<ThemeData> themeData;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalesForceId() {
		return salesForceId;
	}

	public void setSalesForceId(String salesForceId) {
		this.salesForceId = salesForceId;
	}

	public Map<String, Object> getOutcomesToRatings() {
		Map<String, Object> outcomesToRatings = new LinkedHashMap<String, Object>();
		for(ThemeData theme : themeData){
			for(QuestionData question : theme.getQuestions()){
				outcomesToRatings.put(question.getOutcome(), question.getRating());
			}
		}
		
		return outcomesToRatings;
	}
//
//	public void setOutcomesToRatings(Map<String, Object> outcomesToRatings) {
//		this.outcomesToRatings = outcomesToRatings;
//	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<String, String> getMemberComments() {
		Map<String, String> memberComments = new LinkedHashMap<String, String>();
		for(ThemeData theme : themeData){
			for(QuestionData question : theme.getQuestions()){
				memberComments.put(question.getMemberCommentField(), question.getMemberComment());
			}
		}
		
		return memberComments;
	}

//	public void setMemberComments(Map<String, String> memberComments) {
//		this.memberComments = memberComments;
//	}
	
	public List<ThemeData> getThemeData() {
		return themeData;
	}

	public void setThemeData(List<ThemeData> themeData) {
		this.themeData = themeData;
	}
	
	public ThemeData getThemeDataByTitle(String title){
		for(ThemeData themeData : themeData){
			if (themeData.getName().equals(title)) {
				return themeData;
			}
		}
		return null;
	}
	

	@Override
	public String toString() {
		return date + " | " + name;
	}
	
}