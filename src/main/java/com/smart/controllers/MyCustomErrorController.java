package com.smart.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.helper.MessageErrorType;

@Controller
public class MyCustomErrorController implements ErrorController {

	private static Logger logger=LoggerFactory.getLogger(MyCustomErrorController.class);

	@Override
	public String getErrorPath() {
		return "/error";
	}

	//Show error page in customized way
	@RequestMapping("/error")
	public String showError(HttpServletRequest request,HttpSession session,Model model){

		String errorPage="error";
		String title="Error";
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if(status!=null) {
			Integer statusCode = Integer.valueOf(status.toString());
			if(statusCode==HttpStatus.NOT_FOUND.value()) {
				title="Page Not Found";
				errorPage="error/404";
				logger.error("Error 404");
				session.setAttribute("message", new MessageErrorType("The page you requested is not found", "danger"));

			}
			else if(statusCode==HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				title="Internal Server Error";
				errorPage="error/500";
				logger.error("Error 500");
				session.setAttribute("message", new MessageErrorType("Sorry! The server has encountered a problem", "danger"));

			}
			else if(statusCode==HttpStatus.FORBIDDEN.value()) {
				title="Forbidden error";
				errorPage="error/403";
				logger.error("Error 403");
				session.setAttribute("message", new MessageErrorType("You don't have permission to see this", "danger"));

			}

		}
		model.addAttribute("title", title);
		return errorPage;
	}


}
