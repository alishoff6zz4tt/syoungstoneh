package com.example.kidstodoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class FAQ extends Activity {
    private TextView faqText1, faqText2, faqText3, faqText4, faqText5, faqText6, faqText7, faqText8, faqText9, faqText10;
    private Handler parentModeTimeOut;
    private Runnable runnable;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_faq);

        faqText1 = findViewById(R.id.faq_entry_1_text);
        faqText1.setVisibility(View.GONE);

        faqText2 = findViewById(R.id.faq_entry_2_text);
        faqText2.setVisibility(View.GONE);

        faqText3 = findViewById(R.id.faq_entry_3_text);
        faqText3.setVisibility(View.GONE);

        faqText4 = findViewById(R.id.faq_entry_4_text);
        faqText4.setVisibility(View.GONE);

        faqText5 = findViewById(R.id.faq_entry_5_text);
        faqText5.setVisibility(View.GONE);

        faqText6 = findViewById(R.id.faq_entry_6_text);
        faqText6.setVisibility(View.GONE);

        faqText7 = findViewById(R.id.faq_entry_7_text);
        faqText7.setVisibility(View.GONE);

        faqText8 = findViewById(R.id.faq_entry_8_text);
        faqText8.setVisibility(View.GONE);

        faqText9 = findViewById(R.id.faq_entry_9_text);
        faqText9.setVisibility(View.GONE);

        faqText10 = findViewById(R.id.faq_entry_10_text);
        faqText10.setVisibility(View.GONE);

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {                               //This is what is done every x milliseconds unless the user
            @Override                                             //interacts with the screen
            public void run() {
                if(Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    Toast.makeText(FAQ.this,
                            "Exiting parent mode due to inactivity",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    public void toggleVisibility(View view) {
        switch (view.getId()) {
            case R.id.faq_entry_1_title:
                if(faqText1.isShown()) {
                    faqText1.setVisibility(View.GONE);
//                  toggleClose(this, faqText1);
                }
                else {
                    faqText1.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText1);
                }
                break;
            case R.id.faq_entry_2_title:
                if(faqText2.isShown()) {
                    faqText2.setVisibility(View.GONE);
//                    toggleClose(this, faqText2);
                }
                else {
                    faqText2.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText2);
                }
                break;
            case R.id.faq_entry_3_title:
                if(faqText3.isShown()) {
                    faqText3.setVisibility(View.GONE);
//                    toggleClose(this, faqText3);
                }
                else {
                    faqText3.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText3);
                }
                break;
            case R.id.faq_entry_4_title:
                if(faqText4.isShown()) {
                    faqText4.setVisibility(View.GONE);
//                    toggleClose(this, faqText4);
                }
                else {
                    faqText4.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText4);
                }
                break;
            case R.id.faq_entry_5_title:
                if(faqText5.isShown()) {
                    faqText5.setVisibility(View.GONE);
//                    toggleClose(this, faqText5);
                }
                else {
                    faqText5.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText5);
                }
                break;
            case R.id.faq_entry_6_title:
                if(faqText6.isShown()) {
                    faqText6.setVisibility(View.GONE);
//                    toggleClose(this, faqText6);
                }
                else {
                    faqText6.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText6);
                }
                break;
            case R.id.faq_entry_7_title:
                if(faqText7.isShown()) {
                    faqText7.setVisibility(View.GONE);
//                    toggleClose(this, faqText7);
                }
                else {
                    faqText7.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText7);
                }
                break;
            case R.id.faq_entry_8_title:
                if(faqText8.isShown()) {
                    faqText8.setVisibility(View.GONE);
//                    toggleClose(this, faqText8);
                }
                else {
                    faqText8.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText8);
                }
                break;
            case R.id.faq_entry_9_title:
                if(faqText9.isShown()) {
                    faqText9.setVisibility(View.GONE);
//                    toggleClose(this, faqText9);
                }
                else {
                    faqText9.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText9);
                }
                break;
            case R.id.faq_entry_10_title:
                if(faqText10.isShown()) {
                    faqText10.setVisibility(View.GONE);
//                    toggleClose(this, faqText10);
                }
                else {
                    faqText10.setVisibility(View.VISIBLE);
                    toggleOpen(this, faqText10);
                }
                break;
        }
    }

    public void toggleOpen(Context context, View view) {
        Animation toggleOpen = AnimationUtils.loadAnimation(context, R.anim.toggle_open);
        if(toggleOpen != null) {
            toggleOpen.reset();
            if(view != null) {
                view.clearAnimation();
                view.startAnimation(toggleOpen);
            }
        }
    }
    public void toggleClose(Context context, View view) {
        Animation toggleClose = AnimationUtils.loadAnimation(context, R.anim.toggle_close);
        if(toggleClose != null) {
            toggleClose.reset();
            if(view != null) {
                view.clearAnimation();
                view.startAnimation(toggleClose);
            }
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Utility.stopHandler(parentModeTimeOut, runnable);
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.startHandler(parentModeTimeOut, runnable);
    }
}
