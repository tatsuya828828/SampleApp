package com.kuma.model;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class BookForm {
	public int id;
	@NotBlank(groups= Valid1.class)
	public String title;
	@NotBlank(groups= Valid1.class)
	public String body;
	public String author;
	public String genre;
	public UserModel user;
	public int evaluation;
}
