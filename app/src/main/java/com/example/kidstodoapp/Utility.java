package com.example.kidstodoapp;

import android.os.Handler;

public class Utility {                                                                                                      //Utility class used to consolidate variables and methods that will need to be used throughout multiple classes
    private static String password;
    private static String phoneNumber;
    private static Boolean passwordSet = null;                                                                              //If the password has been set or not, used to determine if the user needs to create a password
    private static Boolean inParentMode = false;                                                                            //If we are in parent mode or not
    private static Boolean phoneNumberSet = false;                                                                          //If the phone number has been set, used to determine if the user can send a message to their parent or not

    public static String getPassword() {return password;}
    public static void setPassword(String password) {Utility.password = password;}
    public static Boolean isPasswordSet() {return passwordSet;}
    public static void setPasswordSet(Boolean passwordSet) {Utility.passwordSet = passwordSet;}
    public static Boolean isInParentMode() {return inParentMode;}
    public static void setInParentMode(Boolean inParentMode) {Utility.inParentMode = inParentMode;}

    public static String getPhoneNumber() {return phoneNumber;}
    public static void setPhoneNumber(String phoneNumber) {Utility.phoneNumber = phoneNumber;}
    public static Boolean isPhoneNumberSet() {return phoneNumberSet;}
    public static void setPhoneNumberSet(Boolean phoneNumberSet) {Utility.phoneNumberSet = phoneNumberSet;}

    public static void stopHandler(Handler handler, Runnable runnable) {handler.removeCallbacks(runnable);}                  //Stops the handler
    public static void startHandler(Handler handler, Runnable runnable) {handler.postDelayed(runnable, 60000);}   //Starts the handler, the runnable will run after 60 seconds (60000 milliseconds)
}
