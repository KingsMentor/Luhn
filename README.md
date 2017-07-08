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

![Lib Sample](https://github.com/KingsMentor/Luhn/blob/master/screenshots/add_card_collage.jpg)



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
	        compile 'com.github.KingsMentor:Luhn:v1.0'
	}

#### Supported Attributes
```
        <attr name="luhn_show_pin" format="boolean" />
        <attr name="luhn_title" format="string" />
        <attr name="luhn_show_toolbar_color" format="color" />
        <attr name="luhn_btn_verify_selector" format="reference" />
        <attr name="luhn_typeface" format="string" />
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

#License

The MIT License (MIT). Please see the [License File](https://github.com/KingsMentor/Luhn/blob/master/LICENSE) for more information.
