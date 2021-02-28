package com.smart.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smart.entities.User;

public class EmailValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user=(User)target;
		if(!user.getEmail().endsWith(".com")) {
			errors.rejectValue("email", "email.invalidstring","*String must ends with .com");
		}


	}

}
