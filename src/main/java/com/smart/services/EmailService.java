package com.smart.services;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;


	boolean status=false;

	public boolean sendEmail(String to,String subject,String text)  {
		try {
			MimeMessage message=mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);
			helper.setFrom("belal@gmail.com", "Smart Contact Manager");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text,true);
			mailSender.send(message);
			status=true;

		} catch (UnsupportedEncodingException|MessagingException e) {
			status=false;
			e.printStackTrace();
		}
		return status;
	}
}
