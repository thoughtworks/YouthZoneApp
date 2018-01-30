package com.thoughtworks.youthzone.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.PickInProgressEvaluationActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.ProjectMember;
import com.thoughtworks.youthzone.helper.SalesforceFacade;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ListView;

public class PickInProgressEvaluationActivityTest extends ActivityUnitTestCase<PickInProgressEvaluationActivity> {
	private Intent pickInProgressEvaluationIntent;

	private DatastoreFacade salesforceFacade;
	private Evaluation evaluation;
	private ProjectMember projectMember;

	public PickInProgressEvaluationActivityTest() {
		super(PickInProgressEvaluationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		salesforceFacade = mock(SalesforceFacade.class);
		projectMember = mock(ProjectMember.class);

		List<Evaluation> inProgressEvaluations = new ArrayList<Evaluation>();
		evaluation = mock(Evaluation.class);
		when(evaluation.toString()).thenReturn("evaluation details");
		inProgressEvaluations.add(evaluation);

		when(projectMember.getName()).thenReturn("name");
		when(salesforceFacade.getInProgressEvaluations(anyString(), anyString())).thenReturn(inProgressEvaluations);

		YouthZoneApp application = new YouthZoneApp();
		application.setDatastoreFacade(salesforceFacade);
		application.setSelectedProjectMember(projectMember);

		setApplication(application);

		pickInProgressEvaluationIntent = new Intent(null, PickInProgressEvaluationActivity.class);
	}
	
	public void testShouldPopulateListViewWithCorrectData() {
		startActivity(pickInProgressEvaluationIntent, null, null);

		final ListView inProgressEvaluationsListView = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.in_progress_evaluations_listview);
		
		String listItem = (String) inProgressEvaluationsListView.getItemAtPosition(0);
		
		assertEquals("evaluation details", listItem);
	}

	public void testShouldLaunchPickThemeForInProgressActivityWithIntent() {
		startActivity(pickInProgressEvaluationIntent, null, null);

		final ListView inProgressEvaluationsListView = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.in_progress_evaluations_listview);

		inProgressEvaluationsListView.performItemClick(
				inProgressEvaluationsListView.getAdapter().getView(0, null, null), 0,
				inProgressEvaluationsListView.getAdapter().getItemId(0));

		try {
			Thread.sleep(500);
			verify(salesforceFacade).getRatingsForEvaluation((Evaluation) anyObject());
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
		assertEquals("evaluation details", (String) (inProgressEvaluationsListView.getItemAtPosition(0)));
	}
}
