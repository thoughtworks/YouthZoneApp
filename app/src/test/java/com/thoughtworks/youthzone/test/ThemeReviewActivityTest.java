package com.thoughtworks.youthzone.test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.PickThemeForInProgressActivity;
import com.thoughtworks.youthzone.ThemeReviewActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.ProjectMember;
import com.thoughtworks.youthzone.helper.QuestionData;
import com.thoughtworks.youthzone.helper.SalesforceFacade;
import com.thoughtworks.youthzone.helper.ThemeData;
import com.thoughtworks.youthzone.helper.ThemeToOutcome;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ListView;
import android.widget.TextView;

public class ThemeReviewActivityTest extends ActivityUnitTestCase<ThemeReviewActivity> {

	private Intent themeReviewIntent;
	private YouthZoneApp application;
	private DatastoreFacade salesforceFacade;
	private ProjectMember projectMember;
	private Evaluation evaluation;
	
	private String questionText = "a question";
	private Float rating = 1.0f;
	private String comment = "a comment";

	public ThemeReviewActivityTest() {
		super(ThemeReviewActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		evaluation = mock(Evaluation.class);
		
		QuestionData question = new QuestionData("", questionText, rating, comment, "");
		List<QuestionData> questions = new ArrayList<>();
		questions.add(question);
		
		ThemeData themeData = new ThemeData(ThemeToOutcome.CONFIDENCE.getTitle(), questions); 
		
		when(evaluation.getThemeDataByTitle(anyString())).thenReturn(themeData);
		
		projectMember = mock(ProjectMember.class);
		salesforceFacade = mock(SalesforceFacade.class);
		
		application = new YouthZoneApp();
		application.setDatastoreFacade(salesforceFacade);
		application.setSelectedInProgressEvaluation(evaluation);
		application.setSelectedProjectMember(projectMember);
		application.setInterviewerName("name");
		setApplication(application);

		themeReviewIntent = new Intent(null, PickThemeForInProgressActivity.class);
	}
	
	@SmallTest
	public void testShouldHaveListViewPopulatedWithCorrectData() {
		startActivity(themeReviewIntent, null, null);
		
		final ListView themeList = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.review_listview);
		final TextView questionData = (TextView) themeList.getAdapter().getView(0, null, null);
		
		String expectedOutput = "\nStatement: " + questionText + "\n\nRating: 1\n\nComment:\n"
				+ comment + "\n";
		
		assertEquals(expectedOutput, questionData.getText());
	}

}
