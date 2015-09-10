package com.thoughtworks.youthzone.helper;

import java.util.List;

public class ThemeData {

	private String name;
	private List<QuestionData> questions;

	public ThemeData(String name, List<QuestionData> questions) {
		super();
		this.name = name;
		this.questions = questions;
	}

	public String getName() {
		return name;
	}

	public List<QuestionData> getQuestions() {
		return questions;
	}

	public boolean isComplete() {
		for (QuestionData questionData : questions) {
			if (questionData.getRating().equals(0.0f)) {
				return false;
			}
		}
		return true;
	}

}
