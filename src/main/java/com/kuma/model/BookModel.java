package com.kuma.model;

import lombok.Data;

@Data
public class BookModel {
	private String title;
	private String newTitle;
	private String body;
	private UserModel user;
}
