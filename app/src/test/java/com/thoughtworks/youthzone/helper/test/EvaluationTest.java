package com.thoughtworks.youthzone.helper.test;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.QuestionData;
import com.thoughtworks.youthzone.helper.ThemeData;

public class EvaluationTest {
	
	private Evaluation evaluation;
	private Map<String, String> questionsToOutcomes;
	private Map<String, Object> outcomesToRatings;
	private Map<String, String> memberComments;

	@Before
	public void setup() {
		evaluation = new Evaluation();
		questionsToOutcomes = new LinkedHashMap<>();
		outcomesToRatings = new LinkedHashMap<>();
		memberComments = new LinkedHashMap<>();
		
		questionsToOutcomes.put("a question", "Confidence_Outcome_1__c");
		outcomesToRatings.put("Confidence_Outcome_1__c", 3.0f);
		memberComments.put("Confidence_Comments_1__c", "a comment");
	}
	
	@Test
	public void shouldGenerateThemeDataFromMap() {
		evaluation.initialiseDataForThemes(questionsToOutcomes);
		
		List<ThemeData> themeData = evaluation.getAllThemeData();
		
		ThemeData firstThemeData = themeData.get(0);
		QuestionData firstQuestion = firstThemeData.getQuestions().get(0);
		
		assertEquals(1, themeData.size());
		assertEquals("Confidence", firstThemeData.getName());
		assertEquals("a question", firstQuestion.getQuestion());
		assertEquals("", firstQuestion.getMemberComment());
		assertEquals("Confidence_Comments_1__c", firstQuestion.getMemberCommentField());
		assertEquals("Confidence_Outcome_1__c", firstQuestion.getOutcome());
		assertEquals((Float)0.0f, firstQuestion.getRating());
	}
	
	@Test
	public void shouldGenerateThemeDataFromMaps() {
		evaluation.initialiseDataForThemes(questionsToOutcomes, outcomesToRatings, memberComments);
		
		List<ThemeData> themeData = evaluation.getAllThemeData();
		
		ThemeData firstThemeData = themeData.get(0);
		QuestionData firstQuestion = firstThemeData.getQuestions().get(0);
		
		assertEquals(1, themeData.size());
		assertEquals("Confidence", firstThemeData.getName());
		assertEquals("a question", firstQuestion.getQuestion());
		assertEquals("a comment", firstQuestion.getMemberComment());
		assertEquals("Confidence_Comments_1__c", firstQuestion.getMemberCommentField());
		assertEquals("Confidence_Outcome_1__c", firstQuestion.getOutcome());
		assertEquals((Float)3.0f, firstQuestion.getRating());
	}
	
	@Test
	public void shouldReturnCorrectThemeDataByName() {
		evaluation.initialiseDataForThemes(questionsToOutcomes);
		
		ThemeData themeData = evaluation.getThemeDataByTitle("Confidence");
		
		assertEquals("Confidence", themeData.getName());
	}
	
	@Test
	public void shouldReturnNullWhenThemeDataTitleNotPresent() {
		evaluation.initialiseDataForThemes(questionsToOutcomes);
		
		ThemeData themeData = evaluation.getThemeDataByTitle("Emotional Skills");
		
		assertNull(themeData);
	}
	
	@Test
	public void shouldReturnCorrectOutcomesToRatings() {
		evaluation.initialiseDataForThemes(questionsToOutcomes);
		
		Map<String, Object> outcomesToRatings = evaluation.getOutcomesToRatings();
		String outcome = (String)outcomesToRatings.keySet().toArray()[0];
		Float rating = (Float)outcomesToRatings.values().toArray()[0];
		
		assertEquals("Confidence_Outcome_1__c", outcome);		
		assertEquals((Float)0.0f, rating);
	}
	
	@Test
	public void shouldReturnCorrectMemberComments() {
		evaluation.initialiseDataForThemes(questionsToOutcomes);
		
		Map<String, String> memberComments = evaluation.getMemberComments();
		String commentField = (String) memberComments.keySet().toArray()[0];
		String commentString = (String) memberComments.values().toArray()[0];
		
		assertEquals("Confidence_Comments_1__c", commentField);		
		assertEquals("", commentString);
	}
}
