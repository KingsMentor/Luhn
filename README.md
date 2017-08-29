# Luhn
Smooth UI for Credit Card Entry on Android device, perform check for supported credit card types , pan length and luhn check. Inspired by Uber credit card entry interface



Current Verion [![](https://jitpack.io/v/KingsMentor/Luhn.svg)](https://jitpack.io/#KingsMentor/Luhn) introduces:
* Luhn Verification Algorithm
* Smooth UI
* Info about card details
* Credit Card Type Prediction 
* CardIO Integration
* Styling with Attributes
* Prevent Screen Capture on Add Card Screen

### Change List

#### v1.0.1 
- OTP (One time password) verification
- OTP Length Bug fix

#### v1.0.2
- Fix bug of installing library as a seperate module
- gradle dependency build optimisation

#### v2.0.0
- Customising CardIo in Luhn
- Interswitch Verve Card Image
- Theming Luhn from .xml resources
- custom dialog integration (you can now use your app loading screen instead of Luhn's progress screen. Just don't call `cardVerifier.startProgress();`. Call your progress implementation instead)
- onFinished Callback. Get call back when luhn has finished, so you can `doSomething();`.

#### v2.0.1
- Pin and OTP Mismatch bug fixed
- Expiry date string included
- Proper datatype for pin, cvv and otp
- Proper naming for `American Express`

![Lib Sample](https://github.com/KingsMentor/Luhn/blob/master/screenshots/add_card_collage.jpg)


#### Demo

See demo by downloading [Demo Apk](https://github.com/KingsMentor/Luhn/blob/master/apk/app-debug.apk)

#### adding as a dependency

##### Step 1. 
Add the JitPack repository to your build file in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
##### Step 2. 
Add the dependency

	dependencies {
	        compile 'com.github.KingsMentor:Luhn:v2.1.3'
	}

You can also use  `master-SNAPSHOT` instead. This always contains stable builds (with new fixes) prior to a new release. 
To use, do this instead

```xml
	dependencies {
	        compile 'com.github.KingsMentor:Luhn:master-SNAPSHOT'
	}
```

#### Supported Attributes
```
        <attr name="luhn_show_pin" format="boolean" />
        <attr name="luhn_title" format="string" />
        <attr name="luhn_show_toolbar_color" format="color" />
        <attr name="luhn_btn_verify_selector" format="reference" />
        <attr name="luhn_typeface" format="string" />
```
#### Cards Currently Supported 

```java 

     /**
     * @VisaElectron is listed before
     * @Visa for proper card prediction (both start with 4 but
     * @VisaElectron has to start with any of 4026, 417500, 4508, 4844, 4913 or 4917)
     *
     */
    AmericanExpress("American Express", R.drawable.payment_ic_amex, "34, 37", "15"),
    CardGuard("Card Guard", R.drawable.payment_ic_method, "5392", "16"),
    ChinaUnionPay("China Union Pay", R.drawable.payment_ic_unionpay, "62", "16-19"),
    Dankort("Dankort", R.drawable.payment_ic_method, "5019", "16"),
    DinersClub("Diners Club", R.drawable.payment_ic_dinersclub, "300-305, 309, 36, 38, 39", "14,16-19"),
    Discover("Discover", R.drawable.payment_ic_discover, "6011, 622126 to 622925, 644, 645, 646, 647, 648, 649, 65", "16,19"),
    InstaPayment("Insta Payment", R.drawable.payment_ic_method, "637, 638, 639", "16"),
    JCB("JCB", R.drawable.payment_ic_method, "3528-3589", "16-19"),
    Maestro("Maestro", R.drawable.payment_ic_maestro_card, "5018, 5020, 5038, 5893, 6304, 6759, 6761, 6762, 6763", "12-19"),
    MasterCard("Master", R.drawable.payment_ic_master_card, "51, 52, 53, 54, 55, 222100-272099", "16"),
    MIR("Mir", R.drawable.payment_ic_method, "2200 - 2204", "16"),
    Troy("Troy", R.drawable.payment_ic_method, "979200-979289", "16"),
    UATP("Universal Air Travel Plan", R.drawable.payment_ic_method, "1", "15"),
    Verve("Verve", R.drawable.payment_ic_verve, "506099-506198, 650002-650027", "16,19"),
    VisaElectron("Visa Electron", R.drawable.payment_ic_method, "4026, 417500, 4508, 4844, 4913, 4917", "16"),
    Visa("Visa", R.drawable.payment_ic_visa, "4", "13,16,19");


    CardEnum(String cardName, @DrawableRes int icon, String startWith, String length) {
        this.cardName = cardName; // proper name of the card type e.g master card, visa card
        this.icon = icon; // brand icon of the card type
        this.startWith = startWith; // determine how to predict card by digit the pan starts with
        this.length = length; // possible length of the card pan
    }
    
```
### Using Luhn.
Using Luhn can be as simple as these few lines of code.

```java
Luhn.startLuhn(this, new LuhnCallback() {
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
                    public void onFinished(boolean isVerified) {
                        // luhn has finished. Do something
                    }
                }, R.style.LuhnStyle);
```

#### Customising CardIo in Luhn 

CardIO Scan Screen can also be customise to suit your app requirement. All you have to do is build a `bundle` with CardIO customise settings and pass it when starting Luhn like so :

```
		Bundle cardIoBundle = new Bundle();
                cardIoBundle.putBoolean(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                cardIoBundle.putBoolean(CardIOActivity.EXTRA_SCAN_EXPIRY, true); // default: false
                cardIoBundle.putBoolean(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
                cardIoBundle.putBoolean(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                cardIoBundle.putBoolean(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false
                cardIoBundle.putBoolean(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false

                Luhn.startLuhn(this, new LuhnCallback() {
                    @Override
                    public void cardDetailsRetrieved(Context luhnContext, LuhnCard creditCard, LuhnCardVerifier cardVerifier) {
                        // do something
                    }

                    @Override
                    public void otpRetrieved(Context luhnContext, LuhnCardVerifier cardVerifier, String otp) {
                        // do something
                    }

                    @Override
                    public void onFinished(boolean isVerified) {
                        // do something
                    }
                }, cardIoBundle);
```

#### Styling 

in `styles.xml` Luhn activity can be style. IF no style is provided, then a default styling is used.
```xml
    <style name="LuhnStyle">
        <item name="luhn_title">Add Card</item>
        <item name="luhn_typeface">fonts/ClanMedium.ttf</item>
        <item name="luhn_btn_verify_selector">@drawable/card_next_btn_selector</item>
        <item name="luhn_show_toolbar_color">@color/colorAccent</item>
        <item name="luhn_show_pin">true</item>
    </style>
```
#### Customising CardIO

CardIo integration can be customised by calling :
```
/**
     * @param context
     * @param luhnCallback - callback for Luhn
     * @param cardIOBundle - cardIO settings for card scan
     */
    public static void startLuhn(Context context, LuhnCallback luhnCallback, Bundle cardIOBundle) {
        sLuhnCallback = luhnCallback;
        context.startActivity(new Intent(context, Luhn.class)
                .putExtra(CARD_IO, cardIOBundle)
        );
    }
```

#### Theming Luhn

colors, strings, dimens of views and components in Luhn is controlled from resources. To theme Luhn to suit and blend with your app, you have to overwrite any of these:

##### in colors.xml

 ```   

    <color name="ln_colorPrimary">#ff000000</color>
    <color name="ln_colorPrimaryDark">#303F9F</color>
    <color name="ln_colorAccent">#ff000000</color>

    <color name="ln_colorControlHighlight">#ff000000</color>
    <color name="ln_info_bg">#ff000000</color>
    <color name="ln_colorControlActivated">#ff151525</color>
    <color name="ln_colorHint">#aaaaaa</color>
    <color name="ln_colorError">#ffdd0031</color>
    <color name="ln_colorProgress">#ffffff</color>
    <color name="ln_colorInfoHeader">#ff151525</color>
    <color name="ln_colorInfoDesc">#ff6b6b76</color>
    <color name="ln_colorWhite">#ffffff</color>
    <color name="ln_actionMenuTextColor">#ffffff</color>
    <color name="ln_verify_btn_enabled">#ff000000</color>
    <color name="ln_verify_btn_pressed">#ff3a3a48</color>
    <color name="ln_verify_btn_disabled">#ffa4a4ac</color>

    
```
##### in dimens.xml

```
    <dimen name="ln_input_size">16sp</dimen>
    <dimen name="ln_toolbar_title_size">18sp</dimen>
    <dimen name="ln_progress_txt_size">18sp</dimen>
```
##### in strings.xml

```
    <string name="progress_text">Saving Card</string>
```

#### Credits

* <a href="https://github.com/card-io/card.io-Android-SDK" target="_blank">Card.io Android SDK</a>



#### Contributions 

Contributions are welcome. Generally, contributions are managed by issues and pull requests.

# License

The MIT License (MIT). Please see the [License File](https://github.com/KingsMentor/Luhn/blob/master/LICENSE) for more information.
