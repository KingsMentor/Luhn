package xyz.belvi.luhn;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import xyz.belvi.luhn.cardValidator.models.LuhnCard;
import xyz.belvi.luhn.customTextInputLayout.inputLayouts.CardTextInputLayout;
import xyz.belvi.luhn.customTextInputLayout.inputLayouts.PinTextInputLayout;
import xyz.belvi.luhn.customTextInputLayout.textWatchers.CardNumberTextWatcher;
import xyz.belvi.luhn.customTextInputLayout.textWatchers.CvvTextWatcher;
import xyz.belvi.luhn.customTextInputLayout.textWatchers.ExpiringDateTextWatcher;
import xyz.belvi.luhn.customTextInputLayout.textWatchers.OTPTextWatcher;
import xyz.belvi.luhn.customTextInputLayout.textWatchers.PinTextWatcher;
import xyz.belvi.luhn.interfaces.LuhnCallback;
import xyz.belvi.luhn.interfaces.LuhnCardVerifier;
import xyz.belvi.luhn.screens.CardVerificationProgressScreen;

public final class Luhn extends BaseActivity implements LuhnCardVerifier {

    private LinearLayout llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private CardTextInputLayout cvvInputLayout, expiryInputLayout, cardNumber, otpInputLayout;
    private PinTextInputLayout pinInputLayout;
    private CardVerificationProgressScreen progressScreen;

    private int expMonth;
    private int cvv;
    private int expYear;
    private int pin;
    private int otp;
    private String cardPan, cardName;

    private boolean OTP_MODE;
    private boolean retrievePin;
    private final int CARDIO_REQUEST_ID = 555;

    private static final String STYLE_KEY = "xyz.belvi.Luhn.STYLE_KEY";
    private static LuhnCallback sLuhnCallback;

    public static void startLuhn(Context context, LuhnCallback luhnCallback) {
        sLuhnCallback = luhnCallback;
        context.startActivity(new Intent(context, Luhn.class));
    }

