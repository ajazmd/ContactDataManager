package com.smart.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<Age, Integer> {
	private int lower;
	private int upper;
	@Override
	public void initialize(Age constraintAnnotation) {
		this.lower=constraintAnnotation.lower();
		this.upper=constraintAnnotation.upper();
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	@Override
	public boolean isValid(Integer userEnteredValue, ConstraintValidatorContext context) {
		if(userEnteredValue==null||userEnteredValue<lower||userEnteredValue>upper) 
			return false;
		return true;
	}
}
