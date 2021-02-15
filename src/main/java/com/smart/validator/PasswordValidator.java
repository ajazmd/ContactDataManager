package com.smart.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smart.entities.User;

public class PasswordValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user=((User)target);
		if(user.getPassword().length()>8) {
			errors.rejectValue("password", "password.invalid", "Enter more than 7 character");
		}


	}

}
