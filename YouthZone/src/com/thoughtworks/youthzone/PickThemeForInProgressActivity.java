package com.thoughtworks.youthzone;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.ProjectMember;
import com.thoughtworks.youthzone.helper.ThemeData;
import com.thoughtworks.youthzone.helper.ThemeListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PickThemeForInProgressActivity extends Activity {

	private ListView themesListView;
	private ThemeListAdapter adapter;
	private TextView projectMemberTextView;
	private Evaluation evaluation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_theme_for_in_progress);

		themesListView = (ListView) findViewById(R.id.themes_listview);
		projectMemberTextView = (TextView) findViewById(R.id.project_member_textview);
		projectMemberTextView.setText(((YouthZoneApp) getApplication()).getSelectedProjectMember().getName());

		evaluation = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation();

		adapter = new ThemeListAdapter(this, R.layout.onside_list_item, evaluation.getThemeData());
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
		Intent intent = new Intent(this, QuestionActivity.class);
		startActivity(intent);
	}

	public void onSubmitEvaluationClick(View view) {
		final String buttonText = ((Button) view).getText().toString();
		if (buttonText.equals(getString(R.string.complete_evaluation_button))) {
			new AlertDialog.Builder(this).setTitle("Submit complete evaluation")
					.setMessage("Are you sure you want to mark this evaluation as complete?")
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new UploadOutcome().execute(buttonText);
						}
					}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();
		} else {
			new UploadOutcome().execute(buttonText);
		}
	}

	public void onAddCommentClick(View view) {
		Intent intent = new Intent(this, PrivateCommentActivity.class);
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

	private class UploadOutcome extends AsyncTask<String, Void, Void> {
		ProjectMember projectMember;
		DatastoreFacade salesforceFacade;
		boolean uploadSuccess;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			projectMember = ((YouthZoneApp) getApplication()).getSelectedProjectMember();
			salesforceFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {

			String interviewerName = ((YouthZoneApp) getApplication()).getInterviewerName();

			if (params[0].equals(getResources().getString(R.string.complete_evaluation_button))) {
				evaluation.setStatus("Complete");
			}

			try {
				if (evaluation.getSalesForceId() == null) {
					uploadSuccess = salesforceFacade.uploadNewOutcome(projectMember, evaluation, interviewerName);
				} else {
					uploadSuccess = salesforceFacade.updateExistingOutcome(evaluation, interviewerName);
				}
			} catch (Exception e) {
				uploadSuccess = false;
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (uploadSuccess) {
				Toast.makeText(PickThemeForInProgressActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(PickThemeForInProgressActivity.this, SelectEvaluationActivity.class);
				startActivity(intent);
			} else {
				new AlertDialog.Builder(PickThemeForInProgressActivity.this).setTitle("Upload Failed")
						.setMessage("Please try again")
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
			}

		}
	}

}
