package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.youthzone.helper.DatastoreFacade;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class QuestionActivity extends Activity {
	
	private List<String> indicatorsForProjectList;
	private TextView questionTextview;
	private Iterator<String> iterator;
	private String currentQuestion;
	private String selectedProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		questionTextview = (TextView) findViewById(R.id.question_textview);
		
		indicatorsForProjectList = new ArrayList<String>();
		
		SharedPreferences prefs = getSharedPreferences(PickProjectActivity.PROJET_NAME_PREF, MODE_PRIVATE);
		selectedProject = prefs.getString("selectedProject", "");
		
		new RetrieveIndicators().execute(selectedProject);

	}
	
	public void onNextQuestionClick(View view) {
		if (iterator.hasNext()) {
			currentQuestion = (String) iterator.next();
			questionTextview.setText(currentQuestion);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
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
	
	private class RetrieveIndicators extends AsyncTask<String, Void, Void> {
		DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			datastoreFacade = YouthZoneApp.getInstance().getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				indicatorsForProjectList = datastoreFacade.getIndicatorsForProject(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			iterator = indicatorsForProjectList.iterator();
			if (iterator.hasNext()) {
				currentQuestion = (String) iterator.next();
				questionTextview.setText(currentQuestion);
			}			
		}
	}
}
