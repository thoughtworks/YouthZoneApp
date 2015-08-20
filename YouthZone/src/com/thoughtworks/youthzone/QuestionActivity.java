package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Outcome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
	
	DatastoreFacade datastoreFacade;
	
	private RatingBar ratingBar;
	private Map<String, String> questionsToOutcomes;
	private List<String> questions;
	private TextView questionTextview;
	private Iterator<String> iterator;
	private String currentQuestion;
	private String selectedProject;
	private String selectedMemberSalesforceId;
	private String projectMemberId;
	
	private Map<String, Object> outcomeToRating;
	private List<String> outcomesForTheme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		questionTextview = (TextView) findViewById(R.id.question_textview);
		ratingBar = (RatingBar) findViewById(R.id.question_ratingbar);
		
		String themeTitle = getIntent().getStringExtra("title");
		outcomesForTheme = new ArrayList<String>();
		
		for(Outcome outcome : Outcome.values()){
			if(outcome.getTitle().equals(themeTitle)){
				outcomesForTheme = outcome.getOutcomes();
				break;
			}
		}
		
		
		questionsToOutcomes = new LinkedHashMap<String, String>();
		questions = new ArrayList<String>();
		
		SharedPreferences prefs = getSharedPreferences(PickProjectActivity.PROJECT_NAME_PREF, MODE_PRIVATE);
		selectedProject = prefs.getString("selectedProject", "");
		prefs = getSharedPreferences(PickMemberActivity.MEMBER_NAME_PREF, MODE_PRIVATE);
		selectedMemberSalesforceId = prefs.getString("selectedMemberSalesforceId", "");
		projectMemberId = prefs.getString("projectMemberId", "");
		
		
		outcomeToRating = new LinkedHashMap<String, Object>();
		
		new RetrieveIndicators().execute(selectedProject);

	}
	
	public void onNextQuestionClick(View view) {
		outcomeToRating.put(questionsToOutcomes.get(currentQuestion), ratingBar.getRating());
		ratingBar.setRating(0.0F);
		
		if (iterator.hasNext()) {
			currentQuestion = (String) iterator.next();
			questionTextview.setText(currentQuestion);
		} else {
			String toDisplay = "";
			
			for(String key : outcomeToRating.keySet()){
				toDisplay += key + " " + outcomeToRating.get(key) + "\n"; 
			}
			Log.i("***** THE WHEEL *****", toDisplay);
			Toast.makeText(this, toDisplay, Toast.LENGTH_LONG).show();
			
			try {
				//datastoreFacade.uploadOutcome(selectedMemberSalesforceId, projectMemberId, outcomeToRating);
			} catch (Exception e) {
				e.printStackTrace();           
			}
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

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			datastoreFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				questionsToOutcomes = datastoreFacade.getQuestionsToOutcomes(params[0]);
				for(String question : questionsToOutcomes.keySet()){
					if(outcomesForTheme.contains(questionsToOutcomes.get(question))){
						questions.add(question);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			iterator = questions.iterator();
			if (iterator.hasNext()) {
				currentQuestion = (String) iterator.next();
				questionTextview.setText(currentQuestion);
			}			
		}
	}
}
