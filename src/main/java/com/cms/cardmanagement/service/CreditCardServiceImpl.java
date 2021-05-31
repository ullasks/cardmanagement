package com.cms.cardmanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.dao.CreditCardRepository;
import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.exception.TechnicalFailureException;
import com.cms.cardmanagement.mapper.CardMapper;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
import com.cms.cardmanagement.orm.CreditCard;
import com.cms.cardmanagement.service.validation.CreditCardValidatorServiceImpl;

@Service
public class CreditCardServiceImpl {

	Logger LOGGER = LoggerFactory.getLogger(CreditCardServiceImpl.class);

	@Autowired
	private CreditCardValidatorServiceImpl validator;
	@Autowired
	private CreditCardRepository creditCardRepository;

	public MessageModel createCreditCard(CreditCardModel cardModel)
			throws InvalidCreditCardException, TechnicalFailureException {

		MessageModel message = null;
		if (null != cardModel) {
			String invalidCreditCardExceptionMessage = CreditCardConstants.EMPTY;
			try {
				message = validator.creditCardMandatoryCheck(cardModel);
				if (CollectionUtils.isEmpty(message.getErrors())) {
					boolean isValidCreditCard = validator.isValidCreditcard(cardModel.getNumber());
					if (!isValidCreditCard) {
						invalidCreditCardExceptionMessage = String.format(CreditCardConstants.CARD_INVALID,
								cardModel.getNumber());
						throw new InvalidCreditCardException(invalidCreditCardExceptionMessage);
					} else {
						CreditCard cardExisted = checkCreditCardExists(cardModel.getNumber());
						if (cardExisted != null) {
							invalidCreditCardExceptionMessage = String.format(CreditCardConstants.CARD_EXISTS,
									cardModel.getNumber());
							throw new InvalidCreditCardException(invalidCreditCardExceptionMessage);
						}
						saveCreditCard(cardModel);
						message.setSuccess(CreditCardConstants.CARD_ADD_SUCCESS_MESSAGE);
					}
				}
			} catch (InvalidCreditCardException exception) {
				LOGGER.error("Caught Invalid CreditCard Exception!");
				throw new InvalidCreditCardException(invalidCreditCardExceptionMessage, exception);
			}
		} else {
			LOGGER.error("Caught Invalid CreditCard Exception!");
			throw new InvalidCreditCardException(CreditCardConstants.CARD_INVALID_NULL);
		}

		return message;
	}

	private CreditCard checkCreditCardExists(String number) {
		return creditCardRepository.getByNumber(number);
	}

	private void saveCreditCard(CreditCardModel cardModel) {
		CardMapper creditCardMapper = new CardMapper();
		CreditCard creditCard = creditCardMapper.beanToEntity(cardModel);
		creditCardRepository.save(creditCard);
	}
}
