package xyz.belvi.addcard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import xyz.belvi.addcard.cardValidator.LuhnCard;
import xyz.belvi.addcard.customTextInputLayout.CardNumberTextWatcher;
import xyz.belvi.addcard.customTextInputLayout.CardTextInputLayout;
import xyz.belvi.addcard.customTextInputLayout.CvvTextWatcher;
import xyz.belvi.addcard.customTextInputLayout.ExpiringDateTextWatcher;
import xyz.belvi.addcard.customTextInputLayout.PinTextInputLayout;
import xyz.belvi.addcard.customTextInputLayout.PinTextWatcher;

public class Luhn extends BaseActivity {

    private LinearLayout llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private final int CARDIO_REQUEST_ID = 555;
    private CardVerificationProgressScreen progressScreen;
    private static LuhnCardVerifier mLuhnCardVerifier;

    private int expMonth;
    private int cvv;
    private int expYear;
    private int pin;
    private String cardPan;

    public interface LuhnCardVerifier {
        void onCardVerified(LuhnCard creditCard);
    }

    public static void startLuhn(Context context, LuhnCardVerifier cardVerifier) {
        mLuhnCardVerifier = cardVerifier;
        context.startActivity(new Intent(context, Luhn.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        attachKeyboardListeners(R.id.root_layout);
        setSupportActionBar(toolbar);
        initCardField();
        initBottomSheet();
        initExpiryDateField();
        initCvvField();
        initPin();
        findViewById(R.id.btn_proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCallBackListener();
                progressScreen = new CardVerificationProgressScreen();
                progressScreen.show(getSupportFragmentManager(), "");
                mLuhnCardVerifier.onCardVerified(new LuhnCard(cardPan, expMonth, expYear, cvv, pin));
            }
        });
    }

