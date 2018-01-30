package com.thoughtworks.youthzone.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.PickMemberActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.ProjectMember;
import com.thoughtworks.youthzone.helper.SalesforceFacade;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ListView;

public class PickMemberActivityTest extends ActivityUnitTestCase<PickMemberActivity> {

	private Intent pickMemberIntent;

	private DatastoreFacade salesforceFacade;
	private ProjectMember projectMember;

	public PickMemberActivityTest() {
		super(PickMemberActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		salesforceFacade = mock(SalesforceFacade.class);
		projectMember = mock(ProjectMember.class);

		List<ProjectMember> members = new ArrayList<ProjectMember>();
		members.add(projectMember);

		when(projectMember.toString()).thenReturn("member details");
		when(salesforceFacade.getMembersForProject(anyString())).thenReturn(members);

		YouthZoneApp application = new YouthZoneApp();
		application.setDatastoreFacade(salesforceFacade);

		setApplication(application);

		pickMemberIntent = new Intent(getInstrumentation().getTargetContext(), PickMemberActivity.class);
	}
	
	public void testShouldPopulateMemberListWithCorrectData() {
		startActivity(pickMemberIntent, null, null);
		// TODO: find better way to synchronise
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final ListView memberList = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.members_listview);
		
		String listItem = (String) memberList.getItemAtPosition(0);
		
		assertEquals("member details", listItem);
	}

	public void testShouldLaunchSelectEvaluationActivityWithIntent() {
		startActivity(pickMemberIntent, null, null);
		// TODO: find better way to synchronise
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final ListView memberList = (ListView) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.members_listview);

		memberList.performItemClick(memberList.getAdapter().getView(0, null, null), 0,
				memberList.getAdapter().getItemId(0));

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
		assertEquals("member details", (String) (memberList.getItemAtPosition(0)));
	}

}
