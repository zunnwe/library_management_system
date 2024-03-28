package com.librarymange.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.librarymange.entity.BookDetail;
import com.librarymange.entity.Category;
import com.librarymange.entity.User;
import com.librarymange.service.BookCategoryService;
import com.librarymange.service.BookDetailService;
import com.librarymange.service.UserDataService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LMSController {

	@Autowired
	BookCategoryService categoryService;
	@Autowired
	BookDetailService bookDetailService;
	@Autowired
	UserDataService userDataService;
	
	@GetMapping("/home")
	public String home(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "category_name", required = false,  defaultValue = "") String category_name,
			@RequestParam(name = "registration_id", required = false,  defaultValue = "") String registration_id,
			@RequestParam(name = "book_name", required = false,  defaultValue = "") String book_name,
			@RequestParam(name = "author", required = false,  defaultValue = "") String author,
			@RequestParam(name = "book_type", required = false,  defaultValue = "") String book_type) throws ServletException, IOException{
		List<Category> categories = new ArrayList<>();
		categories = categoryService.getAllCategories();
		List<BookDetail> books = new ArrayList<>();

		books = bookDetailService.getAllBooks(category_name, book_type, registration_id, author, book_name);
		model.addAttribute("categories", categories);
		model.addAttribute("book", books);
		if(request.getSession().getAttribute("username") != null){
    		model.addAttribute("username", request.getSession().getAttribute("username"));		
			 
        }
		return "screens/index";
	}
	
	@GetMapping("/login")
	public void login(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String username = null;
		response.setContentType("text/html");  
        if(request.getSession().getAttribute("username") == null){
        	User user = new User();
    		model.addAttribute("user", user);		
    		request.getRequestDispatcher("/screens/login.html").forward(request,response);
			 
        }else {
			/* response.sendRedirect(request.getContextPath()+"/home"); */
        	request.getRequestDispatcher("/screens/index.html").forward(request,
      			  response);
        }
	}
	
	@PostMapping("/loginSubmit")
	public String loginForm(@RequestParam("username") String name, 
			@RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("user:"+name);
		/* response.setContentType("text/html"); */
		String username = name;
        System.out.println("username: "+username);
        User user2 = userDataService.findByUsername(username);
        if(user2.getPassword().equals(password)){
            //Invalidating session if any
            System.out.println("User:"+username+" logged in successfully");
            request.getSession().invalidate();
            HttpSession newSession = request.getSession(true);
            newSession.setMaxInactiveInterval(300);
            // adding username to session
            newSession.setAttribute("username", name);
            // adding user role to session
            newSession.setAttribute("role", "admin");
			 
            return "redirect:/home";
        }else{
			 
        	return "screens/login";
        }
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "screens/register";
	}
	
	@PostMapping("/register")
	public String registerForm(Model model, @ModelAttribute("user") @Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			/* System.out.println("this is has-error state."); */
            return "screens/register";
        }else {
        	userDataService.registerUser(user);
        	return "redirect:/home";
        }
	}
	
	@GetMapping("/createBook")
	public String createBook(Model model, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		if(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("role") == "admin"){
			System.out.println(request.getSession().getAttribute("username"));
			BookDetail book = new BookDetail();
			List<Category> categories = bookDetailService.getAllBookCategories();
			List<String> category_names = new ArrayList<String>();
			for(int i = 0; i < categories.size(); i++) {
				String category_name = categories.get(i).getCategory_name();
				category_names.add(category_name);
			}
			
			model.addAttribute("book", book);
			model.addAttribute("categories", category_names);
			return "screens/createBook";
        }else{
            return "redirect:/login";
        }
		
	}
	
	@PostMapping("/createBookForm")
	public String saveBook(@RequestParam("file") MultipartFile file, 
			@RequestParam("registration_id") String registration_id, 
			@RequestParam("book_name") String book_name,
			@RequestParam("category_name") String category_name,
			@RequestParam("author") String author,
			@RequestParam("produced_year") String produced_year,
			@RequestParam("book_type") String book_type) {

			BookDetail book = new BookDetail();
			book.setRegistration_id(registration_id);
			book.setBook_name(book_name);
			book.setCategory_name(category_name);
			book.setAuthor(author);
			book.setProduced_year(produced_year);
			book.setBook_type(book_type);
			bookDetailService.saveBook(file, book);
			return "redirect:/home";
		
	}
	
	@GetMapping("/file/{id}")
	@ResponseBody
	public ResponseEntity<FileSystemResource> downloadFile(@PathVariable(name="id") String registration_id, HttpServletResponse response)throws IOException{
		File file = new File("./test.pdf");

	    BookDetail book = bookDetailService.findByRegID(registration_id);
	    try ( FileOutputStream fos = new FileOutputStream(file); ) {
	    	byte[] decoder = Base64.getDecoder().decode(book.getFile());
	    	fos.write(decoder);
	    	int fileLength = (int) file.length();
	    	response.setContentType("application/octet-stream");
	        response.addHeader("Content-Disposition", "attachment; filename=test.pdf");
	        response.setContentLength(fileLength);
	    	fos.close();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	    return new ResponseEntity<FileSystemResource>(
	            new FileSystemResource(file), HttpStatus.OK
	        );
	}
	
	@GetMapping("/update/{id}")
	public String update(@PathVariable(name="id") int id, Model model, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
		if(request.getSession().getAttribute("username") != null && request.getSession().getAttribute("role") == "admin"){
			BookDetail book = new BookDetail();
			book = bookDetailService.findByID(id);
			List<Category> categories = bookDetailService.getAllBookCategories();			
			model.addAttribute("book", book);
			model.addAttribute("categories", categories);
			return "screens/updateBook";
		}else{
            return "redirect:/login";
        }
		
	}
	
	@PostMapping("/updateBookForm")
	public String updateBook(@RequestParam("file") MultipartFile file,
			@RequestParam("id") int id,
			@RequestParam("registration_id") String registration_id, 
			@RequestParam("book_name") String book_name,
			@RequestParam("category_name") String category_name,
			@RequestParam("author") String author,
			@RequestParam("produced_year") String produced_year,
			@RequestParam("book_type") String book_type) {

			BookDetail book = new BookDetail();
			book.setId(id);
			book.setRegistration_id(registration_id);
			book.setBook_name(book_name);
			book.setCategory_name(category_name);
			book.setAuthor(author);
			book.setProduced_year(produced_year);
			book.setBook_type(book_type);
			bookDetailService.updateBook(file, book);
			return "redirect:/home";
		
	}
	
	@GetMapping("/createCategory")
	public String create(Model model, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		if(request.getSession().getAttribute("username") != null){
			Category category = new Category();
			model.addAttribute("category", category);
			return "screens/createCategory";
		}else{
            return "redirect:/login";
        }
		
	}
	
	@PostMapping("/createCategory")
	public String createCategory(Model model, 
			@RequestParam("category_id") String category_id,
			@RequestParam("category_name") String category_name) {

        	Category category = new Category();
        	category.setCategory_id(category_id);
        	category.setCategory_name(category_name);
        	categoryService.saveCategory(category);
        	return "redirect:/home";
	}
	
	public int checkUser(User user, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("this is checkuser");
		int success = 0;
		List<User> userList = userDataService.getAllUsers();
		for(int i = 0; i < userList.size(); i++) {
    		String name = userList.get(i).getName();
    		System.out.println("name: "+name);
    		System.out.println("name2: "+user.getName());
    		if(user.getName().equals(name)) {
    			if(userList.get(i).getPassword().equals(user.getPassword())) {
    				
    				System.out.println("User:"+name+" logged in successfully");
    	            request.getSession().invalidate();
    	            HttpSession newSession = request.getSession(true);
    	            newSession.setMaxInactiveInterval(300);
    	            // adding username to session
    	            newSession.setAttribute("username", name);
    	            // adding user role to session
    	            newSession.setAttribute("role", user.getRole());
    	            success = 1;
            	}else {
            		success = 0;
            	}
    		}
    	}
		return success;
	}
	
}
