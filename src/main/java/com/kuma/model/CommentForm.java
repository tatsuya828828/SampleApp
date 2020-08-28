package com.kuma.model;

import lombok.Data;

@Data
public class CommentForm {
	public UserModel user;
	public BookModel book;
	public String Comment;
}
