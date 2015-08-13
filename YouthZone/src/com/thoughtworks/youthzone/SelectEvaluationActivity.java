package com.thoughtworks.youthzone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SelectEvaluationActivity extends Activity {
	
	private String selectedMember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_evaluation);
		
		SharedPreferences prefs = getSharedPreferences(PickMemberActivity.MEMBER_NAME_PREF, MODE_PRIVATE);
		selectedMember = prefs.getString("selectedMember", "");
		
		Toast.makeText(this, "Member is: " + selectedMember, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_evaluation, menu);
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
	
	public void onNewEvaluationClick(View view) {
		Intent intent = new Intent(this, PickOutcomeActivity.class);
		startActivity(intent);
	}
}
