package com.smart.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Age {
	String message() default "*Your age must lie between {lower}-{upper}";
	int lower() default 18;
	int upper() default 60;
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
