package com.smart.controllers;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.entities.ContactUs;
import com.smart.helper.MessageErrorType;

@Controller
public class ContactController {

	@Autowired
	private JavaMailSender mailSender;

	//Create reusable model object of contact us entity
	@ModelAttribute
	public void handlingContactus(Model model) {
		model.addAttribute("contactUs", new ContactUs());
	}

	//Show contact-us form page
	@GetMapping("/contact")
	public String showContactForm(){
		return "contact-form";
	}

	//Submit contact us form
	@PostMapping("/contact")
	public String submitContact(@Valid @ModelAttribute ("contactUs") ContactUs contactUs,
			BindingResult result,Model model,HttpSession session) throws UnsupportedEncodingException, MessagingException {
		if (result.hasErrors()) {
			model.addAttribute("contactUs",contactUs );
			return "contact-form";
		}
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		String mailSubject=contactUs.getName()+" has sent a message";
		String mailText="<p><b>Sender Name:</b>"+contactUs.getName()+"</p>";
		mailText+="<p><b>Sender Email:</b>"+contactUs.getEmail()+"</p>";
		mailText+="<p><b>Subject:</b>"+contactUs.getSubject()+"</p>";
		mailText+="<p><b>Content:</b>"+contactUs.getContent()+"</p>";

		helper.setFrom("belal@gmail.com", "Contact Data Manager");
		helper.setTo("ajazansari11102@gmail.com");
		helper.setSubject(mailSubject);
		helper.setText(mailText,true);

		mailSender.send(message);
		session.setAttribute("message", new MessageErrorType("Thank you for contacting us, We will get back to you soon!","success"));

		return "message";	
	}


	@InitBinder 
	public void initBinder(WebDataBinder binder) {
		//Custom editor to trim the string containing white spaces for name attribute
		binder.registerCustomEditor(String.class, "name", new StringTrimmerEditor(true));

	}



}
