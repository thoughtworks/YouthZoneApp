package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Outcome;

import android.app.Activity;
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

		for (Outcome outcome : Outcome.values()) {
			if (outcome.getTitle().equals(themeTitle)) {
				outcomesForTheme = outcome.getOutcomes();
				break;
			}
		}

		questionsToOutcomes = ((YouthZoneApp) getApplication()).getQuestionsToOutcomes();
		questions = new ArrayList<String>();

		outcomeToRating =  ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation().getOutcomesToRatings();
		
		for (String question : questionsToOutcomes.keySet()) {
			if (outcomesForTheme.contains(questionsToOutcomes.get(question))) {
				questions.add(question);
			}
		}

		iterator = questions.iterator();
		if (iterator.hasNext()) {
			currentQuestion = (String) iterator.next();
			questionTextview.setText(currentQuestion);
			String currentOutcome = questionsToOutcomes.get(currentQuestion);
			ratingBar.setRating((Float) outcomeToRating.get(currentOutcome));
		}
	}

	public void onNextQuestionClick(View view) {
		outcomeToRating.put(questionsToOutcomes.get(currentQuestion), ratingBar.getRating());

		if (iterator.hasNext()) {
			currentQuestion = (String) iterator.next();
			questionTextview.setText(currentQuestion);
			String currentOutcome = questionsToOutcomes.get(currentQuestion);
			ratingBar.setRating((Float) outcomeToRating.get(currentOutcome));
		} else {
			String toDisplay = "";

			for (String key : outcomeToRating.keySet()) {
				toDisplay += key + " " + outcomeToRating.get(key) + "\n";
			}
			Log.i("***** THE WHEEL *****", toDisplay);
			Toast.makeText(this, toDisplay, Toast.LENGTH_LONG).show();

			try {
				// datastoreFacade.uploadOutcome(selectedMemberSalesforceId, projectMemberId, outcomeToRating);
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
}
