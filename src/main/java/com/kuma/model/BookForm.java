package com.kuma.model;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class BookForm {
	public int id;
	public Date createdAt;
	@NotBlank(groups= Valid1.class)
	public String title;
	@NotBlank(groups= Valid1.class)
	public String body;
	public String author;
	public String genre;
	public UserModel user;
	public int evaluation;
}
