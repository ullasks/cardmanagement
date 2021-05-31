package com.cms.cardmanagement.mapper;

import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.orm.CreditCard;

public class CardMapper {

	public CreditCard beanToEntity(CreditCardModel cardModel) {
		CreditCard creditCard = new CreditCard();
		creditCard.setBalance(cardModel.getBalance());
		creditCard.setCreditLimit(cardModel.getLimit());
		creditCard.setName(cardModel.getName());
		creditCard.setNumber(cardModel.getNumber());
		return creditCard;
	}
	
	public CreditCardModel entityToBean(CreditCard creditCard) {
		CreditCardModel model = new CreditCardModel();
		model.setBalance(creditCard.getBalance());
		model.setLimit(creditCard.getCreditLimit());
		model.setName(creditCard.getName());
		model.setNumber(creditCard.getNumber());
		return model;
	}
}
