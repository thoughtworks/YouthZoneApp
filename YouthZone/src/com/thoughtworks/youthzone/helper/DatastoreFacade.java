package com.thoughtworks.youthzone.helper;

import java.util.List;
import java.util.Map;

public interface DatastoreFacade {
	List<String> getProjects() throws Exception;

	List<ProjectMember> getMembersForProject(String project) throws Exception;

	Map<String, String> getQuestionsToOutcomes(String project) throws Exception;

	boolean uploadNewOutcome(ProjectMember projectMember, Evaluation evaluation, String interviewerName)
			throws Exception;

	boolean updateExistingOutcome(Evaluation evaluation, String interviewerName) throws Exception;
	
	List<Evaluation> getInProgressEvaluations(String projectName, String memberName) throws Exception;
	
	Map<String, Object> getRatingsForInProgressEvaluation(Evaluation evaluation) throws Exception;
}
