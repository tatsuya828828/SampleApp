package com.kuma.model;

import lombok.Data;

@Data
public class UserModel {
	private int id;
	private String selfId;
	private String name;
	private String password;
}
