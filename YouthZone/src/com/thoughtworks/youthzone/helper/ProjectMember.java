package com.thoughtworks.youthzone.helper;

public class ProjectMember {


	private String projectMemberId;
	private String name;
	private String birthDate;
	private String memberId;
	private String salesForceId;
	
	public ProjectMember(String projectMemberId, String name, String birthDate, String memberId, String salesForceId) {
		this.projectMemberId = projectMemberId;
		this.name = name;
		this.birthDate = birthDate;
		this.memberId = memberId;
		this.salesForceId = salesForceId;
	}
	

	public String getProjectMemberId() {
		return projectMemberId;
	}
	
	public String getName() {
		return name;
	}

	public String getBirthdate() {
		return birthDate;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getSalesForceId() {
		return salesForceId;
	}
	
	public String toString(){
		return name + " " + birthDate + " " + memberId;
	}
	
}
