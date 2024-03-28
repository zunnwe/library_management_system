package com.librarymange.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymange.entity.User;
import com.librarymange.repository.UserDataMapper;

@Service
public class UserDataService {

	@Autowired
	UserDataMapper userRepo;
	
	public List<User> getAllUsers(){
		System.out.println("called");
		List<User> users = new ArrayList<>();
		/* users = userRepo.getAllUsers(); */
		userRepo.getAllUsers();
		return users;
	}
	
	public User findByUsername(String name) {
		User user = new User();
		user = userRepo.findByUsername(name);
		return user;
	}
	
	public void registerUser(User user2) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	Date cur_date = new Date();
		String created_date = dateFormat.format(cur_date);
		User user = new User();
    	user.setName(user2.getName());
    	user.setPassword(user2.getPassword());
    	user.setEmail(user2.getEmail());
    	user.setPhone(user2.getPhone());
    	user.setCreated_at(created_date);
    	user.setRole("admin");
        userRepo.registerUser(user);
	}
}
