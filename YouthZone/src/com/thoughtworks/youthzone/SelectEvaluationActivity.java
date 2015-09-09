package com.thoughtworks.youthzone;

import java.util.Map;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SelectEvaluationActivity extends Activity {

	private Map<String, String> questionsToOutcomes;

	private Button newEvaluation;
	private Button inProgressEvaluations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_evaluation);

		newEvaluation = (Button) findViewById(R.id.new_evaluation_button);
		newEvaluation.setEnabled(false);

		inProgressEvaluations = (Button) findViewById(R.id.in_progress_evaluations);
		inProgressEvaluations.setEnabled(false);

		new RetrieveQuestionsToOutcomes().execute(((YouthZoneApp) getApplication()).getSelectedProjectName());
	}

	public void onNewEvaluationClick(View view) {

		Evaluation evaluation = new Evaluation();
		evaluation.initialiseDataForThemes(questionsToOutcomes);
		((YouthZoneApp) getApplication()).setSelectedInProgressEvaluation(evaluation);

		Intent intent = new Intent(this, PickOutcomeActivity.class);
		startActivity(intent);
	}

	public void onContinueEvaluationClick(View view) {
		Intent intent = new Intent(this, PickInProgressEvaluationActivity.class);
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
		SalesforceSDKManager.getInstance().logout(this);
	}

	private class RetrieveQuestionsToOutcomes extends AsyncTask<String, Void, Void> {
		private DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			datastoreFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				questionsToOutcomes = datastoreFacade.getQuestionsToOutcomes(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			((YouthZoneApp) getApplication()).setQuestionsToOutcomes(questionsToOutcomes);

			newEvaluation.setEnabled(true);
			inProgressEvaluations.setEnabled(true);

		}
	}
}