    private void initBottomSheet() {
        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        llBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        findViewById(R.id.ok_dimiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(v);
            }
        });
    }

    private CardTextInputLayout cvvInputLayout, expiryInputLayout, cardNumber;
    private PinTextInputLayout pinInputLayout;

    private void initCardField() {
        cardNumber = (CardTextInputLayout) findViewById(R.id.cti_card_number_input);
        cardNumber.post(new Runnable() {
            @Override
            public void run() {
                cardNumber.getPasswordToggleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!cardNumber.getEditText().getText().toString().isEmpty()) {
                            cardNumber.getEditText().setText("");
                        } else {
                            onScanPress(v);
                        }
                    }
                });
                cardNumber.getEditText().addTextChangedListener(new CardNumberTextWatcher(cardNumber) {
                    @Override
                    public void onValidated(boolean moveToNext, String cardNumberPan) {
                        cardPan = cardNumberPan;
                        if (moveToNext) {
                            findViewById(R.id.tiet_exp_input).requestFocus();
                        }
                        enableNextBtn();
                    }
                });
            }
        });
    }


    private void initExpiryDateField() {

        expiryInputLayout = (CardTextInputLayout) findViewById(R.id.ctil_expiry_input);

        expiryInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    expiryInputLayout.setHint("Exp. Date");
                } else {
                    if (expiryInputLayout.getEditText().getText().toString().isEmpty())
                        expiryInputLayout.setHint("MM/YY");
                    else {
                        if (ExpiringDateTextWatcher.isValidExpiringDate(expiryInputLayout.getEditText().getText().toString())) {
                            expiryInputLayout.setError("");
                        } else {
                            expiryInputLayout.setError("Enter a valid expiration date");
                        }
                        expiryInputLayout.setHint("Exp. Date");
                    }
                }
            }
        });

        expiryInputLayout.post(new Runnable() {
            @Override
            public void run() {
                expiryInputLayout.getPasswordToggleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInfo(R.string.exp_header, R.string.exp_details, R.drawable.payment_bank_card_expiration_info, false);

                    }
                });
                expiryInputLayout.getEditText().addTextChangedListener(new ExpiringDateTextWatcher(expiryInputLayout) {
                    @Override
                    public void onValidated(boolean moveToNext, int expMonthValue, int expYearValue) {
                        expMonth = expMonthValue;
                        expYear = expYearValue;
                        if (moveToNext) {
                            findViewById(R.id.tiet_cvv_input).requestFocus();
                            expiryInputLayout.setError("");
                        }
                        enableNextBtn();
                    }
                });
            }

        });

    }


    private void initCvvField() {
        cvvInputLayout = (CardTextInputLayout) findViewById(R.id.ctil_cvv_input);

        cvvInputLayout.post(new Runnable() {
            @Override
            public void run() {
                cvvInputLayout.getPasswordToggleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInfo(R.string.cvv_header, R.string.cvv_desc, R.drawable.payment_bank_card_cvv_info, false);

                    }
                });
                cvvInputLayout.getEditText().addTextChangedListener(new CvvTextWatcher(cvvInputLayout) {
                    @Override
                    public void onValidated(boolean moveToNext, int cvvValue) {
                        cvv = cvvValue;
                        if (moveToNext)
                            findViewById(R.id.tiet_pin_input).requestFocus();
                        enableNextBtn();
                    }
                });
            }
        });

    }

    private void initPin() {
        pinInputLayout = (PinTextInputLayout) findViewById(R.id.ctil_pin_input);
        pinInputLayout.post(new Runnable() {
            @Override
            public void run() {
                pinInputLayout.getPasswordToggleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInfo(R.string.pin_header, R.string.pin_details, R.drawable.payment_bank_pin, false);

                    }
                });
                pinInputLayout.getEditText().addTextChangedListener(new PinTextWatcher(pinInputLayout) {
                    @Override
                    public void onValidated(boolean moveToNext, int pinValue) {
                        pin = pinValue;
                        enableNextBtn();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, CARDIO_REQUEST_ID);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void showInfo(@StringRes int header, @StringRes int desc, @DrawableRes int drawable, boolean error) {
        showInfo(getString(header), getString(desc), ContextCompat.getDrawable(this, drawable), false);
    }

    private void showInfo(String header, String desc, @Nullable Drawable drawable, boolean error) {
        hideKeyboard();
        AppCompatTextView infoHeader = (AppCompatTextView) llBottomSheet.findViewById(R.id.info_header);
        AppCompatTextView infoDesc = (AppCompatTextView) llBottomSheet.findViewById(R.id.info_desc);
        AppCompatImageView infoImg = (AppCompatImageView) llBottomSheet.findViewById(R.id.info_img);
        if (error) {
            llBottomSheet.findViewById(R.id.info_img).setVisibility(View.GONE);
            ((AppCompatButton) findViewById(R.id.ok_dimiss)).setText("Close");
        } else {
            llBottomSheet.findViewById(R.id.info_img).setVisibility(View.VISIBLE);
            ((AppCompatButton) findViewById(R.id.ok_dimiss)).setText("Ok");
        }

        infoHeader.setText(header);
        infoDesc.setText(desc);
        if (drawable != null)
            infoImg.setImageDrawable(drawable);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void dismiss(View v) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    protected void onShowKeyboard(int keyboardHeight) {
//        findViewById(R.id.btn_proceed).setVisibility(View.INVISIBLE);
        setButtonMargin(findViewById(R.id.btn_proceed), 0, 0, 0, 0);
    }

    @Override
    protected void onHideKeyboard() {
//        findViewById(R.id.btn_proceed).setVisibility(View.VISIBLE);
        setButtonMargin(findViewById(R.id.btn_proceed), 16, 16, 16, 16);
    }

    private void setButtonMargin(View view, int left, int top, int right, int bottom) {
        android.support.v7.widget.LinearLayoutCompat.LayoutParams params = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) view.getLayoutParams();
        params.setMargins(left, top, right, bottom); //left, top, right, bottom
        view.setLayoutParams(params);
    }

    void enableNextBtn() {
        findViewById(R.id.btn_proceed).setEnabled(cvvInputLayout.hasValidInput() && expiryInputLayout.hasValidInput() && cardNumber.hasValidInput() && pinInputLayout.hasValidInput());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CARDIO_REQUEST_ID) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                cardNumber.getEditText().setText(scanResult.getRedactedCardNumber());


                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    expiryInputLayout.getEditText().setText(scanResult.expiryMonth + "/" + scanResult.expiryYear);
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    cvvInputLayout.getEditText().setText(scanResult.cvv);
                }

            }
        }
    }

    private void registerCallBackListener() {
        LocalBroadcastManager.getInstance(this).registerReceiver(callBackReceiver, new IntentFilter(CallbackFilter));
    }


    private static final String CallbackFilter = "xyz.belvi.Luhn.callBackReceiver";
    private static final String CallbackDataStatus = "xyz.belvi.Luhn.DataStatus";
    private static final String CallbackFilterDataErrorTitle = "xyz.belvi.Luhn.DataErrorTitle";
    private static final String CallbackFilterDataErrorMessage = "xyz.belvi.Luhn.DataErrorMessage";
    private BroadcastReceiver callBackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(CallbackDataStatus, false)) {
                finish();
            } else {
                String title = intent.getStringExtra(CallbackFilterDataErrorTitle);
                String message = intent.getStringExtra(CallbackFilterDataErrorMessage);
                showInfo(title, message, null, false);
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }
    };

    public static void verificationStatus(Context context, boolean isSuccessFul, String errorTitle, String errorMessage) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(
                new Intent(CallbackFilter)
                        .putExtra(CallbackDataStatus, isSuccessFul)
                        .putExtra(CallbackFilterDataErrorTitle, errorTitle)
                        .putExtra(CallbackFilterDataErrorMessage, errorMessage)
        );
    }
}
