package xyz.belvi.luhn.cardValidator;

import java.util.ArrayList;
import java.util.Arrays;

import xyz.belvi.luhn.cardValidator.models.Card;

/**
 * Created by zone2 on 6/28/17.
 */

public final class CardValidator {

    private String cardNumber;
    private int checkDigit;

    public CardValidator(String cardNumber) {
        this.cardNumber = cardNumber.replace(" ", "");
    }

    public boolean isValidCardNumber() {
        if (cardNumber.trim().isEmpty())
            return false;
        dropLastNumber();
        return (addAllNumber() + checkDigit) % 10 == 0;
    }

    /*
    the last number is used as a check digit
     */
    private void dropLastNumber() {
        checkDigit = Integer.parseInt(cardNumber.substring(cardNumber.length() - 1));
        this.cardNumber = cardNumber.substring(0, cardNumber.length() - 1);
    }


    private int addAllNumber() {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            if (cardNumber.length() % 2 != 0)
                sum += multiplyOddByTwo(i + 1, Integer.parseInt(String.valueOf(cardNumber.charAt(i))));
            else
                sum += multiplyOddByTwo(i, Integer.parseInt(String.valueOf(cardNumber.charAt(i))));
        }
        return sum;
    }

    private int multiplyOddByTwo(int i, int digit) {
        return (i % 2) == 0 ? digit : subtractNine(digit * 2);
    }

    private int subtractNine(int digit) {
        return digit > 9 ? digit - 9 : digit;
    }

    public Card guessCard() {
        for (CardEnum cardEnum : CardEnum.values()) {
            ArrayList<String> startWiths = new ArrayList<>(Arrays.asList(cardEnum.getStartWith().split(",")));
            for (String cardStartIndex : startWiths) {
                cardStartIndex = cardStartIndex.trim();
                if (cardStartIndex.contains("-")) {
                    String range[] = cardStartIndex.split("-");
                    long start = Long.parseLong(range[0].trim());
                    long end = Long.parseLong(range[1].trim());
                    if (String.valueOf(start).length() <= cardNumber.length()) {
                        String cardNumberSub = cardNumber.substring(0, String.valueOf(start).length());
                        if (Long.parseLong(cardNumberSub) >= start && Long.parseLong(cardNumberSub) <= end) {
                            return new Card(cardEnum.getCardName(), fetchPossibleLength(cardEnum), cardEnum.getIcon());
                        }
                    }
                } else {
                    if (cardNumber.startsWith(cardStartIndex)) {
                        return new Card(cardEnum.getCardName(), fetchPossibleLength(cardEnum), cardEnum.getIcon());
                    }
                }
            }
        }

        return null;
    }

    private ArrayList<Integer> fetchPossibleLength(CardEnum cardEnum) {
        ArrayList<String> lengths = new ArrayList<>(Arrays.asList(cardEnum.getLength().split(",")));
        ArrayList<Integer> possibleLengths = new ArrayList<>();
        for (String length : lengths) {
            if (length.contains("-")) {
                String range[] = length.split("-");
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                for (int index = start; index <= end; index++) {
                    possibleLengths.add(index);
                }
            } else {
                possibleLengths.add(Integer.parseInt(length.trim()));
            }
        }
        return possibleLengths;
    }

}
