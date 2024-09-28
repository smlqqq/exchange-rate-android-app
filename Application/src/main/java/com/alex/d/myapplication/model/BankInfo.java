package com.alex.d.myapplication.model;

import com.alex.d.myapplication.R;

public enum BankInfo {
    NATIONALA("https://www.bnm.md/", R.drawable.nationala),
    AGROINDBANK("https://www.maib.md/ro", R.drawable.agroindbank),
    MOLDINDCONBANK("https://www.micb.md/", R.drawable.moldindconbank),
    VICTORIABANK("https://www.victoriabank.md/ru/", R.drawable.victoriabank),
    MOBIASBANCA("https://www.mobiasbanca.md/", R.drawable.mobiasbanca),
    EXIMBANK("https://eximbank.md/ro", R.drawable.eximbank),
    PROCREDIT("https://www.procreditbank.md/", R.drawable.procredit),
    FINCOMBANK("https://fincombank.com/", R.drawable.fincombank),
    ENERGBANK("https://www.energbank.com", R.drawable.energbank),
    BCR("https://www.bcr.md/", R.drawable.bcr),
    COMERTBANK("https://comertbank.md/", R.drawable.comertbank),
    EUROCREDITBANK("https://www.ecb.md/", R.drawable.eurocreditbank),
    BLOCK1("https://valutar.md/ru/exchange-offices/deghest-csv", R.drawable.block),
    BLOCK2("https://valutar.md/ru/exchange-offices/clio-csv", R.drawable.block),
    BLOCK3("https://valutar.md/ru/exchange-offices/orion-csv", R.drawable.block),
    BLOCK4("https://valutar.md/ru/exchange-offices/profx-schimb-csv", R.drawable.block),
    BLOCK5("https://valutar.md/ru/exchange-offices/ciocana-csv", R.drawable.block),
    BLOCK6("https://valutar.md/ru/exchange-offices/calisto-ng-csv", R.drawable.block),
    BLOCK7("https://valutar.md/ru/exchange-offices/nelus-grup-csv", R.drawable.block),
    BLOCK8("https://valutar.md/ru/exchange-offices/protanir-csv", R.drawable.block),
    BLOCK9("https://valutar.md/ru/exchange-offices/vadisan-csv", R.drawable.block);

    private final String url;
    private final int imageResId;

    BankInfo(String url, int imageResId) {
        this.url = url;
        this.imageResId = imageResId;
    }

    public String getUrl() {
        return url;
    }

    public int getImageResId() {
        return imageResId;
    }
}
