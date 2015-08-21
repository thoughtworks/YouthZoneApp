package com.thoughtworks.youthzone.helper;

import java.util.List;
import java.util.Map;

public interface DatastoreFacade {
	List<String> getProjects() throws Exception;

	List<ProjectMember> getMembersForProject(String project) throws Exception;

	Map<String, String> getQuestionsToOutcomes(String project) throws Exception;

	void uploadNewOutcome(String memberId, String projectMemberNumber, Map<String, Object> uploadData)
			throws Exception;

	void updateExistingOutcome(String outcomeSalesforceId, Map<String, Object> uploadData) throws Exception;
	
	List<Evaluation> getInProgressEvaluations(String projectName, String memberName) throws Exception;
	
	Map<String, Object> getRatingsForInProgressEvaluation(String salesforceId) throws Exception;
}
