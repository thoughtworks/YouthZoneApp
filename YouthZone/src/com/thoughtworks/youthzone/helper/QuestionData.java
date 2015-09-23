package com.thoughtworks.youthzone.helper;

public class QuestionData {

	private String outcome;
	private String question;
	private Float rating;
	private String memberComment;
	private String memberCommentField;

	public QuestionData(String outcome, String question, Float rating, String memberComment,
			String memberCommentField) {
		super();
		this.outcome = outcome;
		this.question = question;
		this.rating = rating;
		this.memberComment = memberComment;
		this.memberCommentField = memberCommentField;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public String getMemberComment() {
		return memberComment;
	}

	public void setMemberComment(String memberComment) {
		this.memberComment = memberComment;
	}

	public String getMemberCommentField() {
		return memberCommentField;
	}

	public void setMemberCommentField(String memberCommentField) {
		this.memberCommentField = memberCommentField;
	}

}
