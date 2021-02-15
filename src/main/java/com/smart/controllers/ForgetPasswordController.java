package com.smart.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.services.EmailService;

@Controller
public class ForgetPasswordController {

	@Autowired
	private BCryptPasswordEncoder bpass;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;

	Random random=new Random();

	//Show email form page to ask registered email of user 
	@GetMapping("/show-email")
	public String showRegisteredEmailPage() {
		return "registered-email-form";
	}

	//Send OTP to user mail
	@PostMapping("/verify-otp")
	public String showVerifyOtpPage(@RequestParam String email,HttpSession session) throws UnsupportedEncodingException, MessagingException {
		Integer otp=random.nextInt(99999);

		String subject="You got a message regarding otp";
		String text="<p><b>Your OTP is :</b>"+otp+"</p>";
		boolean status=emailService.sendEmail(email, subject, text);

		if(status) {
			session.setAttribute("message", "Successfully sent OTP to your email");
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify-otp";
		}
		else {
			session.setAttribute("message", "Check your email Id");
			return "registered-email-form";
		}


	}

	//Validate OTP 
	@PostMapping("/submit-otp")
	public String submitOtp(@RequestParam ("userEnteredOtp") int userEnteredOtp,HttpSession session) {
		int otp = (Integer) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (userEnteredOtp==otp) {
			Optional<User> user = userRepository.findByEmail(email);
			if(user.isPresent()) {
				return "change-password-form";
			}
			else {
				session.setAttribute("message", "User |"+email+"| does not exist");
				return "registered-email-form";
			}

		} 
		else {
			session.setAttribute("message", "Please enter valid OTP");
			return "verify-otp";
		}
	}

	//Change  password 
	@PostMapping("/submit-password")
	public String submitPassword(@RequestParam String password,HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user = userRepository.findByEmail(email).get();
		user.setPassword(bpass.encode(password));
		userRepository.save(user);
		session.setAttribute("message", "Your password has been changed");
		return "login";

	}

}
