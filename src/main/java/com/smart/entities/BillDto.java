package com.smart.entities;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class BillDto {
	private CreditCard creditCard;
	private BigDecimal amount;
	private Currency currency;
	private Date date;



}
