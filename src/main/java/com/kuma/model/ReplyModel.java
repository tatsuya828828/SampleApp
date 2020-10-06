package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyModel {
	private int id;
	private Date createdAt;
	private UserModel user;
	private CommentModel comment;
	private String reply;
}
