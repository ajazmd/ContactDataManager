package com.smart.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.smart.entities.CreditCard;

public class MyCreditCardEditor extends PropertyEditorSupport{


	@Override
	public String getAsText() {

		return "Enter in format 1111-2222-3333-4444";
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		CreditCard cc=new CreditCard();
		String[] arr=text.split("-");
		cc.setFirstFourDigit(Integer.valueOf(arr[0]));
		cc.setSecondFourDigit(Integer.valueOf(arr[1]));
		cc.setThirdFourDigit(Integer.valueOf(arr[2]));
		cc.setLastFourDigit(Integer.valueOf(arr[3]));
		setValue(cc);

	}
}
