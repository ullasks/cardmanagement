package com.cms.cardmanagement.service.validation;

import org.springframework.stereotype.Service;

@Service
public class CreditCardValidatorServiceImpl {

	public boolean isValidCreditCardLength(String creditCardNumber) {
		return creditCardNumber.length() >= 13 && creditCardNumber.length() <= 19;
	}
}
