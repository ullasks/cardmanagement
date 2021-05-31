package com.cms.cardmanagement.service.validation;

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
		return true;
	}

	private boolean isValidCreditCardLength(String creditCardNumber) {
		return creditCardNumber.length() >= 13 && creditCardNumber.length() <= 19;
	}
}
