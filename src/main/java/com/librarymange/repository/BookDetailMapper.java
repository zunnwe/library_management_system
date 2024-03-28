package com.librarymange.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.librarymange.entity.BookDetail;
import com.librarymange.entity.Category;

@Mapper
public interface BookDetailMapper {
	
	List<BookDetail> getAllBooks(String category_name, String book_type, String registration_id,String author, String book_name); 
	
	List<Category> getAllBookCategories();
	
	BookDetail findByID(int id);
	
	BookDetail findByRegID(String registration_id);
	
	/* List<BookDetail> findByCategoryName(String category_name); */
	
	void saveBook(BookDetail bookdetail);
	
	void updateBook(BookDetail bookdetail);
}
