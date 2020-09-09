package com.kuma.model;

import lombok.Data;

@Data
public class BookModel {
	private int id;
	private String title;
	private String body;
	private String author;
	private UserModel user;
	private int evaluation;
}
