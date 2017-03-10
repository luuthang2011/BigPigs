package com.bigpigs.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * Lop Utility
 */
public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email mau
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";


    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
    /**
     *
     * Kiem tra xem String obj co null hay ko
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }
}
