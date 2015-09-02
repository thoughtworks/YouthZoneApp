package com.thoughtworks.youthzone;

import java.util.ArrayList;
import java.util.List;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.ProjectMember;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickMemberActivity extends Activity {

	public static final String MEMBER_NAME_PREF = "memberName";

	private List<String> membersForProjectList;
	private ListView membersForProjectListview;
	private List<ProjectMember> membersForProject;
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

		new RetrieveMembers().execute(((YouthZoneApp) getApplication()).getSelectedProjectName());
	}

	private void handleListItemClick(String listElementText) {

		for (ProjectMember projectMember : membersForProject) {
			if (projectMember.toString().equals(listElementText)) {
				((YouthZoneApp) getApplication()).setSelectedProjectMember(projectMember);
				break;
			}
		}

		Intent intent = new Intent(this, SelectEvaluationActivity.class);
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
		SalesforceSDKManager.getInstance().logout(this);
	}

	private class RetrieveMembers extends AsyncTask<String, Void, Void> {
		DatastoreFacade datastoreFacade;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			datastoreFacade = ((YouthZoneApp) getApplication()).getDatastoreFacade();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				membersForProject = datastoreFacade.getMembersForProject(params[0]);
				List<String> displayAdapterText = new ArrayList<String>();
				for (ProjectMember projectMember : membersForProject) {
					displayAdapterText.add(projectMember.toString());
				}
				membersForProjectList = new ArrayList<String>(displayAdapterText);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			adapter = new ArrayAdapter<String>(PickMemberActivity.this, R.layout.onside_list_item,
					R.id.label, membersForProjectList);
			membersForProjectListview.setAdapter(adapter);
		}
	}
}
