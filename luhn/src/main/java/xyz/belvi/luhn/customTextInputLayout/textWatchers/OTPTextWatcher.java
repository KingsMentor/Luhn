package xyz.belvi.luhn.customTextInputLayout.textWatchers;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;

import xyz.belvi.luhn.customTextInputLayout.inputLayouts.CardTextInputLayout;

/**
 * Created by zone2 on 7/3/17.
 */

public abstract class OTPTextWatcher implements TextWatcher {
    private CardTextInputLayout mCardTextInputLayout;

    int length = 0;

    public OTPTextWatcher(CardTextInputLayout cardTextInputLayout, int length) {
        this.length = length;
        this.mCardTextInputLayout = cardTextInputLayout;
        mCardTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mCardTextInputLayout.hasValidInput() || mCardTextInputLayout.getEditText().getText().toString().isEmpty()) {
                    mCardTextInputLayout.setError("");

                } else {
                    mCardTextInputLayout.setError("Enter a valid otp code");
                }
            }
        });
        setMaxLength(length);
    }

    private void setMaxLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        mCardTextInputLayout.getEditText().setFilters(FilterArray);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence source, int start, int before, int count) {
        mCardTextInputLayout.setError("");
        boolean moveToNext = isValid(source);
        try {
            mCardTextInputLayout.passwordVisibilityToggleRequested();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mCardTextInputLayout.setHasValidInput(moveToNext);
        String text = mCardTextInputLayout.getEditText().getText().toString();
        onValidated(moveToNext, text);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean isValid(CharSequence source) {
        return source.toString().length() == length;
    }

    protected abstract void onValidated(boolean moveToNext, String pin);
}
