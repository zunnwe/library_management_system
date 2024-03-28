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
import org.springframework.web.multipart.MultipartFile;

import com.librarymange.entity.BookDetail;
import com.librarymange.entity.Category;
import com.librarymange.repository.BookDetailMapper;

import org.springframework.util.StringUtils;

@Service
public class BookDetailService {

	@Autowired
	BookDetailMapper repo;
	
	/*
	 * public List<BookDetail> getAllBooks(String category_name, String book_type){
	 * List<BookDetail> books = new ArrayList<>(); books =
	 * repo.getAllBooks(category_name, book_type); System.out.println("books" +
	 * books); return books; }
	 */
	
	public List<BookDetail> getAllBooks(String category_name, String book_type, String registration_id, String author, String book_name){
//	public List<BookDetail> getAllBooks(String category_name){
		List<BookDetail> books = new ArrayList<>();
		books = repo.getAllBooks(category_name, book_type, registration_id, author, book_name);
//		books = repo.getAllBooks(category_name);
		System.out.println("books" + books);
		return books;
	}
	
	public List<Category> getAllBookCategories(){
		List<Category> categories = new ArrayList<Category>();
		categories = repo.getAllBookCategories();
		return categories;
	}
	
	public BookDetail findByID(int id) {
		BookDetail book = new BookDetail();
		book = repo.findByID(id);
		return book;
	}
	
	public BookDetail findByRegID(String registration_id) {
		BookDetail book = new BookDetail();
		book = repo.findByRegID(registration_id);
		return book;
	}
	
	/*
	 * public List<BookDetail> findByCategoryName(String category_name){
	 * List<BookDetail> books = new ArrayList<>(); books =
	 * repo.findByCategoryName(category_name); return books; }
	 */
	
	public void saveBook(MultipartFile file, BookDetail book2) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	Date cur_date = new Date();
		String created_date = dateFormat.format(cur_date);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		BookDetail book = new BookDetail();
		
		if(!fileName.contains("..")) {
			try {
				book.setFile(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	book.setRegistration_id(book2.getRegistration_id());
    	book.setCategory_name(book2.getCategory_name());
    	book.setBook_name(book2.getBook_name());
    	book.setAuthor(book2.getAuthor());
    	book.setProduced_year(book2.getProduced_year());
    	book.setBook_type(book2.getBook_type());
    	book.setStatus("available");
    	book.setCreated_at(created_date);
		repo.saveBook(book);
	}
	
	public void updateBook(MultipartFile file, BookDetail book2) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	Date cur_date = new Date();
		String updated_date = dateFormat.format(cur_date);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		BookDetail book = new BookDetail();
		
		if(!fileName.contains("..")) {
			try {
				book.setFile(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		book.setId(book2.getId());
    	book.setRegistration_id(book2.getRegistration_id());
    	book.setCategory_name(book2.getCategory_name());
    	book.setBook_name(book2.getBook_name());
    	book.setAuthor(book2.getAuthor());
    	book.setProduced_year(book2.getProduced_year());
    	book.setBook_type(book2.getBook_type());
    	book.setStatus("available");
    	book.setUpdated_at(updated_date);
		this.repo.updateBook(book);
	}
}
