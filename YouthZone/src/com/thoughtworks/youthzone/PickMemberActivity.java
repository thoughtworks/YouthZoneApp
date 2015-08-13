package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.youthzone.helper.DatastoreFacade;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PickMemberActivity extends Activity {
	
	public static final String MEMBER_NAME_PREF = "memberName";
	
	private List<String> membersForProjectList;
	private ListView membersForProjectListview;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_member);
		
		membersForProjectListview = (ListView) findViewById(R.id.members_listview);
		
		membersForProjectListview.setOnItemClickListener(new OnItemClickListener() {
		    @Override 
		    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		    	String memberName = membersForProjectListview.getItemAtPosition(position).toString();
		    	handleListItemClick(memberName);
		    }
		});
		

		String selectedProject = getIntent().getStringExtra("selectedProject");
		
		membersForProjectList = new ArrayList<String>();
		
	    new RetrieveMembers().execute(selectedProject);
	}
	
	private void handleListItemClick(String memberName) {
		SharedPreferences selectedMember = getSharedPreferences(MEMBER_NAME_PREF, MODE_PRIVATE);
		SharedPreferences.Editor editor = selectedMember.edit();
		editor.putString("selectedMember", memberName);
		editor.commit();
		
		Intent intent = new Intent(this, SelectEvaluationActivity.class);
		intent.putExtra("selectedMember", memberName);
		startActivity(intent);
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
