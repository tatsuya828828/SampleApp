package com.kuma.model;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class SignupForm {
	private int id;
	@NotBlank(groups= Valid1.class)
	@Email(groups = Valid2.class)
	private String selfId;
	@NotBlank(groups= Valid1.class)
	@Length(min = 6, max = 100, groups = Valid2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Valid3.class)
	private String password;
	@NotBlank(groups= Valid1.class)
	private String name;
}
