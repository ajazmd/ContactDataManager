package com.smart.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalController{
	
	@ExceptionHandler(value=NullPointerException.class)
	public String handleNullPointerException(Model model) {
		model.addAttribute("expmsg", "Null pointer exception has occured");
		return "globalexception";
		}
	@ExceptionHandler(value=IllegalStateException.class)
	public String handleIllegalStateException(Model model) {
		model.addAttribute("expmsg", "Illegal state exception has occured");
		return "globalexception";
	}
	@ExceptionHandler(value=NumberFormatException.class)
	public String handleNumberFormatException(Model model) {
		model.addAttribute("expmsg", "Number format exception has occured");
		return "globalexception";
	}
	@ExceptionHandler(value=Exception.class)
	public String handleAnyException(Model model) {
		model.addAttribute("expmsg", "Try again or Contact support with us");
		return "globalexception";
	}
	
	
	
		
	}

