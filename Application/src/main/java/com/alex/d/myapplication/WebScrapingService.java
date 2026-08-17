package com.alex.d.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Scrapes valutar.md directly from the device — this replaces the old Spring
 * backend (WebScrapingServiceImpl) that used to run on Render.
 *
 * IMPORTANT: every method here does blocking network I/O (Jsoup.connect().get()).
 * Never call scrapeData() on the main thread — it will throw
 * NetworkOnMainThreadException. Callers must run it on a background thread
 * (see MainActivity's ExecutorService usage, or ExchangeRatesWorker for the
 * background refresh job).
 */
public class WebScrapingService {

    private static final String BASE_URL = "https://valutar.md";
    private static final String BANKS_LIST_URL = BASE_URL + "/ru/banks";
    private static final int TIMEOUT_MS = 15000;
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; ExchangeRateApp/1.0)";

    // Currency names exactly as they appear in the "Валюта" column on a bank's
    // page (ru locale).
    private static final String CUR_USD = "Доллар США";
    private static final String CUR_EUR = "Евро";
    private static final String CUR_RON = "Румынский лей";
    private static final String CUR_GBP = "Фунт стерлингов";

    /**
     * Scrapes every bank's rates. Returns an empty list on any failure
     * (network down, site structure changed, etc.) — callers treat an empty
     * list as "fetch failed" and fall back to cached/error state.
     */
    public List<ListItemClass> scrapeData() {
        List<ListItemClass> data = new ArrayList<>();
        try {
            Map<String, String> bankLinks = fetchBankLinks();
            for (Map.Entry<String, String> entry : bankLinks.entrySet()) {
                try {
                    ListItemClass item = scrapeBankPage(entry.getKey(), entry.getValue());
                    if (item != null) {
                        data.add(item);
                    }
                } catch (Exception e) {
                    // one bank failing shouldn't take down the whole refresh
                }
            }
        } catch (Exception e) {
            // fetchBankLinks() itself failed (site down, no connectivity, markup changed
            // again) — return what we have, which is an empty list here
        }
        return data;
    }

    /**
     * Reads https://valutar.md/ru/banks and returns bank name -> bank page URL.
     * Deduplicates by href (not by link text), since the site has both a name
     * link and a "Подробнее" link pointing at the same bank page.
     */
    private Map<String, String> fetchBankLinks() throws Exception {
        Document doc = Jsoup.connect(BANKS_LIST_URL)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .get();

        Map<String, String> hrefToName = new LinkedHashMap<>();
        for (Element a : doc.select("a[href*=/banks/]")) {
            String href = a.attr("abs:href");
            String text = a.text().trim();

            if (!href.matches(".*/banks/[a-z0-9\\-]+/?$")) continue;
            if (text.isEmpty() || text.equalsIgnoreCase("Подробнее")) continue;

            hrefToName.putIfAbsent(href, text);
        }

        Map<String, String> nameToHref = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : hrefToName.entrySet()) {
            nameToHref.put(e.getValue(), e.getKey());
        }
        return nameToHref;
    }

    private ListItemClass scrapeBankPage(String bankName, String bankUrl) throws Exception {
        Document doc = Jsoup.connect(bankUrl)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .get();

        Element table = null;
        for (Element candidate : doc.select("table")) {
            String headText = candidate.text().toLowerCase();
            if (headText.contains("покупка") && headText.contains("продажа")) {
                table = candidate;
                break;
            }
        }
        if (table == null) {
            return null;
        }

        ListItemClass item = new ListItemClass();
        item.setBank(bankName);

        for (Element row : table.select("tr")) {
            if (!row.select("th").isEmpty()) continue;

            Elements cells = row.select("td");
            if (cells.size() < 3) continue;

            String currencyName = cells.get(0).text().trim();
            String buy = cells.get(1).text().trim();
            String sell = cells.get(2).text().trim();

            if (currencyName.contains(CUR_USD)) {
                item.setUsdB(buy);
                item.setUsdS(sell);
            } else if (currencyName.contains(CUR_EUR)) {
                item.setEuroB(buy);
                item.setEuroS(sell);
            } else if (currencyName.contains(CUR_RON)) {
                item.setRoLeuB(buy);
                item.setRoLeuS(sell);
            } else if (currencyName.contains(CUR_GBP)) {
                item.setGbpB(buy);
                item.setGbpS(sell);
            }
        }
        return item;
    }
}
