package xyz.belvi.addcard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
                    public void cardDetailsRetrieved(Context luhnContext, LuhnCard creditCard, final LuhnCardVerifier cardVerifier) {
                        cardVerifier.startProgress();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cardVerifier.requestOTP(4);
                            }
                        }, 2500);
                    }

                    @Override
                    public void otpRetrieved(Context luhnContext, final LuhnCardVerifier cardVerifier, String otp) {
                        cardVerifier.startProgress();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cardVerifier.onCardVerified(false, getString(R.string.verification_error), getString(R.string.verification_error));
                            }
                        }, 2500);
                    }

                    @Override
                    public void onFinished(boolean isVerfied) {
                        // luhn has finished. Do something
                    }
                }, R.style.LuhnStyle);
            }
        });
    }
}
