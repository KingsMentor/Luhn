package xyz.belvi.luhn.customTextInputLayout.textWatchers;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;

import xyz.belvi.luhn.R;
import xyz.belvi.luhn.cardValidator.CardValidator;
import xyz.belvi.luhn.cardValidator.models.Card;
import xyz.belvi.luhn.customTextInputLayout.inputLayouts.CardTextInputLayout;


/**
 * Created by zone2 on 7/3/17.
 */

public abstract class CardNumberTextWatcher implements TextWatcher {
    private CardTextInputLayout mCardTextInputLayout;

    public CardNumberTextWatcher(CardTextInputLayout cardTextInputLayout) {
        this.mCardTextInputLayout = cardTextInputLayout;
        currentText = mCardTextInputLayout.getEditText().getText().toString();
        mCardTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!currentText.isEmpty()) {
                        if (mCardTextInputLayout.hasValidInput()) {
                            mCardTextInputLayout.setError("");
                        } else {
                            mCardTextInputLayout.setError("Enter a valid credit card number");
                        }
                    }
                }
            }
        });
    }

    private String currentText = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence source, int start, int before, int count) {
        Card card = setCardIcon(source.toString());
        mCardTextInputLayout.setError("");
        if (!source.toString().equals(currentText)) {
            source = source.toString().replace(" ", "");
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < source.length(); i++) {
                if (i % 4 == 0 && i != 0) {
                    result.append(" ");
                }

                result.append(source.charAt(i));
            }
            currentText = result.toString();
            mCardTextInputLayout.getEditText().setText(result.toString());
            mCardTextInputLayout.getEditText().setSelection(currentText.length());
        }
        try {
            mCardTextInputLayout.passwordVisibilityToggleRequested();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        boolean moveToNext = false;
        if (card == null) {
            moveToNext = false;
        } else if (card.getPossibleLengths().contains(currentText.replace(" ", "").length())) {
            moveToNext = new CardValidator(currentText).isValidCardNumber();
        } else {
            moveToNext = false;
        }
        mCardTextInputLayout.setHasValidInput(moveToNext);
        onValidated(moveToNext, currentText, card == null ? "" : card.getCardName());

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private Card setCardIcon(String source) {
        Card card = new CardValidator(source).guessCard();

        InputFilter[] FilterArray = new InputFilter[1];
        if (card != null) {
            int maxLength = Integer.parseInt(String.valueOf(card.getMaxLength()));
            FilterArray[0] = new InputFilter.LengthFilter(getSpacedPanLength(maxLength));
            mCardTextInputLayout.getEditText().setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(mCardTextInputLayout.getContext(), card.getDrawable()), null, null, null);
        } else {
            FilterArray[0] = new InputFilter.LengthFilter(getSpacedPanLength(19));
            mCardTextInputLayout.getEditText().setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(mCardTextInputLayout.getContext(), R.drawable.payment_method_generic_card), null, null, null);
        }

        mCardTextInputLayout.getEditText().setFilters(FilterArray);

        return card;
    }

    private int getSpacedPanLength(int length) {
        return (length % 4 == 0) ? length + (length / 4) - 1 : length + (length / 4);
    }

    protected abstract void onValidated(boolean moveToNext, String cardPan, String cardName);


}
