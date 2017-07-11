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
	        compile 'com.github.KingsMentor:Luhn:v1.0.3'
	}

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

    AmericanExpress("America Express", R.drawable.payment_ic_amex, "34, 37", "15"),
    MasterCard("Master", R.drawable.payment_ic_master_card, "51, 52, 53, 54, 55, 222100-272099", "16"),
    VisaElectron("Visa Electron", R.drawable.payment_ic_method, "4026, 417500, 4508, 4844, 4913, 4917", "16"),
    Visa("Visa", R.drawable.payment_ic_visa, "4", "13,16,19"),
    Maestro("Maestro", R.drawable.payment_ic_maestro_card, "5018, 5020, 5038, 5893, 6304, 6759, 6761, 6762, 6763", "12-19"),
    JCB("JCB", R.drawable.payment_ic_method, "3528-3589", "16-19"),
    InstaPayment("Insta Payment", R.drawable.payment_ic_method, "637, 638, 639", "16"),
    Verve("Verve", R.drawable.payment_ic_method, "506099-506198, 650002-650027", "16,19"),
    Discover("Discover", R.drawable.payment_ic_discover, "6011, 622126 to 622925, 644, 645, 646, 647, 648, 649, 65", "16,19"),
    UATP("Universal Air Travel Plan", R.drawable.payment_ic_method, "1", "15"),
    MIR("Mir", R.drawable.payment_ic_method, "2200 - 2204", "16"),
    Dankort("Dankort", R.drawable.payment_ic_method, "5019", "16"),
    Troy("Troy", R.drawable.payment_ic_method, "979200-979289", "16"),
    CardGuard("Card Guard", R.drawable.payment_ic_method, "5392", "16"),
    DinersClub("Diners Club", R.drawable.payment_ic_dinersclub, "300-305, 309, 36, 38, 39", "14,16-19"),
    ChinaUnionPay("China Union Pay", R.drawable.payment_ic_unionpay, "62", "16-19");


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
                        

                    }

                    @Override
                    public void otpRetrieved(int otp, final LuhnCardVerifier cardVerifier) {
                        /**
                         * otp is retrieved.
                         * 
                         * do something with otp and return callback to Luhn
                         */
                    }
                }, R.style.LuhnStyle);
```

##### Styling 

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

#### Credits

* <a href="https://github.com/card-io/card.io-Android-SDK" target="_blank">Card.io Android SDK</a>



#### Contributions 

Contributions are welcome. Generally, contributions are managed by issues and pull requests.

# License

The MIT License (MIT). Please see the [License File](https://github.com/KingsMentor/Luhn/blob/master/LICENSE) for more information.
