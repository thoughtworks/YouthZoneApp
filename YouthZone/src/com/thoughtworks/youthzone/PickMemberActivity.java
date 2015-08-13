package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.helper.DatastoreFacade;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickMemberActivity extends Activity {
	
	private List<String> membersForProjectList;
	private ListView membersForProjectListview;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_member);
		
		membersForProjectListview = (ListView) findViewById(R.id.members_listview);

		String selectedProject = getIntent().getStringExtra("selectedProject");
		
		membersForProjectList = new ArrayList<String>();
		
	    new RetrieveMembers().execute(selectedProject);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_member, menu);
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
	
	
	private class RetrieveMembers extends AsyncTask<String, Void, Void> {
		DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			datastoreFacade = YouthZoneApp.getInstance().getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				membersForProjectList = datastoreFacade.getMembersForProject(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			adapter = new ArrayAdapter<String>(PickMemberActivity.this,
		            android.R.layout.simple_list_item_1, membersForProjectList);
			membersForProjectListview.setAdapter(adapter);
		}
		
	}
}
