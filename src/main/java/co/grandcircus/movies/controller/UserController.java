/*
 * Source Material (c) 2016 GCD
 * All rights reserved
 */
package co.grandcircus.movies.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import co.grandcircus.movies.dao.UserDao;
import co.grandcircus.movies.model.User;

/**
 * Handles requests for pages related to users
 */
@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserDao userDao;
	
	
	
	
	/**
	 * List all users
	 */
	@RequestMapping("/users")
	public String listUsers(Model model, @RequestParam(value = "sort", required = false) String sort) {
		List<User> users;
		
		if (sort == null) {
			users = userDao.getAllUsers();
		} else {
			try {
				users = userDao.getAllUsersSortedBy(sort);
			} catch (IllegalArgumentException ex) {
				logger.warn(ex.getMessage());
				users = userDao.getAllUsers();
			}
		}
		model.addAttribute("users", users);
		

		logger.info("/users -> user-list.jsp");
		return "user-list";
	}
	
	/**
	 * Display one user
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String displayUser(@PathVariable int id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("user", userDao.getUser(id));

		logger.info("GET /users/" + id + " -> user.jsp");
		return "user";
	}
	
	/**
	 * Save one user
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
	public String saveUser(@PathVariable int id, User user, Model model) {
		userDao.updateUser(id, user);
		model.addAttribute("id", id);
		model.addAttribute("user", user);
		
		logger.info("POST /users/" + id + " -> user.jsp");
		return "user";
	}
	
	/**
	 * Delete one user
	 */
	@RequestMapping(value = "/users/{id}/delete", method = RequestMethod.POST)
	public String deleteUser(@PathVariable int id, Model model) {
		userDao.deleteUser(id);
		model.asMap().clear();
		
		logger.info("POST /users/" + id + "/delete -> redirect to /users");
		return "redirect:/users";
	}
	
	/**
	 * Display one user
	 */
	@RequestMapping(value = "/users/create", method = RequestMethod.GET)
	public String createUserForm(Model model) {
		model.addAttribute("user", new User());
		
		logger.info("GET /users/create -> user-create.jsp");
		return "user-create";
	}
	
	/*
	 * Display Login Page
	 */
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm(Model model) {
		model.addAttribute("user", new User());
		
		logger.info("GET /login -> login.jsp");
		return "login";
	}
	
	/*
	 * Get User by email and password
	 */
	@RequestMapping(value = "/users/user", method = RequestMethod.POST)
	public String displayUserByEmailPwd(
										 @RequestParam(value="email") String email, 
										 @RequestParam(value="password")String password, Model model) {
		
		model.addAttribute("user", userDao.getUserByEmailAndPassword(email,password));

		
		return "user";
	}
	
	
	/**
	 * Save new user
	 */
	@RequestMapping(value = "/users/create", method = RequestMethod.POST)
	public String createUser(User user, Model model) {
		userDao.addUser(user);
		model.asMap().clear();
		
		logger.info("POST /users/create -> redirect to /users");
		return "redirect:/users";
	}
	
}
