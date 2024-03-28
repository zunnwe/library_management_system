package com.librarymange.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.librarymange.entity.BookDetail;
import com.librarymange.entity.Category;
import com.librarymange.repository.BookCategoryMapper;

@Service
public class BookCategoryService {
	
	@Autowired
	BookCategoryMapper repo;

	public List<Category> getAllCategories(){
		List<Category> categories = new ArrayList<>();
		categories = repo.getAllCategories();
		return categories;
	}
	
	public Category findByCategoryName(String category_name) {
		Category category = new Category();
		category = repo.findByCategoryName(category_name);
		return category;
	}
	
	public void saveCategory(Category category2) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	Date cur_date = new Date();
		String created_date = dateFormat.format(cur_date);
		Category category = new Category();
		
    	category.setCategory_id(category2.getCategory_id());
    	category.setCategory_name(category2.getCategory_name());
    	category.setCreated_at(created_date);
		repo.saveCategory(category);
	}
	
}
