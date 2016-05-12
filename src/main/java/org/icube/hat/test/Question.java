package org.icube.hat.test;

import java.util.List;

public class Question {
	private int questionId;
	private String questionText;
	private String questionImageId;
	private List<SubQuestion> subQuestionList;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionImageId() {
		return questionImageId;
	}

	public void setQuestionImageId(String questionImageId) {
		this.questionImageId = questionImageId;
	}

	public List<SubQuestion> getSubQuestionList() {
		return subQuestionList;
	}

	public void setSubQuestionList(List<SubQuestion> subQuestionList) {
		this.subQuestionList = subQuestionList;
	}
}
