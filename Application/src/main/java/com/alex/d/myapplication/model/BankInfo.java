package com.alex.d.myapplication.model;

import com.alex.d.myapplication.R;

public enum BankInfo {
    NATIONALA("Banca Națională", "https://www.bnm.md/", R.drawable.nationala),
    AGROINDBANK("Moldova Agroindbank", "https://www.maib.md/ro", R.drawable.agroindbank),
    MOLDINDCONBANK("Moldindconbank", "https://www.micb.md/", R.drawable.moldindconbank),
    VICTORIABANK("Victoriabank", "https://www.victoriabank.md/ru/", R.drawable.victoriabank),
    MOBIASBANCA("Mobiasbanca", "https://www.mobiasbanca.md/", R.drawable.mobiasbanca),
    EXIMBANK("Eximbank", "https://eximbank.md/ro", R.drawable.eximbank),
    PROCREDIT("ProCredit Bank", "https://www.procreditbank.md/", R.drawable.procredit),
    FINCOMBANK("FinComBank", "https://fincombank.com/", R.drawable.fincombank),
    ENERGBANK("Energbank", "https://www.energbank.com", R.drawable.energbank),
    BCR("BCR", "https://www.bcr.md/", R.drawable.bcr),
    COMERTBANK("Comerțbank", "https://comertbank.md/", R.drawable.comertbank),
    EUROCREDITBANK("EuroCreditBank", "https://www.ecb.md/", R.drawable.eurocreditbank),
    OTP_BANK("OTP Bank", "https://www.otpbank.md/", R.drawable.block), // TODO: add a real otp icon drawable
    DEGHEST("Deghest", "https://valutar.md/ru/exchange-offices/deghest-csv", R.drawable.block),
    CLIO("Clio", "https://valutar.md/ru/exchange-offices/clio-csv", R.drawable.block),
    ORION("Orion", "https://valutar.md/ru/exchange-offices/orion-csv", R.drawable.block),
    PROFX("PRO-FX Schimb", "https://valutar.md/ru/exchange-offices/profx-schimb-csv", R.drawable.block),
    CIOCANA("Ciocana", "https://valutar.md/ru/exchange-offices/ciocana-csv", R.drawable.block),
    CALISTO("Calisto NG", "https://valutar.md/ru/exchange-offices/calisto-ng-csv", R.drawable.block),
    NELUS("Nelus-Grup", "https://valutar.md/ru/exchange-offices/nelus-grup-csv", R.drawable.block),
    PROTANIR("Protanir", "https://valutar.md/ru/exchange-offices/protanir-csv", R.drawable.block),
    VADISAN("Vadisan", "https://valutar.md/ru/exchange-offices/vadisan-csv", R.drawable.block);

    private final String matchKey; // bank name as returned by the backend's "bank" field
    private final String url;
    private final int imageResId;

    BankInfo(String matchKey, String url, int imageResId) {
        this.matchKey = matchKey;
        this.url = url;
        this.imageResId = imageResId;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public String getUrl() {
        return url;
    }

    public int getImageResId() {
        return imageResId;
    }
}