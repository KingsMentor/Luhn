package xyz.belvi.luhn.cardValidator.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zone2 on 7/5/17.
 */

public final class LuhnCard implements Parcelable {
    private String pan, cardName, expDate, cvv, pin;
    private int expMonth, expYear;


    public LuhnCard(String pan, String cardName, String expDate, String cvv, String pin, int expMonth, int expYear) {
        this.pan = pan;
        this.cardName = cardName;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.pin = pin;
        this.expDate = expDate;
    }

    protected LuhnCard(Parcel in) {
        pan = in.readString();
        cardName = in.readString();
        expMonth = in.readInt();
        expYear = in.readInt();
        cvv = in.readString();
        pin = in.readString();
        expDate = in.readString();
    }

    public static final Creator<LuhnCard> CREATOR = new Creator<LuhnCard>() {
        @Override
        public LuhnCard createFromParcel(Parcel in) {
            return new LuhnCard(in);
        }

        @Override
        public LuhnCard[] newArray(int size) {
            return new LuhnCard[size];
        }
    };

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pan);
        dest.writeString(cardName);
        dest.writeInt(expMonth);
        dest.writeInt(expYear);
        dest.writeString(cvv);
        dest.writeString(pin);
        dest.writeString(expDate);
    }

    public String getPan() {
        return this.pan;
    }

    public int getExpMonth() {
        return this.expMonth;
    }

    public int getExpYear() {
        return this.expYear;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getCvv() {
        return cvv;
    }

    public String getPin() {
        return pin;
    }

    public String getCardName() {
        return this.cardName;
    }
}
