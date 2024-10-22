package com.thoughtworks.youthzone;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.ThemeData;
import com.thoughtworks.youthzone.helper.ThemeListCompleteAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PickThemeForPreviousActivity extends Activity {

	private ListView themesListView;
	private ThemeListCompleteAdapter adapter;
	private TextView projectMemberTextView;
	private TextView staffCommentTextView;
	private Evaluation evaluation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_theme_for_previous);

		themesListView = (ListView) findViewById(R.id.themes_listview);
		projectMemberTextView = (TextView) findViewById(R.id.project_member_textview);
		projectMemberTextView.setText(((YouthZoneApp) getApplication()).getSelectedProjectMember().getName());
		staffCommentTextView = (TextView) findViewById(R.id.staff_comment_textview);

		evaluation = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation();
		String comment = evaluation.getComment();
		if (comment == null || comment.isEmpty()) {
			comment = "N/A";
		}
		String commentToDisplay = "Staff comment:\n" + comment;
		staffCommentTextView.setText(commentToDisplay);

		adapter = new ThemeListCompleteAdapter(this, R.layout.onside_theme_list_item, evaluation.getAllThemeData());
		themesListView.setAdapter(adapter);

		themesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				String title = ((ThemeData) themesListView.getItemAtPosition(position)).getName();
				handleListItemClick(title);
			}
		});
	}

	private void handleListItemClick(String title) {
		((YouthZoneApp) getApplication()).setSelectedThemeTitle(title);
		Intent intent = new Intent(this, ThemeReviewActivity.class);
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
