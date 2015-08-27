package com.thoughtworks.youthzone;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MemberCommentActivity extends Activity {

	private EditText editText;
	private Map<String, String> memberComments;
	private String currentOutcome;
	private String memberComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_comment);
		
		currentOutcome = getIntent().getStringExtra("currentOutcome");
		memberComments = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation().getMemberComments();
		
		memberComment = memberComments.get(currentOutcome.replace("Outcome", "Comments"));
		
		editText = (EditText) findViewById(R.id.member_comment);
		editText.setText(memberComment);
	}
	
	public void onSaveCommentClick(View view) {
		editText = (EditText) findViewById(R.id.member_comment);
		memberComment = editText.getText().toString();
		
		memberComments = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation().getMemberComments();
		currentOutcome = getIntent().getStringExtra("currentOutcome");
		
		memberComments.put(currentOutcome.replace("Outcome", "Comments"), memberComment);
		
		Intent intent = new Intent(this, QuestionActivity.class);
		intent.putExtra("questionIndex", getIntent().getIntExtra("questionIndex", 0));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
