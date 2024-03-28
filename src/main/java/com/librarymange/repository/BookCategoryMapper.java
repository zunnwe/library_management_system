package com.librarymange.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.librarymange.entity.Category;

@Mapper
public interface BookCategoryMapper {

	List<Category> getAllCategories();
	
	Category findByCategoryName(String category_name);
	
	void saveCategory(Category category);
}
