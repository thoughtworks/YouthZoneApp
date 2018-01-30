package com.thoughtworks.youthzone.test;

import static org.mockito.Mockito.mock;

import com.thoughtworks.youthzone.MemberCommentActivity;
import com.thoughtworks.youthzone.PrivateCommentActivity;
import com.thoughtworks.youthzone.YouthZoneApp;
import com.thoughtworks.youthzone.helper.Evaluation;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;

public class MemberCommentActivityTest extends ActivityUnitTestCase<MemberCommentActivity> {

	private Intent memberCommentIntent;
	private YouthZoneApp application;

	public MemberCommentActivityTest() {
		super(MemberCommentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Evaluation evaluation = mock(Evaluation.class);

		application = new YouthZoneApp();
		application.setSelectedInProgressEvaluation(evaluation);

		setApplication(application);

		memberCommentIntent = new Intent(null, PrivateCommentActivity.class);
		memberCommentIntent.putExtra("memberComment", "a comment");
	}

	public void testShouldLaunchPickOutcomeActivityWithIntent() {
		startActivity(memberCommentIntent, null, null);

		final Button done = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.save_member_comment_button);
		done.performClick();

		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
	}

	public void testShouldDisplayCorrectCommentInTextField() {
		startActivity(memberCommentIntent, null, null);

		final EditText editText = getActivity().findViewById(com.thoughtworks.youthzone.R.id.member_comment);
		String comment = editText.getText().toString();

		assertEquals("a comment", comment);
	}
	
	public void testShouldStoreCommentWhenDoneButtonClicked() {
		startActivity(memberCommentIntent, null, null);
		
		final EditText editText = getActivity().findViewById(com.thoughtworks.youthzone.R.id.member_comment);
		editText.setText("comment");
		
		final Button done = getActivity()
				.findViewById(com.thoughtworks.youthzone.R.id.save_member_comment_button);
		done.performClick();
		
		String storedComment = getStartedActivityIntent().getStringExtra("memberComment");
		assertEquals("comment", storedComment);
	}
}
