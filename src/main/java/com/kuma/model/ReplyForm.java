package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyForm {
	public int id;
	public Date createdAt;
	public UserModel user;
	public CommentModel comment;
	public String reply;
}
