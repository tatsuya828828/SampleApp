package com.kuma.model;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class BookForm {
	@NotBlank(groups= Valid1.class)
	public String title;
	public String newTitle;
	@NotBlank(groups= Valid1.class)
	public String body;
	public String userId;
}
