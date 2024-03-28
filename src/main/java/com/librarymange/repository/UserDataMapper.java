package com.librarymange.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.librarymange.entity.User;

@Mapper
public interface UserDataMapper {

	void registerUser(User user);
	
	List<User> getAllUsers();
	
	User findByUsername(String name);
	
}
