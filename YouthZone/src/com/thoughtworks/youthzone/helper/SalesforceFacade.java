package com.thoughtworks.youthzone.helper;

import java.util.Arrays;
import java.util.List;

import com.salesforce.androidsdk.rest.RestClient;

public class SalesforceFacade implements DatastoreFacade {

	private RestClient client;
	
	public SalesforceFacade(RestClient client) {
		super();
		this.client = client;
	}

	public List<String> getProjects() {

		String[] projects = new String[] { "Sport England", "Challenge to Change" };
		List<String> projectsList = Arrays.asList(projects);
		return projectsList;
	}
}
