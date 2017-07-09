package xyz.belvi.addcard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xyz.belvi.luhn.Luhn;
import xyz.belvi.luhn.cardValidator.models.LuhnCard;
import xyz.belvi.luhn.interfaces.LuhnCallback;
import xyz.belvi.luhn.interfaces.LuhnCardVerifier;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Luhn.startLuhn(MainActivity.this, new LuhnCallback() {
                    @Override
                    public void cardDetailsRetrieved(LuhnCard creditCard, final LuhnCardVerifier cardVerifier) {
                        /**
                         * Request for Otp with:
                         *
                         * cardVerifier.requestOTP(4);
                         *
                         * or
                         * do something with card details and return a callback to luhn using
                         *
                         * cardVerifier.onCardVerified(false,"error occured","error message");
                         *
                         *cardVerifier.onCardVerified(true, "", "");
                         */

                        cardVerifier.requestOTP(5);

                    }

                    @Override
                    public void otpRetrieved(int otp, final LuhnCardVerifier cardVerifier) {
                        /**
                         * otp is retrieved.
                         *
                         * do something with otp and return callback to Luhn
                         */
                        cardVerifier.onCardVerified(false,getString(R.string.verification_error),getString(R.string.verification_details));
                    }
                }, R.style.LuhnStyle);
            }
        });
    }
}
