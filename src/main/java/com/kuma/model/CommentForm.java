package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentForm {
	private int id;
	public Date createdAt;
	public int evaluation;
	public String comment;
	public UserModel user;
	public BookModel book;
}
