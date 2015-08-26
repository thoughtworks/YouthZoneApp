package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.Outcome;
import com.thoughtworks.youthzone.helper.ProjectMember;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PickOutcomeActivity extends Activity {

	private ListView themesListView;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_outcome);

		themesListView = (ListView) findViewById(R.id.themes_listview);

		List<String> themeTitles = new ArrayList<String>();

		for (Outcome outcome : Outcome.values()) {
			themeTitles.add(outcome.getTitle());
		}

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, themeTitles);
		themesListView.setAdapter(adapter);

		themesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				String title = themesListView.getItemAtPosition(position).toString();
				handleListItemClick(title);
			}
		});

	}

	private void handleListItemClick(String title) {
		Intent intent = new Intent(this, QuestionActivity.class);
		intent.putExtra("title", title);
		startActivity(intent);
	}

	public void onSubmitEvaluationClick(View view) {
		String buttonText = ((Button) view).getText().toString();
		new UploadOutcome().execute(buttonText);
	}
	
	public void onAddCommentClick(View view) {
		Intent intent = new Intent(this, PrivateCommentActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_outcome, menu);
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

	private class UploadOutcome extends AsyncTask<String, Void, Void> {
		Evaluation evaluation;
		ProjectMember projectMember;
		DatastoreFacade salesforceFacade;
		boolean uploadSuccess;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			evaluation = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation();
			projectMember = ((YouthZoneApp) getApplication()).getSelectedProjectMember();
			salesforceFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {

			if (params[0].equals(getResources().getString(R.string.complete_evaluation_button))) {
				evaluation.setStatus("Complete");
			}

			try {
				if (evaluation.getSalesForceId() == null) {
					uploadSuccess = salesforceFacade.uploadNewOutcome(projectMember, evaluation);
				} else {
					uploadSuccess = salesforceFacade.updateExistingOutcome(evaluation);
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
				Toast.makeText(PickOutcomeActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(PickOutcomeActivity.this, SelectEvaluationActivity.class);
				startActivity(intent);
			} else {
				new AlertDialog.Builder(PickOutcomeActivity.this).setTitle("Upload Failed")
				.setMessage("Please try again")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
			}

		}
	}

}
