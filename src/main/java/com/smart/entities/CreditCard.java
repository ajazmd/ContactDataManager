package com.smart.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data

public class CreditCard {
	private Integer firstFourDigit;
	private Integer secondFourDigit;
	private Integer thirdFourDigit;
	private Integer lastFourDigit;
	@Override
	public String toString() {
		return firstFourDigit + "-" + secondFourDigit+"-"+
				thirdFourDigit+"-"+ lastFourDigit;
	}
}
