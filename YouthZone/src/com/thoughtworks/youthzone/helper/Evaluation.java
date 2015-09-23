package com.thoughtworks.youthzone.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Evaluation {

	private String date;
	private String name;
	private String salesForceId;
	private String status = "In Progress";
	private String comment = "";
	private List<ThemeData> themeData = new ArrayList<ThemeData>();

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
		for (ThemeData theme : themeData) {
			for (QuestionData question : theme.getQuestions()) {
				outcomesToRatings.put(question.getOutcome(), question.getRating());
			}
		}

		return outcomesToRatings;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<String, String> getMemberComments() {
		Map<String, String> memberComments = new LinkedHashMap<String, String>();
		for (ThemeData theme : themeData) {
			for (QuestionData question : theme.getQuestions()) {
				memberComments.put(question.getMemberCommentField(), question.getMemberComment());
			}
		}

		return memberComments;
	}

	public List<ThemeData> getAllThemeData() {
		return themeData;
	}

	public void initialiseDataForThemes(Map<String, String> questionsToOutcomes) {
		generateThemeList(questionsToOutcomes, null, null);
	}

	public void initialiseDataForThemes(Map<String, String> questionsToOutcomes, Map<String, Object> outcomesToRatings,
			Map<String, String> memberComments) {
		generateThemeList(questionsToOutcomes, outcomesToRatings, memberComments);
	}

	private void generateThemeList(Map<String, String> questionsToOutcomes, Map<String, Object> outcomesToRatings,
			Map<String, String> memberComments) {

		boolean newEvaluationStarted = true;
		if (outcomesToRatings != null && memberComments != null) {
			newEvaluationStarted = false;
		}

		for (ThemeToOutcome theme : ThemeToOutcome.values()) {
			List<QuestionData> questions = new ArrayList<QuestionData>();
			for (String question : questionsToOutcomes.keySet()) {
				if (theme.getOutcomes().contains(questionsToOutcomes.get(question))) {
					String outcomeField = questionsToOutcomes.get(question);
					String commentField = outcomeField.replace("Outcome", "Comments");
					if (newEvaluationStarted) {
						questions.add(new QuestionData(outcomeField, question, 0.0f, "", commentField));
					} else {
						Float rating = (Float) outcomesToRatings.get(outcomeField);
						if (rating == null) {
							rating = 0.0f;
						}
						questions.add(new QuestionData(outcomeField, question, rating, memberComments.get(commentField),
								commentField));
					}
				}
			}
			if (!questions.isEmpty()) {
				themeData.add(new ThemeData(theme.getTitle(), questions));
			}
		}
	}

	public ThemeData getThemeDataByTitle(String title) {
		for (ThemeData themeData : themeData) {
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