package com.cms.cardmanagement.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.util.ReflectionTestUtils;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
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

	public static Object[][] creditcardNumberCombinations() {
		return new Object[][] { { "638849890909009327914200" }, { "63884oiyo27914200" }, { "ljlkk**7hlhasd" } };
	}

	@ParameterizedTest
	@MethodSource("creditcardNumberCombinations")
	public void inValidCreditCardlength(String creditCardNumber) throws InvalidCreditCardException {
		String cardNumber = creditCardNumber;
		CreditCardModel model = new CreditCardModel();
		MessageModel message = new MessageModel();
		model.setNumber(cardNumber);
		Mockito.when(mockCreditCardValidatorServiceImpl.creditCardMandatoryCheck(model)).thenReturn(message);
		Mockito.when(mockCreditCardValidatorServiceImpl.isValidCreditcard(model.getNumber())).thenReturn(false);
		InvalidCreditCardException thrown = Assertions.assertThrows(InvalidCreditCardException.class, () -> {
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
