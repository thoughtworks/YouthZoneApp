package com.thoughtworks.youthzone.test;

import com.thoughtworks.youthzone.MainActivity;
import com.thoughtworks.youthzone.YouthZoneApp;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

	private Intent mainIntent;
	private YouthZoneApp application;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		application = new YouthZoneApp();
		setApplication(application);

		mainIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
	}

	@SmallTest
	public void testShouldLaunchPickProjectActivityWithIntent() {
		// TO-DO: find better way to synchronise
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startActivity(mainIntent, null, null);

		final Button pickProjectButton = (Button) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.get_started);
		pickProjectButton.performClick();

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}
	
	@SmallTest
	public void testShouldStoreStaffNameWhenGetStartedButtonClicked() {
		startActivity(mainIntent, null, null);
		
		final EditText editText = (EditText) getActivity().findViewById(com.thoughtworks.youthzone.R.id.staff_name_edit);
		editText.setText("name");

		final Button pickProjectButton = (Button) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.get_started);
		pickProjectButton.performClick();

		assertEquals("name", application.getInterviewerName());
	}
}
