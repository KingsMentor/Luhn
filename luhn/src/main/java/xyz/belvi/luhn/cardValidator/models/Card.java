package xyz.belvi.luhn.cardValidator.models;

import java.util.ArrayList;

/**
 * Created by zone2 on 7/4/17.
 */

public final class Card {
    ArrayList<Integer> possibleLengths;
    String cardName;
    int drawable;

    public Card(String cardName, ArrayList<Integer> possibleLengths, int drawable) {
        this.cardName = cardName;
        this.possibleLengths = possibleLengths;
        this.drawable = drawable;
    }

    public String getCardName() {
        return this.cardName;
    }

    public int getMaxLength() {
        return possibleLengths.get(possibleLengths.size() - 1);
    }

    public ArrayList<Integer> getPossibleLengths() {
        return this.possibleLengths;
    }

    public int getDrawable() {
        return this.drawable;
    }
}
