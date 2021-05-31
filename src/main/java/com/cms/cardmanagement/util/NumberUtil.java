package com.cms.cardmanagement.util;

import java.util.ArrayList;
import java.util.List;

public class NumberUtil {

	private NumberUtil() {
	}
	
	public static boolean isNumeric (String creditCardNumber) {
		return creditCardNumber.matches("^\\d+$");
		}
	
	 public static List<Integer> parseNumber(String creditCardNumber) {
	        List<Integer> creditCardNumbers = new ArrayList<>();
	        for (char number : creditCardNumber.toCharArray()) {
	            int tempNumber = Character.getNumericValue(number);
	            creditCardNumbers.add(tempNumber);
	        }
	        return creditCardNumbers;
	    }
}
