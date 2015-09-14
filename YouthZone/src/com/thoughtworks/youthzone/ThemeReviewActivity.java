package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.QuestionData;
import com.thoughtworks.youthzone.helper.ThemeData;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ThemeReviewActivity extends Activity {

	private List<QuestionData> questionData;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme_review);

		ListView reviewListView = (ListView) findViewById(R.id.review_listview);
		List<String> questions = new ArrayList<String>();

		String themeTitle = ((YouthZoneApp) getApplication()).getSelectedThemeTitle();

		Evaluation inProgressEvaluation = ((YouthZoneApp) getApplication()).getSelectedInProgressEvaluation();

		ThemeData themeData = inProgressEvaluation.getThemeDataByTitle(themeTitle);

		questionData = themeData.getQuestions();

		for (QuestionData qd : questionData) {
			String comment = qd.getMemberComment();
			if (comment == null || comment.isEmpty()) {
				comment = "N/A";
			}

			String question = "\nStatement: " + qd.getQuestion() + "\n\nRating: " + qd.getRating() + "\n\nComment:\n\""
					+ comment + "\"\n";
			questions.add(question);
		}

		adapter = new ArrayAdapter<String>(this, R.layout.onside_questions_review_list_item, questions);
		reviewListView.setAdapter(adapter);
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
