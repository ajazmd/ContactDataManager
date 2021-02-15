package com.smart.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController
public class SearchController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ContactRepository contactRepository;

	//Create search code of contacts by name 
	@GetMapping("/search/{query}")
	public ResponseEntity<?> handleSearch(@PathVariable("query") String query, Principal principal){
		User user = userRepository.findByEmail(principal.getName()).get();
		List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}

}
