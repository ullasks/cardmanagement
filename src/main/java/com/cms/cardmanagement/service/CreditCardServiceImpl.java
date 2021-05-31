package com.cms.cardmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.dao.CreditCardRepository;
import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.exception.TechnicalFailureException;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
import com.cms.cardmanagement.service.validation.CreditCardValidatorServiceImpl;

@Service
public class CreditCardServiceImpl {

	@Autowired
	private CreditCardValidatorServiceImpl validator;
	
	
	public MessageModel createCreditCard(CreditCardModel cardModel) throws InvalidCreditCardException,TechnicalFailureException{
		MessageModel message =null;
		String invalidCreditCardExceptionMessage=CreditCardConstants.EMPTY;
		if(!validator.isValidCreditCardLength(cardModel.getNumber())){
			invalidCreditCardExceptionMessage = String.format(CreditCardConstants.CARD_INVALID,cardModel.getNumber());
			throw new InvalidCreditCardException(invalidCreditCardExceptionMessage);
		}
		return message;
	}
}
