package com.thoughtworks.youthzone.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;

import com.thoughtworks.youthzone.SelectEvaluationActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.SalesforceFacade;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

public class SelectEvaluationActivityTest extends ActivityUnitTestCase<SelectEvaluationActivity> {

	private Intent selectEvaluationIntent;
	private DatastoreFacade salesforceFacade;

	public SelectEvaluationActivityTest() {
		super(SelectEvaluationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		salesforceFacade = mock(SalesforceFacade.class);
		Map<String, String> questionsToOutcomes = new LinkedHashMap<String, String>();
		questionsToOutcomes.put("question1", "Confidence_Outcome_1__c");
		questionsToOutcomes.put("question2", "Confidence_Outcome_2__c");

		when(salesforceFacade.getQuestionsToOutcomes(anyString())).thenReturn(questionsToOutcomes);

		YouthZoneApp application = new YouthZoneApp();
		application.setDatastoreFacade(salesforceFacade);

		setApplication(application);

		selectEvaluationIntent = new Intent(null, SelectEvaluationActivity.class);
	}

	@MediumTest
	public void testShouldLaunchPickThemeForInProgressActivityWithIntent() {
		startActivity(selectEvaluationIntent, null, null);
		// TODO: find better way to synchronise
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		final Button startEvaluationButton = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.new_evaluation_button);
		startEvaluationButton.performClick();

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}
}
