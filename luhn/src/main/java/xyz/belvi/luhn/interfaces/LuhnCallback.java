package xyz.belvi.luhn.interfaces;

import xyz.belvi.luhn.cardValidator.models.LuhnCard;

/**
 * Created by zone2 on 7/6/17.
 */

public interface LuhnCallback {
    void cardDetailsRetrieved(LuhnCard creditCard, LuhnCardVerifier cardVerifier);

    void otpRetrieved(int otp, LuhnCardVerifier cardVerifier);
}
