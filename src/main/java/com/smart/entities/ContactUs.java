package com.smart.entities;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactUs {

	@NotEmpty(message = "*Name cannot be empty")
	private String name;
	@Email(regexp = ".+@.+\\..+", message = "*Please provide a valid email ")
	private String email;
	private String subject;
	private String content;
}
