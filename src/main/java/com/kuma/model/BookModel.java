package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class BookModel {
	private int id;
	private Date createdAt;
	private String image;
	private String title;
	private String body;
	private String author;
	private String genre;
	private UserModel user;
	private int evaluation;
}
