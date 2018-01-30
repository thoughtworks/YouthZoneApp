package com.thoughtworks.youthzone.test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.QuestionActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.QuestionData;
import com.thoughtworks.youthzone.helper.SalesforceFacade;
import com.thoughtworks.youthzone.helper.ThemeData;
import com.thoughtworks.youthzone.helper.ThemeToOutcome;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class QuestionActivityTest extends ActivityUnitTestCase<QuestionActivity> {

	private Intent questionIntent;
	private DatastoreFacade salesforceFacade;

	public QuestionActivityTest() {
		super(QuestionActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		salesforceFacade = mock(SalesforceFacade.class);

		YouthZoneApp application = new YouthZoneApp();
		Evaluation evaluation = mock(Evaluation.class);
		
		QuestionData question1 = new QuestionData("Confidence_Outcome_1__c", "question1", 1.0f, "", "");
		QuestionData question2 = new QuestionData("Confidence_Outcome_2__c", "question2", 2.0f, "", "");
		List<QuestionData> questions = new ArrayList<QuestionData>();
		questions.add(question1);
		questions.add(question2);
		
		ThemeData themeData = new ThemeData(ThemeToOutcome.CONFIDENCE.getTitle(), questions); 
		
		when(evaluation.getThemeDataByTitle(anyString())).thenReturn(themeData);
		
		application.setSelectedInProgressEvaluation(evaluation);
		application.setDatastoreFacade(salesforceFacade);
		application.setSelectedThemeTitle(ThemeToOutcome.CONFIDENCE.getTitle());

		setApplication(application);

		questionIntent = new Intent(null, QuestionActivity.class);
	}

	@MediumTest
	public void testShouldUpdateCurrentQuestionWhenNextButtonClicked() {
		startActivity(questionIntent, null, null);

		TextView currentQuestion = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.question_textview);

		assertEquals("question1", currentQuestion.getText());

		final Button nextQuestionButton = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.next_question_button);
		nextQuestionButton.performClick();

		currentQuestion = getActivity().findViewById(com.thoughtworks.youthzone.R.id.question_textview);
		assertEquals("question2", currentQuestion.getText());
	}

	@MediumTest
	public void testShouldUpdateCurrentQuestionWhenBackButtonClicked() {
		startActivity(questionIntent, null, null);

		TextView currentQuestion = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.question_textview);

		assertEquals("question1", currentQuestion.getText());

		final Button nextQuestionButton = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.next_question_button);
		nextQuestionButton.performClick();

		final Button previousQuestionButton = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.previous_question_button);
		previousQuestionButton.performClick();

		currentQuestion = (TextView) getActivity().findViewById(com.thoughtworks.youthzone.R.id.question_textview);
		assertEquals("question1", currentQuestion.getText());
	}

	@MediumTest
	public void testShouldSetRatingBarWhenNextButtonClicked() {
		startActivity(questionIntent, null, null);

		RatingBar ratingBar = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.question_ratingbar);
		assertEquals(1.0f, ratingBar.getRating());

		final Button nextQuestionButton = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.next_question_button);
		nextQuestionButton.performClick();

		ratingBar = getActivity().findViewById(com.thoughtworks.youthzone.R.id.question_ratingbar);
		assertEquals(2.0f, ratingBar.getRating());
	}

	@MediumTest
	public void testShouldLaunchActivityWhenBackButtonClicked() {
		startActivity(questionIntent, null, null);

		final Button previousQuestionButton = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.previous_question_button);
		previousQuestionButton.performClick();

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}
}
