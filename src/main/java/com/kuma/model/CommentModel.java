package com.kuma.model;

import lombok.Data;

@Data
public class CommentModel {
	private String comment;
	private UserModel user;
	private BookModel book;
}
