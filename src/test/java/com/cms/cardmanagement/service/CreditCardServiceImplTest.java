package com.cms.cardmanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.util.ReflectionTestUtils;

import com.cms.cardmanagement.constants.CreditCardConstants;
import com.cms.cardmanagement.dao.CreditCardRepository;
import com.cms.cardmanagement.exception.InvalidCreditCardException;
import com.cms.cardmanagement.exception.TechnicalFailureException;
import com.cms.cardmanagement.model.CreditCardModel;
import com.cms.cardmanagement.model.MessageModel;
import com.cms.cardmanagement.orm.CreditCard;
import com.cms.cardmanagement.service.validation.CreditCardValidatorServiceImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CreditCardServiceImplTest {

	private CreditCardServiceImpl testObj = null;
	@Mock
	private CreditCardValidatorServiceImpl mockCreditCardValidatorServiceImpl;
	
	@Mock
	private CreditCardRepository mockCreditCardRepository;

	@BeforeEach
	public void setup() {
		testObj = new CreditCardServiceImpl();
		ReflectionTestUtils.setField(testObj, "validator", mockCreditCardValidatorServiceImpl);
		ReflectionTestUtils.setField(testObj, "creditCardRepository", mockCreditCardRepository);
	}

	public static Object[][] creditcardNumberCombinations() {
		return new Object[][] { { "638849890909009327914200" }, { "63884oiyo27914200" }, { "ljlkk**7hlhasd" } };
	}
	public static Object[][] creditcardMandatoryData() {
		return new Object[][] { { null ,"6388498327914201", new BigDecimal("9000"),CreditCardConstants.CARD_NAME_MANDATORY},
			{ "MY CREDITCARD" ,null, new BigDecimal("9000"), CreditCardConstants.CARD_NUMBER_MANDATORY},
			{ "TEST" ,"6388498327914201", null, CreditCardConstants.CARD_LIMIT_MANDATORY}, };
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

	@ParameterizedTest
	@MethodSource("creditcardMandatoryData")
	public void mandatoryCheck(String ccName, String ccNumber, BigDecimal limit, String errorMessage) {
		CreditCardModel model= new CreditCardModel();
		model.setNumber(ccNumber);
		model.setName(ccName);
		model.setLimit(limit);
		MessageModel message= new MessageModel();
		message.getErrors().add(errorMessage);
		Mockito.when(mockCreditCardValidatorServiceImpl.creditCardMandatoryCheck(model)).thenReturn(message);
		MessageModel actual = testObj.createCreditCard(model);
		Assertions.assertEquals(errorMessage, actual.getErrors().get(0));
	}
	
	@Test
	public void inValidCreditCardlengthNull() throws InvalidCreditCardException {
		InvalidCreditCardException thrown = Assertions.assertThrows(InvalidCreditCardException.class, () -> {
			CreditCardModel model = null;
			testObj.createCreditCard(model);
		});
		Assertions.assertEquals(CreditCardConstants.CARD_INVALID_NULL, thrown.getMessage());
	}
	
	@Test
	public void createValidCreditCard() {
		CreditCardModel model= new CreditCardModel();
		model.setNumber("6388498327914201");
		MessageModel message= new MessageModel();
		Mockito.when(mockCreditCardValidatorServiceImpl.creditCardMandatoryCheck(model)).thenReturn(message);
		Mockito.when(mockCreditCardValidatorServiceImpl.isValidCreditcard(model.getNumber())).thenReturn(true);
		MessageModel actual = testObj.createCreditCard(model);
		Assertions.assertEquals(CreditCardConstants.CARD_ADD_SUCCESS_MESSAGE, actual.getSuccess());
		verify(mockCreditCardRepository,times(1)).save(ArgumentMatchers.any(CreditCard.class));
	}
	
	@Test
	public void createCreditCardTFE() throws InvalidCreditCardException,TechnicalFailureException{
		CreditCardModel model= new CreditCardModel();
		model.setNumber("6388498327914200");
		MessageModel message= new MessageModel();
		Mockito.when(mockCreditCardValidatorServiceImpl.creditCardMandatoryCheck(model)).thenReturn(message);

		TechnicalFailureException thrown = Assertions.assertThrows(TechnicalFailureException.class, () -> {
			Mockito.when(mockCreditCardValidatorServiceImpl.isValidCreditcard(model.getNumber())).thenThrow(TechnicalFailureException.class);
			testObj.createCreditCard(model);
		});
		Assertions.assertEquals(CreditCardConstants.CARD_TECHNICAL_FAILURE, thrown.getMessage());
	}
	
	@Test
	public void createCreditCardExists() throws InvalidCreditCardException{
		CreditCardModel model= new CreditCardModel();
		model.setNumber("6388498327914201");
		MessageModel message= new MessageModel();
		Mockito.when(mockCreditCardValidatorServiceImpl.creditCardMandatoryCheck(model)).thenReturn(message);
		Mockito.when(mockCreditCardValidatorServiceImpl.isValidCreditcard(model.getNumber())).thenReturn(true);
		CreditCard creditCard= new CreditCard();
		creditCard.setId(10L);
		creditCard.setNumber("6388498327914201");
		Mockito.when(mockCreditCardRepository.getByNumber(model.getNumber())).thenReturn(creditCard);
		InvalidCreditCardException thrown = Assertions.assertThrows(InvalidCreditCardException.class, () -> {
			testObj.createCreditCard(model);
		});
		
		String expectedMessage = String.format(CreditCardConstants.CARD_EXISTS,model.getNumber());
		Assertions.assertEquals(expectedMessage, thrown.getMessage());
	}
	@Test
	public void getEmptyCardList(){
		List<CreditCard> creditCards = new ArrayList<>();
		Mockito.when(mockCreditCardRepository.findAll()).thenReturn(creditCards );
		Assertions.assertEquals(0, testObj.getAllCreditCards().size());
	}
	@Test
	public void getAllCreditCardsTest(){
		List<CreditCard> creditCards = new ArrayList<>();
		CreditCard card= new CreditCard();
		card.setId(11L);
		creditCards.add(card);
		Mockito.when(mockCreditCardRepository.findAll()).thenReturn(creditCards );
		Assertions.assertEquals(1, testObj.getAllCreditCards().size());
	}
	
}
