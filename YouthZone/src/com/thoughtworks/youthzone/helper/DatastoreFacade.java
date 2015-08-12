package com.thoughtworks.youthzone.helper;

import java.util.List;

public interface DatastoreFacade {
    List<String> getProjects() throws Exception; 
    List<String> getMembersForProject(String project) throws Exception;
}
