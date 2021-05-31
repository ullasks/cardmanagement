package com.cms.cardmanagement.service.validation;

import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
import com.cms.cardmanagement.util.NumberUtil;

@Service
public class CreditCardValidatorServiceImpl {

	public MessageModel creditCardMandatoryCheck(CreditCardModel cardModel) {
		MessageModel message = new MessageModel();
		if (ObjectUtils.isEmpty(cardModel.getName())) {
			message.getErrors().add(CreditCardConstants.CARD_NAME_MANDATORY);
		}
		if (ObjectUtils.isEmpty(cardModel.getNumber())) {
			message.getErrors().add(CreditCardConstants.CARD_NUMBER_MANDATORY);
		}
		if (ObjectUtils.isEmpty(cardModel.getLimit())) {
			message.getErrors().add(CreditCardConstants.CARD_LIMIT_MANDATORY);
		}
		return message;
	}

	public boolean isValidCreditcard(String cardNumber) {
		boolean isValid = NumberUtil.isNumeric(cardNumber) && isValidCreditCardLength(cardNumber);
		if (isValid) {
			isValid = creditcardBusinessValidation(cardNumber);
		}
		return isValid;
	}

	private boolean creditcardBusinessValidation(String cardNumber) {
		Long creditCardNumber = Long.valueOf(cardNumber);
		// create a int stream with values (1,2,1,2,1,2,1,2 ...) with the length of credit card. 
		OfInt integerMultiplierSeries = IntStream.iterate(1, i -> 3 - i).
				limit(cardNumber.length()).iterator();
		// reverse the number using the modulus ( % )  and divide by 10 method.
		LongStream reverseCardNumber = LongStream.iterate(creditCardNumber, n -> n / 10).takeWhile(n -> n > 0)
				.map(n -> n % 10);
		long sum = reverseCardNumber.map(i -> i * integerMultiplierSeries.nextInt()).reduce(0, (a, b) -> a + b / 10 + b % 10);
		return (sum % 10) == 0;
	}

	private boolean isValidCreditCardLength(String creditCardNumber) {
		return creditCardNumber.length() >= 13 && creditCardNumber.length() <= 19;
	}
}
