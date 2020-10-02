package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentModel {
	private int id;
	private Date createdAt;
	private int evaluation;
	private String comment;
	private UserModel user;
	private BookModel book;
}
