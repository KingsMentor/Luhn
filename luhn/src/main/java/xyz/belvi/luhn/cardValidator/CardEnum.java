package xyz.belvi.luhn.cardValidator;

import android.support.annotation.DrawableRes;

import xyz.belvi.luhn.R;


/**
 * Created by zone2 on 6/28/17.
 */

enum CardEnum {
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
        this.cardName = cardName;
        this.icon = icon;
        this.startWith = startWith;
        this.length = length;
    }

    private int icon;
    private String startWith, length, cardName;

    public int getIcon() {
        return this.icon;
    }

    public String getStartWith() {
        return this.startWith;
    }

    public String getLength() {
        return this.length;
    }

    public String getCardName() {
        return this.cardName;
    }
}
