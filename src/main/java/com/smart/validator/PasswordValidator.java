package com.smart.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String regex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher m=pattern.matcher(user.getPassword());
		if(!m.matches()) {
			errors.rejectValue("password", "password.invalid", "Read the instructions and enter valid input");
		}


	}

}
