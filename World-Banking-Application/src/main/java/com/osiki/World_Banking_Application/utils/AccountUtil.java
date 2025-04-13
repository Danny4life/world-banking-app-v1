package com.osiki.World_Banking_Application.utils;

import java.time.Year;

public class AccountUtil {
    public static final String ACCOUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";

    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account has been created successfully!";


    public static final String ACCOUNT_NUMBER_NON_EXISTS_CODE = "003";
    public static final String ACCOUNT_NUMBER_NON_EXISTS_MESSAGE = "Provided account number does not exists.";

    public static final String ACCOUNT_NUMBER_FOUND_CODE = "004";
    public static final String ACCOUNT_NUMBER_FOUND_MESSAGE = "Account number found";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Account credited successfully";

    public static final String ACCOUNT_BALANCE_CODE = "006";

    public static final String ACCOUNT_BALANCE_MESSAGE = "Insufficient Balance";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";

    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account has been debited successfully";

    public static final String TRANSFER_SUCCESSFUL_CODE = "008";

    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful";







    // Algorithm to automatically generate bank account number
    public static String generateAccountNumber(){
        /**
         * This algorithm will assume that your country account number will be a total of 10digits
         * To implement this we are going to concatenate the current year + any six random digits
         * 2025 + 123456 = 2025123456
         */

        // to get the current year -- this give us the first 4 digits
        Year currentYear = Year.now();

        // this will give us the next 6 digits
        int min = 100000;
        int max = 999999;

        // generate a random number between min and max
        int randomNumber = (int)Math.floor(Math.random() * (max - min + 1) + min);

        // convert current year and random number to string and then concatenate them
        String year = String.valueOf(currentYear);
        String randomNum = String.valueOf(randomNumber);

        // append both the current year and the random number to generate the 10 digits account number
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNum).toString();

    }
}
