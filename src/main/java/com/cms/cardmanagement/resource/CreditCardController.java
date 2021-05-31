package com.cms.cardmanagement.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.exception.TechnicalFailureException;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
import com.cms.cardmanagement.service.CreditCardServiceImpl;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {
	
	Logger LOGGER = LoggerFactory.getLogger(CreditCardController.class);

	@Autowired
	private CreditCardServiceImpl creditCardServiceImpl;

	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<MessageModel> createCreditcard(@RequestBody CreditCardModel model)
			throws InvalidCreditCardException, TechnicalFailureException {
		MessageModel response = new MessageModel();
		try {
			response = creditCardServiceImpl.createCreditCard(model);
		} catch (InvalidCreditCardException exception) {
			response.getErrors().add(exception.getMessage());
			return new ResponseEntity<MessageModel>(response, HttpStatus.BAD_REQUEST);
		} catch (TechnicalFailureException exception) {
			response.getErrors().add(exception.getMessage());
			return new ResponseEntity<MessageModel>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			response.getErrors().add(exception.getMessage());
			return new ResponseEntity<MessageModel>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CreditCardModel>> getAllCreditcards() {
		LOGGER.info("Fetching the credit card informations");
		List<CreditCardModel> creditCards = creditCardServiceImpl.getAllCreditCards();
		if(CollectionUtils.isEmpty(creditCards)) {
			LOGGER.info("No cards founds:: ");
			return new ResponseEntity<List<CreditCardModel>>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<>(creditCards, HttpStatus.OK);
		}
	}
}
