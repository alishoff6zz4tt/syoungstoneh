package com.example.kidstodoapp;

import android.os.Handler;

public class Utility {
    private static String password;
    private static String phoneNumber;
    private static Boolean passwordSet = null;
    private static Boolean inParentMode = false;
    private static Boolean phoneNumberSet = false;

    public static String getPassword() {return password;}
    public static void setPassword(String password) {Utility.password = password;}
    public static Boolean isNotFirstTime() {return passwordSet;}
    public static void setNotFirstTime(Boolean passwordSet) {Utility.passwordSet = passwordSet;}
    public static Boolean isInParentMode() {return inParentMode;}
    public static void setInParentMode(Boolean inParentMode) {Utility.inParentMode = inParentMode;}

    public static String getPhoneNumber() {return phoneNumber;}
    public static void setPhoneNumber(String phoneNumber) {Utility.phoneNumber = phoneNumber;}
    public static Boolean isPhoneNumberSet() {return phoneNumberSet;}
    public static void setPhoneNumberSet(Boolean phoneNumberSet) {Utility.phoneNumberSet = phoneNumberSet;}

    public static void stopHandler(Handler handler, Runnable runnable) {handler.removeCallbacks(runnable);}
    public static void startHandler(Handler handler, Runnable runnable) {handler.postDelayed(runnable, 10000);}
}
