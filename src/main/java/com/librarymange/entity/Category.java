package com.librarymange.entity;

import lombok.Data;

@Data
public class Category {

	private int id;
	private String category_id;
	private String category_name;
	private String created_at;
	private String updated_at;
	private String deleted_at;
}
