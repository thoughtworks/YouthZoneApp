package com.thoughtworks.youthzone.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ThemeToOutcome {
	CONFIDENCE("Confidence",
			new ArrayList<String>(Arrays.asList("Confidence_Outcome_1__c", "Confidence_Outcome_2__c",
					"Confidence_Outcome_3__c", "Self_Efficiency_Outcome_1__c", "Self_Efficiency_Outcome_2__c",
					"Self_Efficiency_Outcome_3__c", "Self_Esteem_Outcome_1__c", "Self_Esteem_Outcome_2__c",
					"Self_Esteem_Outcome_3__c"))),

	SOCIAL_SKILLS("Social Skills/ Relationships",
			new ArrayList<String>(Arrays.asList("Social_Skills_Outcome_1__c", "Social_Skills_Outcome_2__c",
					"Social_Skills_Outcome_3__c", "Communication_Skills_Outcome_1__c",
					"Communication_Skills_Outcome_2__c", "Communication_Skills_Outcome_3__c", "Cohesion_Outcome_1__c",
					"Cohesion_Outcome_2__c", "Cohesion_Outcome_3__c", "Leadership_Skills_Outcome_1__c",
					"Leadership_Skills_Outcome_2__c", "Leadership_Skills_Outcome_3__c", "Citizenship_Outcome_1__c",
					"Citizenship_Outcome_2__c", "Citizenship_Outcome_3__c"))),

	EMOTIONAL_SKILLS("Emotional Skills",
			new ArrayList<String>(Arrays.asList("Self_Awareness_Outcome_1__c", "Self_Awareness_Outcome_2__c",
					"Self_Awareness_Outcome_3__c", "Managing_Feelings_Outcome_1__c", "Managing_Feelings_Outcome_2__c",
					"Managing_Feelings_Outcome_3__c", "Empathy_Outcome_1__c", "Empathy_Outcome_2__c",
					"Empathy_Outcome_3__c", "Resilience_Outcome_1__c", "Resilience_Outcome_2__c",
					"Resilience_Outcome_3__c", "Problem_Solving_Outcome_1__c", "Problem_Solving_Outcome_2__c",
					"Problem_Solving_Outcome_3__c"))),

	HEALTH("Health",
			new ArrayList<String>(Arrays.asList("Physical_Health_Outcome_1__c", "Physical_Health_Outcome_2__c",
					"Physical_Health_Outcome_3__c", "Mental_Wellbeing_Outcome_1__c", "Mental_Wellbeing_Outcome_2__c",
					"Mental_Wellbeing_Outcome_3__c", "Positive_Health_Choices_Outcome_1__c",
					"Positive_Health_Choices_Outcome_2__c", "Positive_Health_Choices_Outcome_3__c"))),

	ASPIRATIONS("Aspirations and Achievement",
			new ArrayList<String>(Arrays.asList("Aspirations_Outcome_1__c", "Aspirations_Outcome_2__c",
					"Aspirations_Outcome_3__c", "Determination_Outcome_1__c", "Determination_Outcome_2__c",
					"Determination_Outcome_3__c", "Life_Skills_Outcome_1__c", "Life_Skills_Outcome_2__c",
					"Life_Skills_Outcome_3__c", "Ready_for_Work_LLL_Outcome_1__c", "Ready_for_Work_LLL_Outcome_2__c",
					"Ready_for_Work_LLL_Outcome_3__c")));

	private final String title;
	private final List<String> outcomes;

	private ThemeToOutcome(String title, List<String> outcomes) {
		this.title = title;
		this.outcomes = outcomes;
	}

	public String getTitle() {

		return title;
	}

	public List<String> getOutcomes() {
		return new ArrayList<String>(outcomes);
	}
}
