package xyz.belvi.luhn.interfaces;

public interface LuhnCardVerifier {
    void onCardVerified(boolean isSuccessFul, String errorTitle, String errorMessage);

    void requestOTP(int otpLength);

    void startProgress();

    /**
     * this is called within Luhn
     */
    void dismissProgress();

}