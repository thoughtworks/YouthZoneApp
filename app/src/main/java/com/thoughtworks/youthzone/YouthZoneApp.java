/*
 * Copyright (c) 2011, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.thoughtworks.youthzone;

import java.util.List;
import java.util.Map;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.thoughtworks.youthzone.helper.DatastoreFacade;
import com.thoughtworks.youthzone.helper.Evaluation;
import com.thoughtworks.youthzone.helper.ProjectMember;
import com.thoughtworks.youthzone.helper.ThemeData;

import android.app.Application;

/**
 * Application class for our application.
 */
public class YouthZoneApp extends Application {

	private DatastoreFacade datastoreFacade;

	private Map<String, String> questionsToOutcomes;
	private String selectedProjectName;

	private ProjectMember selectedProjectMember;
	private Evaluation selectedInProgressEvaluation;
	private String selectedThemeTitle;

	private String interviewerName;

	private List<ThemeData> themeData;

	@Override
	public void onCreate() {
		super.onCreate();
		SalesforceSDKManager.initNative(getApplicationContext(), new KeyImpl(), MainActivity.class);

		selectedInProgressEvaluation = new Evaluation();

		/*
		 * Un-comment the line below to enable push notifications in this app.
		 * Replace 'pnInterface' with your implementation of
		 * 'PushNotificationInterface'. Add your Google package ID in
		 * 'bootonfig.xml', as the value for the key
		 * 'androidPushNotificationClientId'.
		 */
		// SalesforceSDKManager.getInstance().setPushNotificationReceiver(pnInterface);
	}

	public DatastoreFacade getDatastoreFacade() {
		return datastoreFacade;
	}

	public void setDatastoreFacade(DatastoreFacade datastoreFacade) {
		this.datastoreFacade = datastoreFacade;
	}

	public Map<String, String> getQuestionsToOutcomes() {
		return questionsToOutcomes;
	}

	public void setQuestionsToOutcomes(Map<String, String> questionsToOutcomes) {
		this.questionsToOutcomes = questionsToOutcomes;
	}

	public String getSelectedProjectName() {
		return selectedProjectName;
	}

	public void setSelectedProjectName(String selectedProjectName) {
		this.selectedProjectName = selectedProjectName;
	}

	public ProjectMember getSelectedProjectMember() {
		return selectedProjectMember;
	}

	public void setSelectedProjectMember(ProjectMember selectedProjectMember) {
		this.selectedProjectMember = selectedProjectMember;
	}

	public void setSelectedInProgressEvaluation(Evaluation selectedInProgressEvaluation) {
		this.selectedInProgressEvaluation = selectedInProgressEvaluation;
	}

	public String getSelectedThemeTitle() {
		return selectedThemeTitle;
	}

	public void setSelectedThemeTitle(String selectedThemeTitle) {
		this.selectedThemeTitle = selectedThemeTitle;
	}

	public Evaluation getSelectedInProgressEvaluation() {
		return selectedInProgressEvaluation;
	}

	public String getInterviewerName() {
		return interviewerName;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public List<ThemeData> getThemes() {
		return themeData;
	}

	public void setThemes(List<ThemeData> themeDatas) {
		this.themeData = themeDatas;
	}

}
