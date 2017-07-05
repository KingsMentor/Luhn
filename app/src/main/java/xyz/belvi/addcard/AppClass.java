package xyz.belvi.addcard;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by zone2 on 7/4/17.
 */

public class AppClass extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ClanProForUBER-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //....
    }
}
