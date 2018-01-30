package com.thoughtworks.youthzone.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.PickThemeForInProgressActivity;
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
import android.widget.Button;
import android.widget.ListView;

public class PickThemeForInProgressActivityTest extends ActivityUnitTestCase<PickThemeForInProgressActivity> {

	private Intent pickThemeForInProgressIntent;
	private YouthZoneApp application;
	private DatastoreFacade salesforceFacade;
	private ProjectMember projectMember;
	private Evaluation evaluation;

	public PickThemeForInProgressActivityTest() {
		super(PickThemeForInProgressActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		evaluation = mock(Evaluation.class);
		
		QuestionData question = new QuestionData("", "", 1.0f, "", "");
		List<QuestionData> questions = new ArrayList<QuestionData>();
		questions.add(question);
		
		ThemeData themeData1 = new ThemeData(ThemeToOutcome.CONFIDENCE.getTitle(), questions); 
		ThemeData themeData2 = new ThemeData(ThemeToOutcome.SOCIAL_SKILLS.getTitle(), questions);
		ThemeData themeData3 = new ThemeData(ThemeToOutcome.EMOTIONAL_SKILLS.getTitle(), questions);
		ThemeData themeData4 = new ThemeData(ThemeToOutcome.HEALTH.getTitle(), questions);
		ThemeData themeData5 = new ThemeData(ThemeToOutcome.ASPIRATIONS.getTitle(), questions);
		
		List<ThemeData> themes = new ArrayList<ThemeData>();
		themes.add(themeData1);
		themes.add(themeData2);
		themes.add(themeData3);
		themes.add(themeData4);
		themes.add(themeData5);
		
		when(evaluation.getAllThemeData()).thenReturn(themes);
		
		projectMember = mock(ProjectMember.class);
		salesforceFacade = mock(SalesforceFacade.class);
		
		application = new YouthZoneApp();
		application.setDatastoreFacade(salesforceFacade);
		application.setSelectedInProgressEvaluation(evaluation);
		application.setSelectedProjectMember(projectMember);
		application.setInterviewerName("name");
		setApplication(application);

		pickThemeForInProgressIntent = new Intent(getInstrumentation().getTargetContext(), PickThemeForInProgressActivity.class);
	}
	
	@SmallTest
	public void testShouldHaveListViewPopulatedWithCorrectThemes() {
		startActivity(pickThemeForInProgressIntent, null, null);
		
		final ListView themeList = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.themes_listview);
		
		String listItem0 = ((ThemeData) themeList.getItemAtPosition(0)).getName();
		String listItem1 = ((ThemeData) themeList.getItemAtPosition(1)).getName();
		String listItem2 = ((ThemeData) themeList.getItemAtPosition(2)).getName();
		String listItem3 = ((ThemeData) themeList.getItemAtPosition(3)).getName();
		String listItem4 = ((ThemeData) themeList.getItemAtPosition(4)).getName();
		
		assertEquals(ThemeToOutcome.CONFIDENCE.getTitle(), listItem0);
		assertEquals(ThemeToOutcome.SOCIAL_SKILLS.getTitle(), listItem1);
		assertEquals(ThemeToOutcome.EMOTIONAL_SKILLS.getTitle(), listItem2);
		assertEquals(ThemeToOutcome.HEALTH.getTitle(), listItem3);
		assertEquals(ThemeToOutcome.ASPIRATIONS.getTitle(), listItem4);
	}
	
	@SmallTest
	public void testShouldStoreCorrectThemeTitleWhenListItemClicked() {
		startActivity(pickThemeForInProgressIntent, null, null);
		
		final ListView themeList = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.themes_listview);
		themeList.performItemClick(themeList.getAdapter().getView(0, null, null), 0,
				themeList.getAdapter().getItemId(0));
		
		assertEquals(ThemeToOutcome.CONFIDENCE.getTitle(), application.getSelectedThemeTitle());
	}

	@SmallTest
	public void testShouldLaunchQuestionActivityWithIntent() {
		startActivity(pickThemeForInProgressIntent, null, null);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		final ListView themeList = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.themes_listview);
		themeList.performItemClick(themeList.getAdapter().getView(0, null, null), 0,
				themeList.getAdapter().getItemId(0));

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}

	@SmallTest
	public void testShouldLaunchPrivateCommentActivityWithIntent() {
		startActivity(pickThemeForInProgressIntent, null, null);
		
		final Button addComment = (Button) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.add_comment_button);
		addComment.performClick();
		
		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}
	
	@SmallTest
	public void testShouldSaveNewOutcomeWhenSaveButtonClicked() throws Exception {
		startActivity(pickThemeForInProgressIntent, null, null);
		when(evaluation.getSalesForceId()).thenReturn(null);
		when(salesforceFacade.uploadNewOutcome(projectMember, evaluation, "name")).thenReturn(true);
		
		final Button saveForLater = (Button) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.submit_in_progress_button);
		saveForLater.performClick();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		verify(salesforceFacade).uploadNewOutcome(projectMember, evaluation, "name");
	}
	
	@SmallTest
	public void testShouldSaveExistingOutcomeWhenSaveButtonClicked() throws Exception {
		startActivity(pickThemeForInProgressIntent, null, null);
		when(evaluation.getSalesForceId()).thenReturn("");
		when(evaluation.getStatus()).thenReturn("In Progress");
		when(salesforceFacade.updateExistingOutcome(evaluation, "name")).thenReturn(true);
		
		final Button saveForLater = (Button) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.submit_in_progress_button);
		saveForLater.performClick();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		verify(salesforceFacade).updateExistingOutcome(evaluation, "name");
	}
}
