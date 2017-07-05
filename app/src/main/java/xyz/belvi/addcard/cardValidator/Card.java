package xyz.belvi.addcard.cardValidator;

import java.util.ArrayList;

/**
 * Created by zone2 on 7/4/17.
 */

public class Card {
    ArrayList<Integer> possibleLengths;
    int drawable;

    public Card(ArrayList<Integer> possibleLengths, int drawable) {
        this.possibleLengths = possibleLengths;
        this.drawable = drawable;
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
