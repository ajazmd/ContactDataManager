package com.smart.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.smart.entities.CaptchaResponse;

@Component
public class CaptchaValidator {
	
    @Autowired
	private RestTemplate rt;
	
	public boolean isValid(String captcha) {
		boolean b=false;
	
		String url="https://www.google.com/recaptcha/api/siteverify ";
		String params="?secret=6Lfz810aAAAAAPhDN2JswKLxMKQ5UZjqyWujR-Pg&response="+captcha;
		try {
			CaptchaResponse resp1 = rt.postForObject(url+params, null, CaptchaResponse.class);
			b=resp1.isSuccess();
			} catch (Exception e) {
			System.out.println(e);
		}
		return b;
		
	}

}
