package com.kuma.model;

import lombok.Data;

@Data
public class EvaluationModel {
	private Integer evaluation;
	private UserModel user;
	private BookModel book;
}
