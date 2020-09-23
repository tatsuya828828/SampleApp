package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentModel {
	private Date createdAt;
	private String comment;
	private UserModel user;
	private BookModel book;
}
