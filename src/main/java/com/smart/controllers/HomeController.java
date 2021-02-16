package com.smart.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.entities.UserUpdate;
import com.smart.helper.MessageErrorType;
import com.smart.validator.EmailValidator;
import com.smart.validator.PasswordValidator;



@Controller
@SessionAttributes("user")
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;



	//Home page of the application
	@GetMapping("/")
	public String showHome(Model model) {
		model.addAttribute("title", "Home- Contact Data Manager");
		return "home";
	}

	//Sign up page
	@GetMapping("/signup")
	public String showSignup(Model model) {
		model.addAttribute("title", "Register- Contact Data Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	//Submit registration form
	@PostMapping("/processingRegister")
	public String handleRegistration(@Valid @ModelAttribute("user") User user,
			BindingResult result, Model model,HttpSession session
			) {
		if(result.hasErrors()) {
			System.out.println(result.toString());
			model.addAttribute("user", user);
			session.setAttribute("message", new MessageErrorType("Try again by following the given guidelines","danger"));
			return "signup";
		}
		user.setRole("ROLE_USER");
		user.setImageUrl("default.png");
		user.setEnabled(true);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		User data =userRepository.save(user);
		session.setAttribute("message", new MessageErrorType(user.getName().toUpperCase()+" , Successfully registered!","success"));
		return "signup";

	}

	//Show login page and preventing user going back to login if already logged in
	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("title", "Login-Contact Data Manager");
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null||authentication instanceof AnonymousAuthenticationToken)
			return"login";
		return "redirect:/";

	}

	//Show failure page after login unsuccessful
	@GetMapping("/failure")
	public String showFailure(Model model,HttpSession session) {
		model.addAttribute("title", "Login Failure-Contact Data Manager");
		session.setAttribute("message", "Incorrect Username/Password!");
		return"login";

	}

	//Show update form of user by ID
	@PostMapping("/update/{id}")
	public String showUpdate(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("title", "update");
		User user = userRepository.findById(id).get();
		model.addAttribute("user", user);
		return "user-update-form";
	}

	//Submit update form by fetching user entered values
	@PostMapping("/update-user")
	public String showUpdate(@ModelAttribute("user") UserUpdate userUpdate,
			Model model,HttpSession session) {
		try {
			Optional<User> user1 = userRepository.findById(userUpdate.getId());
			if(user1.isPresent()) {
				User user = user1.get();
				user.setName(userUpdate.getName());
				user.setEmail(userUpdate.getEmail());
				user.setAge(userUpdate.getAge());
				user.setAbout(userUpdate.getAbout());
				userRepository.save(user);
				session.setAttribute("message", new MessageErrorType("Your profile is successfully updated","success"));
			}
			else {
				session.setAttribute("message", new MessageErrorType("Something went wrong! Try again","danger"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}


		return "user-update-form";
	}

	//Delete user by Id
	@PostMapping("/delete/{id}") 
	public String deleteUser(@PathVariable Integer id,HttpSession session) { 
		userRepository.deleteById(id);
		return "";

	}

	//Implement init binder for email and password
	@InitBinder
	public void emailAndPasswordInitBinder(WebDataBinder binder) {
		binder.addValidators(new EmailValidator());
		binder.addValidators(new PasswordValidator());
	}



}
