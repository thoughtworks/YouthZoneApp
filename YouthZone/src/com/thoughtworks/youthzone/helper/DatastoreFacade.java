package com.thoughtworks.youthzone.helper;

import java.util.List;
import java.util.Map;

public interface DatastoreFacade {
    List<String> getProjects() throws Exception; 
    List<String> getMembersForProject(String project) throws Exception;
    Map<String, String> getIndicatorsForProject(String project) throws Exception;
}
