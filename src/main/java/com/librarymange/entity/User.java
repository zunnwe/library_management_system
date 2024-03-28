package com.librarymange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private int id;
	private String name;
	private String password;
	private String email;
	private String phone;
	private String created_at;
	private String updated_at;
	private String deleted_at;
	private String role;
}
