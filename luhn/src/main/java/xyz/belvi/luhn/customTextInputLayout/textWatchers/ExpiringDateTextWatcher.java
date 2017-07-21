package xyz.belvi.luhn.customTextInputLayout.textWatchers;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.Calendar;

import xyz.belvi.luhn.customTextInputLayout.inputLayouts.CardTextInputLayout;

/**
 * Created by zone2 on 7/3/17.
 */

public abstract class ExpiringDateTextWatcher implements TextWatcher {
    private CardTextInputLayout mCardTextInputLayout;

    public ExpiringDateTextWatcher(CardTextInputLayout cardTextInputLayout) {
        this.mCardTextInputLayout = cardTextInputLayout;
        currentText = mCardTextInputLayout.getEditText().getText().toString();
    }

    private String currentText = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence source, int start, int before, int count) {
        if (!source.toString().equals(currentText)) {
            currentText = getExpDate(source.toString());
            mCardTextInputLayout.getEditText().setText(currentText);
            mCardTextInputLayout.setHasValidInput(false);
            mCardTextInputLayout.getEditText().setSelection(currentText.length());
        }

        boolean moveToNext = false;
        if (source.length() == 5 && isValidExpiringDate(source.toString())) {
            moveToNext = true;
        }

        mCardTextInputLayout.setHasValidInput(moveToNext);
        onValidated(moveToNext, mCardTextInputLayout.getEditText().getText().toString(), expMonth, expYear);


        try {
            mCardTextInputLayout.passwordVisibilityToggleRequested();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getExpDate(String source) {
        mCardTextInputLayout.setError("");
        if (currentText.length() > source.length()) {
            // backspace is pressed on keyboard
            if (currentText.endsWith("/")) {
                return source.substring(0, source.length() - 1);
            }
        } else {
            if (source.length() == 1) {
                if (Integer.parseInt(source) > 1) {
                    return "0" + source + "/";
                }

            } else if (source.length() == 2) {
                if (source.startsWith("0")) {
                    return source.endsWith("0") ? "0" : source + "/";
                } else if (source.startsWith("1")) {
                    if (Integer.parseInt(source.substring(1)) > 2) {
                        return "1";
                    }
                    return source + "/";
                } else {
                    return source.substring(0, 1);
                }
            }
        }
        return source;
    }

    static int expMonth, expYear;

    public static boolean isValidExpiringDate(String expDate) {
        try {
            if (expDate.length() != 5)
                return false;
            Calendar now = Calendar.getInstance();
            int month = now.get(Calendar.MONTH);
            int year = Integer.parseInt(String.valueOf(now.get(Calendar.YEAR)).substring(2));

            String expMonthDate[] = expDate.split("/");
            if (expMonthDate[0].length() != 2)
                return false;
            expMonth = Integer.parseInt(expMonthDate[0]);
            expYear = Integer.parseInt(expMonthDate[1]);
            return year < expYear ? true : year == expYear ? month < expMonth : false;
        } catch (Exception e) {
            return false;
        }

    }

    protected abstract void onValidated(boolean moveToNext, String expDate, int month, int year);
}
