package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PickPreviousEvaluationActivity extends Activity {
	
	private ListView previousEvaluationsListView;
	private ArrayAdapter<String> adapter;

	private List<Evaluation> previousEvaluations;
	private List<String> titlesForPreviousEvaluations = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_previous_evaluation);

		previousEvaluationsListView = (ListView) findViewById(R.id.previous_evaluations_listview);

		previousEvaluationsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				String evaluation = previousEvaluationsListView.getItemAtPosition(position).toString();
				handleListItemClick(evaluation);
			}
		});

		new RetrievePreviousEvaluations().execute();
	}

	private void handleListItemClick(String listElementText) {
		for (Evaluation selectedEvaluation : previousEvaluations) {
			if (selectedEvaluation.toString().equals(listElementText)) {
				new RetrieveRatingsAndCommentsForEvaluation().execute(selectedEvaluation);
				break;
			}
		}
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

	private class RetrievePreviousEvaluations extends AsyncTask<Void, Void, Void> {
		private DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			datastoreFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String projectName = ((YouthZoneApp) getApplication()).getSelectedProjectName();
				String memberName = ((YouthZoneApp) getApplication()).getSelectedProjectMember().getName();

				previousEvaluations = datastoreFacade.getAllEvaluations(projectName, memberName);

				for (Evaluation e : previousEvaluations) {
					titlesForPreviousEvaluations.add(e.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (titlesForPreviousEvaluations.isEmpty()) {
				showNoInProgressWarning();
			} else {
				adapter = new ArrayAdapter<String>(PickPreviousEvaluationActivity.this, R.layout.onside_list_item,
						R.id.label, titlesForPreviousEvaluations);
				previousEvaluationsListView.setAdapter(adapter);
			}
		}
	}

	private void showNoInProgressWarning() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Oops ...").setMessage("There are no previous evaluations for this member.")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						Intent intent = new Intent(PickPreviousEvaluationActivity.this,
								SelectEvaluationActivity.class);
						startActivity(intent);
						finish();
					}
				}).setIcon(android.R.drawable.ic_dialog_alert);
		AlertDialog dialog = dialogBuilder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	private class RetrieveRatingsAndCommentsForEvaluation extends AsyncTask<Evaluation, Void, Void> {
		private DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			datastoreFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(Evaluation... params) {
			try {
				
				Evaluation selectedEvaluation = params[0];
				Map<String, Object> outcomesToRatings = datastoreFacade.getRatingsForEvaluation(selectedEvaluation);
				Map<String, String> memberComments = datastoreFacade.getMemberCommentsForEvaluation(selectedEvaluation);
				
				Map<String, String> questionsToOutcomes = ((YouthZoneApp) getApplication()).getQuestionsToOutcomes();

				selectedEvaluation.initialiseDataForThemes(questionsToOutcomes, outcomesToRatings, memberComments);

				((YouthZoneApp) getApplication()).setSelectedInProgressEvaluation(selectedEvaluation);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(PickPreviousEvaluationActivity.this, PickThemeForPreviousActivity.class);
			startActivity(intent);
		}
	}
}
