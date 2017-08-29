package xyz.belvi.luhn.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import xyz.belvi.luhn.R;

/**
 * Created by zone2 on 7/5/17.
 */

public class CardVerificationProgressScreen extends DialogFragment {


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        return inflater.inflate(R.layout.lh_progress_screen, container, false);
    }
}
