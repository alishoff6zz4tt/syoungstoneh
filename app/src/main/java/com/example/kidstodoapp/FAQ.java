package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FAQ extends Activity {
    TextView faqText1;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_faq);

        faqText1 = findViewById(R.id.faq_entry_1_text);
        faqText1.setVisibility(View.GONE);
    }

    public void toggle_contents(View v) {
        if(faqText1.isShown()) {faqText1.setVisibility(View.GONE);}
        else {faqText1.setVisibility(View.VISIBLE);}
    }
}
