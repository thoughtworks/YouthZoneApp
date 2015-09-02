package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickInProgressEvaluationActivity extends Activity {

	private ListView inProgressEvaluationsListView;
	private ArrayAdapter<String> adapter;

	private List<Evaluation> inProgressEvaluations;
	private List<String> titlesForInProgressEvaluations = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_in_progress_evaluation);

		inProgressEvaluationsListView = (ListView) findViewById(R.id.in_progress_evaluations_listview);

		inProgressEvaluationsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				String evaluation = inProgressEvaluationsListView.getItemAtPosition(position).toString();
				handleListItemClick(evaluation);
			}
		});

		new RetrieveInProgressEvaluations().execute();
	}

	private void handleListItemClick(String listElementText) {
		for (Evaluation selectedEvaluation : inProgressEvaluations) {
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
		SalesforceSDKManager.getInstance().logout(this);
	}

	private class RetrieveInProgressEvaluations extends AsyncTask<Void, Void, Void> {
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

				inProgressEvaluations = datastoreFacade.getInProgressEvaluations(projectName, memberName);
				Log.d("*** InProg", "" + inProgressEvaluations.size());
				for (Evaluation e : inProgressEvaluations) {
					titlesForInProgressEvaluations.add(e.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			adapter = new ArrayAdapter<String>(PickInProgressEvaluationActivity.this,
					R.layout.onside_list_item, R.id.label, titlesForInProgressEvaluations);
			inProgressEvaluationsListView.setAdapter(adapter);
		}
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
				Map<String, Object> outcomesToRatings = datastoreFacade
						.getRatingsForInProgressEvaluation(selectedEvaluation);
				selectedEvaluation.setOutcomesToRatings(outcomesToRatings);

				Map<String, String> memberComments = datastoreFacade
						.getMemberCommentsForInProgressEvaluation(selectedEvaluation);
				selectedEvaluation.setMemberComments(memberComments);

				((YouthZoneApp) getApplication()).setSelectedInProgressEvaluation(selectedEvaluation);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(PickInProgressEvaluationActivity.this, PickOutcomeActivity.class);
			startActivity(intent);
		}
	}
}
