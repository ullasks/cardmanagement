package com.cms.cardmanagement.model;

import java.util.ArrayList;
import java.util.List;

public class MessageModel {
	
private String success;
	
	private List<String> errors = new ArrayList<>();

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
