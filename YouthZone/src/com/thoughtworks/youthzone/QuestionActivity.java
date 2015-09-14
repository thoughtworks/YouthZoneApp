package com.thoughtworks.youthzone;

import java.util.List;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.QuestionData;
import com.thoughtworks.youthzone.helper.ThemeData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class QuestionActivity extends Activity {

	DatastoreFacade datastoreFacade;

	private RatingBar ratingBar;
	private List<QuestionData> questionData;
	private TextView questionTextview;

	private int questionIndex = -1;

	private QuestionData currentQuestionData;
	
	private final String[] descriptions = {"","I’m not thinking about this at the moment",
			                                  "I’m interested but don’t know what to do",
				                              "I’ve started to do something about this",
				                              "I’m working well on this",
				                              "This is part of my everyday life"};  


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		questionTextview = (TextView) findViewById(R.id.question_textview);
		ratingBar = (RatingBar) findViewById(R.id.question_ratingbar);

		String themeTitle = ((YouthZoneApp) getApplication()).getSelectedThemeTitle();
		
		Evaluation inProgressEvaluation = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation();
		
		ThemeData themeData = inProgressEvaluation.getThemeDataByTitle(themeTitle);
		
		questionData = themeData.getQuestions();

		questionIndex = getIntent().getIntExtra("questionIndex", 0);
		
		currentQuestionData = questionData.get(questionIndex);
		String memberComment = getIntent().getStringExtra("memberComment");
		if(memberComment == null){
			memberComment = currentQuestionData.getMemberComment();
		} else {			
			currentQuestionData.setMemberComment(memberComment);
		}
		
		setupNextQuestion();

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				int position = (int) rating;
				setDescription(position);
				currentQuestionData.setRating(ratingBar.getRating());
			}
		});
	}

	private void setupNextQuestion() {

		if (questionIndex < questionData.size()) {
			currentQuestionData = questionData.get(questionIndex);
			questionTextview.setText(currentQuestionData.getQuestion());
			setRatingBar();
		} else {
			Intent intent = new Intent(this, PickThemeForInProgressActivity.class);
			startActivity(intent);
		}
	}

	private void setupPreviousQuestion() {

		if (questionIndex >= 0) {
			currentQuestionData = questionData.get(questionIndex);
			questionTextview.setText(currentQuestionData.getQuestion());
			setRatingBar();
		} else {
			Intent intent = new Intent(this, PickThemeForInProgressActivity.class);
			startActivity(intent);
		}
	}

	private void setRatingBar() {
		Float rating = currentQuestionData.getRating();
		if (rating == null) {
			rating = 0.0f;
		}
		ratingBar.setRating(rating);
		int position = rating.intValue();
		setDescription(position);
	}
	
	private void setDescription(int position) {
		TextView description = (TextView) findViewById(R.id.description_textview);
		description.setText(descriptions[position]);
	}

	public void onNextQuestionClick(View view) {
		questionIndex++;
		setupNextQuestion();
	}

	public void onPreviousQuestionClick(View view) {
		questionIndex--;
		setupPreviousQuestion();
	}

	public void onAddMemberCommentClick(View view) {
		Intent intent = new Intent(this, MemberCommentActivity.class);
		intent.putExtra("questionIndex", questionIndex);
		intent.putExtra("memberComment", currentQuestionData.getMemberComment());
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
