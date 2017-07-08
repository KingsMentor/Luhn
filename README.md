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

... still updating readme ...
