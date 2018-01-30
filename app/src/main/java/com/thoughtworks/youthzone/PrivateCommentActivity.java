package com.thoughtworks.youthzone;

import com.salesforce.androidsdk.app.SalesforceSDKManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PrivateCommentActivity extends Activity {

	private TextView commentTextView;
	private String comment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_comment);

		comment = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation().getComment();

		commentTextView = (TextView) findViewById(R.id.private_comment);

		commentTextView.setText(comment);
	}

	public void onSaveCommentClick(View view) {

		comment = commentTextView.getText().toString();

		((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation().setComment(comment);

		Intent intent = new Intent(this, PickThemeForInProgressActivity.class);
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
