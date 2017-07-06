package xyz.belvi.luhn.customTextInputLayout;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by zone2 on 6/28/17.
 */

class CreditCardTransaformation extends PasswordTransformationMethod {

    private static CreditCardTransaformation sInstance;

    public static CreditCardTransaformation getInstance() {
        if (sInstance != null)
            return sInstance;

        sInstance = new CreditCardTransaformation();
        return sInstance;
    }

    @Override
    public CharSequence getTransformation(final CharSequence source, View view) {
        return source;
    }


}
