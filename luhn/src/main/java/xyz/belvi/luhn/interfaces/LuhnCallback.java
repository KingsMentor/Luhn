package xyz.belvi.luhn.interfaces;

import android.content.Context;

import xyz.belvi.luhn.cardValidator.models.LuhnCard;

/**
 * Created by zone2 on 7/6/17.
 */

public interface LuhnCallback {
    void cardDetailsRetrieved(Context luhnContext, LuhnCard creditCard, LuhnCardVerifier cardVerifier);

    void otpRetrieved(Context luhnContext, LuhnCardVerifier cardVerifier, String otp);

    void onFinished(boolean isVerified);
}