    public static void startLuhn(Context context, LuhnCallback luhnCallback, @StyleRes int style) {
        sLuhnCallback = luhnCallback;
        context.startActivity(new Intent(context, Luhn.class)
                .putExtra(STYLE_KEY, style)
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_add_card);
        initStyle(getIntent().getIntExtra(STYLE_KEY, R.style.LuhnStyle));
        attachKeyboardListeners(R.id.root_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        setButtonMargin(findViewById(R.id.btn_proceed), 0, 0, 0, 0);
    }

    @Override
    protected void onHideKeyboard() {
        setButtonMargin(findViewById(R.id.btn_proceed), 16, 16, 16, 16);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CARDIO_REQUEST_ID) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                cardNumber.getEditText().setText(scanResult.getFormattedCardNumber());
                if (scanResult.isExpiryValid()) {
                    String month = String.valueOf(scanResult.expiryMonth).length() == 1 ? "0" + scanResult.expiryMonth : String.valueOf(scanResult.expiryMonth);
                    String year = String.valueOf(scanResult.expiryYear).substring(2);
                    expiryInputLayout.getEditText().setText(month + "/" + year);
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    cvvInputLayout.getEditText().setText(scanResult.cvv);
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardVerified(boolean isSuccessFul, String errorTitle, String errorMessage) {
        dismissProgress();
        if (isSuccessFul) {
            finish();
        } else {
            showInfo(errorTitle, errorMessage, null, true);
        }
    }


    @Override
    public void requestOTP(int otpLength) {
        dismissProgress();
        disableAllFields();
        initOtp(otpLength);
        enableNextBtn();
        Toast.makeText(this, "Enter Otp", Toast.LENGTH_LONG).show();
    }


    private void includeCalligraphy(String font) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(font)
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initStyle(int style) {
        TypedArray ta = obtainStyledAttributes(style, R.styleable.luhnStyle);
        String fontName = ta.getString(R.styleable.luhnStyle_luhn_typeface);
        includeCalligraphy(fontName);
        initViews();
        retrievePin = ta.getBoolean(R.styleable.luhnStyle_luhn_show_pin, false);
        findViewById(R.id.btn_proceed).setBackground(ta.getDrawable(R.styleable.luhnStyle_luhn_btn_verify_selector));
        findViewById(R.id.toolbar).setBackgroundColor(ta.getColor(R.styleable.luhnStyle_luhn_show_toolbar_color, ContextCompat.getColor(this, R.color.colorAccent)));
    }

    private void initViews() {
        initCardField();
        initBottomSheet();
        initExpiryDateField();
        initCvvField();
        initPin();
        findViewById(R.id.btn_proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressScreen = new CardVerificationProgressScreen();
                progressScreen.show(getSupportFragmentManager(), "");
                if (sLuhnCallback != null)
                    if (OTP_MODE)
                        sLuhnCallback.otpRetrieved(otp, Luhn.this);
                    else
                        sLuhnCallback.cardDetailsRetrieved(new LuhnCard(cardPan, cardName, expMonth, expYear, cvv, pin), Luhn.this);
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
                    public void onValidated(boolean moveToNext, String cardNumberPan, String cardNameValue) {
                        cardPan = cardNumberPan;
                        cardName = cardNameValue;

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
                    protected void onValidated(boolean moveToNext, int expMonthValue, int expYearValue) {
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
                        if (moveToNext && retrievePin)
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
                pinInputLayout.setVisibility(retrievePin ? View.VISIBLE : View.GONE);
                pinInputLayout.getPasswordToggleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInfo(R.string.pin_header, R.string.pin_details, R.drawable.payment_bank_pin, false);

                    }
                });
                pinInputLayout.getEditText().addTextChangedListener(new PinTextWatcher(pinInputLayout) {
                    @Override
                    public void onValidated(boolean moveToNext, int otpValue) {
                        otp = otpValue;
                        enableNextBtn();
                    }
                });
            }
        });

    }

    private void initOtp(final int otpLength) {
        OTP_MODE = true;
        findViewById(R.id.ctil_otp_layout).setVisibility(View.VISIBLE);
        otpInputLayout = (CardTextInputLayout) findViewById(R.id.ctil_otp_input);
        otpInputLayout.post(new Runnable() {
            @Override
            public void run() {
                otpInputLayout.requestFocus();
                otpInputLayout.getPasswordToggleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInfo(R.string.otp_header, R.string.otp_details, R.drawable.payment_bank_pin, false);

                    }
                });
                otpInputLayout.getEditText().addTextChangedListener(new OTPTextWatcher(otpInputLayout, otpLength) {
                    @Override
                    public void onValidated(boolean moveToNext, int pinValue) {
                        pin = pinValue;
                        enableNextBtn();
                    }
                });
            }
        });

    }

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, CARDIO_REQUEST_ID);
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


    private void setButtonMargin(View view, int left, int top, int right, int bottom) {
        android.support.v7.widget.LinearLayoutCompat.LayoutParams params = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) view.getLayoutParams();
        params.setMargins(left, top, right, bottom); //left, top, right, bottom
        view.setLayoutParams(params);
    }

    void enableNextBtn() {
        if (OTP_MODE)
            findViewById(R.id.btn_proceed).setEnabled(otpInputLayout.hasValidInput());
        else if (retrievePin)
            findViewById(R.id.btn_proceed).setEnabled(cvvInputLayout.hasValidInput() && expiryInputLayout.hasValidInput() && cardNumber.hasValidInput() && pinInputLayout.hasValidInput());
        else
            findViewById(R.id.btn_proceed).setEnabled(cvvInputLayout.hasValidInput() && expiryInputLayout.hasValidInput() && cardNumber.hasValidInput());
    }


    private void dismissProgress() {
        progressScreen.dismissAllowingStateLoss();
    }


    private void disableAllFields() {
        TextInputLayout allFields[] = {pinInputLayout, cvvInputLayout, cardNumber, expiryInputLayout};
        for (TextInputLayout field : allFields) {
            field.setEnabled(false);
            field.setErrorEnabled(false);
        }
        Arrays.fill(allFields, null);

    }
}
