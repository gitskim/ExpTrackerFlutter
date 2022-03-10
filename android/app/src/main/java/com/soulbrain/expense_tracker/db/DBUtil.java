package com.soulbrain.expense_tracker.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBUtil {
    private static final String TEST_STRING_AMEX =
            "Large purchase on American Express acct ending 81002 Feb 22 Amount: $21.07 Merch: H MART View recent activity at www.amexmobile.com";

    private static final String AMOUNT_REGEX = "(?<=Amount\\: \\$).*(?= Merch)";
    private static final String DESCRIPTION_REGEX = "(?<=Merch\\: ).*(?= View recent)";
    private static final Pattern
            rx1 = Pattern.compile(AMOUNT_REGEX),
            rx2 = Pattern.compile(DESCRIPTION_REGEX);

    public static String extractAmountAmex(String text) {
        Matcher matcher = rx1.matcher(text);
        String result = null;
        while (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    public static String extractDescAmex(String text) {
        Matcher matcher2 = rx2.matcher(text);
        String result = null;
        while (matcher2.find()) {
            result = matcher2.group();
        }
        return result;
    }

    public static String convertDate(String epochTime) {
        long milliseconds = Long.parseLong(epochTime);

        //convert seconds to milliseconds
        Date date = new Date(milliseconds);

        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String javaDate = jdf.format(date);
        return javaDate;
    }

}
