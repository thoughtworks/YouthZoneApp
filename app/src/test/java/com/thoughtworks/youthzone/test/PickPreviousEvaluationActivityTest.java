package com.thoughtworks.youthzone.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.PickInProgressEvaluationActivity;
import com.thoughtworks.youthzone.PickPreviousEvaluationActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.ProjectMember;
import com.thoughtworks.youthzone.helper.SalesforceFacade;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ListView;

public class PickPreviousEvaluationActivityTest extends ActivityUnitTestCase<PickPreviousEvaluationActivity> {
	private Intent pickPreviousEvaluationIntent;

	private DatastoreFacade salesforceFacade;
	private Evaluation evaluation;
	private ProjectMember projectMember;

	public PickPreviousEvaluationActivityTest() {
		super(PickPreviousEvaluationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		salesforceFacade = mock(SalesforceFacade.class);
		projectMember = mock(ProjectMember.class);

		List<Evaluation> previousEvaluations = new ArrayList<Evaluation>();
		evaluation = mock(Evaluation.class);
		when(evaluation.toString()).thenReturn("evaluation details");
		previousEvaluations.add(evaluation);

		when(projectMember.getName()).thenReturn("name");
		when(salesforceFacade.getCompletedEvaluations(anyString(), anyString())).thenReturn(previousEvaluations);

		YouthZoneApp application = new YouthZoneApp();
		application.setDatastoreFacade(salesforceFacade);
		application.setSelectedProjectMember(projectMember);

		setApplication(application);

		pickPreviousEvaluationIntent = new Intent(getInstrumentation().getTargetContext(),
				PickInProgressEvaluationActivity.class);
	}
	
	public void testShouldPopulateListViewWithCorrectData() {
		startActivity(pickPreviousEvaluationIntent, null, null);
		// TODO: find better way to synchronise
		try {
			Thread.sleep(500);
			verify(salesforceFacade).getCompletedEvaluations(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		final ListView previousEvaluationsListView = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.previous_evaluations_listview);
		
		String listItem = (String) previousEvaluationsListView.getItemAtPosition(0);
		
		assertEquals("evaluation details", listItem);
	}

	public void testShouldLaunchPickThemeForPreviousActivityWithIntent() {
		startActivity(pickPreviousEvaluationIntent, null, null);
		// TODO: find better way to synchronise
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final ListView previousEvaluationsListView = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.previous_evaluations_listview);

		previousEvaluationsListView.performItemClick(
				previousEvaluationsListView.getAdapter().getView(0, null, null), 0,
				previousEvaluationsListView.getAdapter().getItemId(0));

		try {
			Thread.sleep(500);
			verify(salesforceFacade).getRatingsForEvaluation((Evaluation) anyObject());
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
		assertEquals("evaluation details", (String) (previousEvaluationsListView.getItemAtPosition(0)));
	} 

}
