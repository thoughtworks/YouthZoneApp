package com.thoughtworks.youthzone.test;

import com.thoughtworks.youthzone.PrivateCommentActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.Evaluation;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;

import static org.mockito.Mockito.*;

public class PrivateCommentActivityTest extends ActivityUnitTestCase<PrivateCommentActivity> {

	private Intent privateCommentIntent;

	public PrivateCommentActivityTest() {
		super(PrivateCommentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		Evaluation evaluation = mock(Evaluation.class);
		when(evaluation.getComment()).thenReturn("a comment");

		YouthZoneApp application = new YouthZoneApp();
		application.setSelectedInProgressEvaluation(evaluation);

		setApplication(application);

		privateCommentIntent = new Intent(getInstrumentation().getTargetContext(), PrivateCommentActivity.class);
	}

	public void testShouldLaunchPickOutcomeActivityWithIntent() {
		startActivity(privateCommentIntent, null, null);

		final Button done = (Button) getActivity().findViewById(com.thoughtworks.youthzone.R.id.save_comment_button);
		done.performClick();

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}

	public void testShouldDisplayCorrectCommentInTextField() {
		startActivity(privateCommentIntent, null, null);

		final EditText editText = (EditText) getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.private_comment);
		String comment = editText.getText().toString();

		assertEquals("a comment", comment);
	}
}
