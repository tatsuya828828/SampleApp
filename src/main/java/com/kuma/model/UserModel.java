package com.kuma.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserModel {
	private int id;
	private Date lastLogin;
	private Date createdAt;
	private String selfId;
	private String name;
	private String password;
}
