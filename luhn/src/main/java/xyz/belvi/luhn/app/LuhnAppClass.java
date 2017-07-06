package xyz.belvi.luhn.app;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import xyz.belvi.luhn.R;

/**
 * Created by zone2 on 7/4/17.
 */

public class LuhnAppClass extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ClanProForUBER-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        //....
    }
}
