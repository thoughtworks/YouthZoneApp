package com.thoughtworks.youthzone.helper;

import java.util.List;
import java.util.Map;

public interface DatastoreFacade {
    List<String> getProjects() throws Exception; 
    List<ProjectMember> getMembersForProject(String project) throws Exception;
    Map<String, String> getQuestionsToOutcomes(String project) throws Exception;
    void uploadOutcome(String memberId, String projectMemberNumber, Map<String, Object> outcomeToRating) throws Exception;
}
