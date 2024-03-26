package com.moneyline.srvauth.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class FnUtilities {
    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static boolean isNullOrEmpty(String value) {

        if (value == null || value.isEmpty()) {
            return true;
        }
        return false;
    } public static boolean isNullOrEmpty(Object value) {

        if (value == null) {
            return true;
        }
        return false;
    }

    public static String getlang(String object) {
        if (isNullOrEmpty(object)) {
            return Parameter.FR;
        }
        if (!object.equalsIgnoreCase(Parameter.FR) && !object.equalsIgnoreCase(Parameter.ENG)) {
            return Parameter.FR;
        }
        return object.toLowerCase();
    }

    public static String DateToString(Date actuelle, SimpleDateFormat Sformat) {
        String dat = "";
        try {
            dat = Sformat.format(actuelle);
        } catch (Exception e) {
            e.printStackTrace();
            dat = GetDateNow(Sformat);
        }
        return dat;
    }

    public static String GetDateNow(SimpleDateFormat Sformat) {

        Date actuelle = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(actuelle);
        actuelle = cal.getTime();
        String dat = Sformat.format(actuelle);
        return dat;
    }

    public static String getComplexId() {

        String catime;

        String key = null;
        Date newDate_depart, lastDate_depart;
        Date newDate_return, lastDate_return;
        int mm, ss, hh, mois, jour, annee, mls;

        newDate_depart = new Date();
        newDate_return = new Date();
        lastDate_depart = new Date();
        lastDate_return = new Date();

        Calendar now = Calendar.getInstance();

        mm = now.get(Calendar.MINUTE);
        ss = now.get(Calendar.SECOND);
        mls = now.get(Calendar.MILLISECOND);
        hh = now.get(Calendar.HOUR_OF_DAY);
        mois = now.get(Calendar.MONTH) + 1;
        jour = now.get(Calendar.DAY_OF_MONTH);
        annee = now.get(Calendar.YEAR);

        //<editor-fold defaultstate="collapsed" desc="[ COMMENT ]">
        catime = (getKeyYear() + "" + String.valueOf(mois) + "" + String.valueOf(jour) + "" + String.valueOf(hh) + "" + String.valueOf(mm) + "" + String.valueOf(ss) + "" + String.valueOf(mls));
        catime = catime + GetNumberRandom();

        if (catime.length() < 20) {
            catime = catime + GetNumberRandom();
        }

        if (catime.length() == 20) {
            return catime;
        }
        if (catime.length() > 20) {
            catime = catime.substring(0, 20);
        }
        //  //new logger().OCategory.info(this.catime.length());

        return catime;
        //</editor-fold>

    }

    public static int getKeyYear() {
        int mm, ss, hh, mois, jour, annee, mls;
        Calendar now = Calendar.getInstance();
        annee = now.get(Calendar.YEAR);
        return annee - 2010;
    }

    public static String GetNumberRandom() {
        Random rn = new Random();
        int myInt = rn.nextInt(1000000);
        return String.valueOf(myInt);
    }

    public static String random6digits() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            int myInt = sr.nextInt(9000000) + 1000000;
            return String.valueOf(myInt);
        } catch (NoSuchAlgorithmException e) {
            return GetNumberRandom();
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }

//    public static void main(String[] args) {
//        System.out.println( random6digits());
//    }
}
