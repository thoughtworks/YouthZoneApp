package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.helper.Outcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickOutcomeActivity extends Activity {
	
	private ListView themesListView;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_outcome);
		
		themesListView = (ListView) findViewById(R.id.themes_listview); 
		
		List<String> themeTitles = new ArrayList<String>();
		
		for(Outcome outcome : Outcome.values()){
			themeTitles.add(outcome.getTitle());
		}
		
		adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, themeTitles);
		themesListView.setAdapter(adapter);
		
	}
	
	public void onStartQuestionsClick(View view) {
		Intent intent = new Intent(this, QuestionActivity.class);
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
}
