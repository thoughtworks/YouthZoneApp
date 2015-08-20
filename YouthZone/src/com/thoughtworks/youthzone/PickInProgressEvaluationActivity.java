package com.thoughtworks.youthzone;

import java.util.List;

import com.thoughtworks.youthzone.helper.DatastoreFacade;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickInProgressEvaluationActivity extends Activity {
	
	private ListView inProgressEvaluationsListView;
	private ArrayAdapter<String> adapter;
	private List<String> inProgressEvaluations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_in_progress_evaluation);
		
		inProgressEvaluationsListView = (ListView) findViewById(R.id.in_progress_evaluations_listview);
		
		new RetrieveInProgressEvaluations().execute("");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_in_progress_evaluation, menu);
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
	
	private class RetrieveInProgressEvaluations extends AsyncTask<String, Void, Void> {
		private DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			datastoreFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String projectName = ((YouthZoneApp) getApplication()).getSelectedProjectName();
				String memberName = ((YouthZoneApp) getApplication()).getSelectedProjectMember().getName();
				inProgressEvaluations = datastoreFacade.getInProgressEvaluations(projectName, memberName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			adapter = new ArrayAdapter<String>(PickInProgressEvaluationActivity.this, android.R.layout.simple_list_item_1,
					inProgressEvaluations);
			inProgressEvaluationsListView.setAdapter(adapter);
		}
	}
}
