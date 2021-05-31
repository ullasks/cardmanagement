package com.cms.cardmanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.exception.TechnicalFailureException;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
import com.cms.cardmanagement.service.validation.CreditCardValidatorServiceImpl;

@Service
public class CreditCardServiceImpl {

	Logger LOGGER = LoggerFactory.getLogger(CreditCardServiceImpl.class);

	@Autowired
	private CreditCardValidatorServiceImpl validator;

	public MessageModel createCreditCard(CreditCardModel cardModel)
			throws InvalidCreditCardException, TechnicalFailureException {
		
		MessageModel message = null;
		if (null != cardModel) {
			String invalidCreditCardExceptionMessage = CreditCardConstants.EMPTY;
			if (!validator.isValidCreditCardLength(cardModel.getNumber())) {
				invalidCreditCardExceptionMessage = String.format(CreditCardConstants.CARD_INVALID,
						cardModel.getNumber());
				throw new InvalidCreditCardException(invalidCreditCardExceptionMessage);
			}
		} else {
			LOGGER.error("Caught Invalid CreditCard Exception!");
			throw new InvalidCreditCardException(CreditCardConstants.CARD_INVALID_NULL);
		}

		return message;
	}
}
