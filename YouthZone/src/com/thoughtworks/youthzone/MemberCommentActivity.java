package com.thoughtworks.youthzone;

import com.salesforce.androidsdk.app.SalesforceSDKManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MemberCommentActivity extends Activity {

	private EditText editText;
	private String memberComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_comment);

		memberComment = getIntent().getStringExtra("memberComment");

		editText = (EditText) findViewById(R.id.member_comment);
		editText.setText(memberComment);
	}

	public void onSaveCommentClick(View view) {
		editText = (EditText) findViewById(R.id.member_comment);
		memberComment = editText.getText().toString();

		Intent intent = new Intent(this, QuestionActivity.class);
		intent.putExtra("questionIndex", getIntent().getIntExtra("questionIndex", 0));
		intent.putExtra("memberComment", memberComment);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.youth_zone, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			doLogout();
		}
		return super.onOptionsItemSelected(item);
	}

	private void doLogout() {
		SalesforceSDKManager.getInstance().logout(this, true);
	}
}
