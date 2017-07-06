package xyz.belvi.luhn.interfaces;

public interface LuhnCardVerifier {
    void onCardVerified(boolean isSuccessFul, String errorTitle, String errorMessage);
}