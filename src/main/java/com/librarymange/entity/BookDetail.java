package com.librarymange.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class BookDetail {

	@Id
	private int id;
	private String registration_id;
	private String category_name;
	private String book_name;
	private String author;
	private String produced_year;
	private String book_type;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String file;
	private String status;
	private String created_at;
	private String updated_at;
	private String deleted_at;
}
