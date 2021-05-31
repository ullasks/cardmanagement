package com.cms.cardmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.service.validation.CreditCardValidatorServiceImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CreditCardServiceImplTest {

	private CreditCardServiceImpl testObj = null;
	@Mock
	private CreditCardValidatorServiceImpl mockCreditCardValidatorServiceImpl;

	@BeforeEach
	public void setup() {
		testObj = new CreditCardServiceImpl();
		ReflectionTestUtils.setField(testObj, "validator", mockCreditCardValidatorServiceImpl);
	}

	@Test
	public void inValidCreditCardlength() throws InvalidCreditCardException {
		String cardNumber = "638849890909009327914200";
		InvalidCreditCardException thrown = Assertions.assertThrows(InvalidCreditCardException.class, () -> {
			CreditCardModel model = new CreditCardModel();
			model.setNumber(cardNumber);
			testObj.createCreditCard(model);
		});
		String expectedMessage = String.format("The Credit card number %s is wrong! Please check the card number!",
				cardNumber);
		Assertions.assertEquals(expectedMessage, thrown.getMessage());
	}
	
	@Test
	public void inValidCreditCardlengthNull() throws InvalidCreditCardException {
		InvalidCreditCardException thrown = Assertions.assertThrows(InvalidCreditCardException.class, () -> {
			CreditCardModel model = null;
			testObj.createCreditCard(model);
		});
		Assertions.assertEquals(CreditCardConstants.CARD_INVALID_NULL, thrown.getMessage());
	}
}
