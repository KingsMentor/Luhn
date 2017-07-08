package xyz.belvi.luhn.customTextInputLayout.transformations;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by zone2 on 6/28/17.
 */

public class CreditCardTransformation extends PasswordTransformationMethod {

    private static CreditCardTransformation sInstance;

    public static CreditCardTransformation getInstance() {
        if (sInstance != null)
            return sInstance;

        sInstance = new CreditCardTransformation();
        return sInstance;
    }

    @Override
    public CharSequence getTransformation(final CharSequence source, View view) {
        return source;
    }


}
