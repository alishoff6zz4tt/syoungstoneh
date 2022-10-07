package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FAQ extends Fragment implements View.OnClickListener {
    private TextView faqText1, faqText2, faqText3, faqText4, faqText5, faqText6, faqText7, faqText8, faqText9, faqText10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        faqText1 = view.findViewById(R.id.faq_entry_1_text);
        faqText1.setVisibility(View.GONE);

        faqText2 = view.findViewById(R.id.faq_entry_2_text);
        faqText2.setVisibility(View.GONE);

        faqText3 = view.findViewById(R.id.faq_entry_3_text);
        faqText3.setVisibility(View.GONE);

        faqText4 = view.findViewById(R.id.faq_entry_4_text);
        faqText4.setVisibility(View.GONE);

        faqText5 = view.findViewById(R.id.faq_entry_5_text);
        faqText5.setVisibility(View.GONE);

        faqText6 = view.findViewById(R.id.faq_entry_6_text);
        faqText6.setVisibility(View.GONE);

        faqText7 = view.findViewById(R.id.faq_entry_7_text);
        faqText7.setVisibility(View.GONE);

        faqText8 = view.findViewById(R.id.faq_entry_8_text);
        faqText8.setVisibility(View.GONE);

        faqText9 = view.findViewById(R.id.faq_entry_9_text);
        faqText9.setVisibility(View.GONE);

        faqText10 = view.findViewById(R.id.faq_entry_10_text);
        faqText10.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onClick(View view){
        toggleVisibility(view);
    }

    public void toggleVisibility(View view) {
        switch (view.getId()) {
            case R.id.faq_entry_1_title:
                if(faqText1.isShown()) {
                    faqText1.setVisibility(View.GONE);
//                  AnimationsUtility.toggleClose(getContext(), faqText1);
                }
                else {
                    faqText1.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText1);
                }
                break;
            case R.id.faq_entry_2_title:
                if(faqText2.isShown()) {
                    faqText2.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText2);
                }
                else {
                    faqText2.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText2);
                }
                break;
            case R.id.faq_entry_3_title:
                if(faqText3.isShown()) {
                    faqText3.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText3);
                }
                else {
                    faqText3.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText3);
                }
                break;
            case R.id.faq_entry_4_title:
                if(faqText4.isShown()) {
                    faqText4.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText4);
                }
                else {
                    faqText4.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText4);
                }
                break;
            case R.id.faq_entry_5_title:
                if(faqText5.isShown()) {
                    faqText5.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText5);
                }
                else {
                    faqText5.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText5);
                }
                break;
            case R.id.faq_entry_6_title:
                if(faqText6.isShown()) {
                    faqText6.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText6);
                }
                else {
                    faqText6.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText6);
                }
                break;
            case R.id.faq_entry_7_title:
                if(faqText7.isShown()) {
                    faqText7.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText7);
                }
                else {
                    faqText7.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText7);
                }
                break;
            case R.id.faq_entry_8_title:
                if(faqText8.isShown()) {
                    faqText8.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText8);
                }
                else {
                    faqText8.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText8);
                }
                break;
            case R.id.faq_entry_9_title:
                if(faqText9.isShown()) {
                    faqText9.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText9);
                }
                else {
                    faqText9.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText9);
                }
                break;
            case R.id.faq_entry_10_title:
                if(faqText10.isShown()) {
                    faqText10.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), faqText10);
                }
                else {
                    faqText10.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), faqText10);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setCheckedItem(R.id.FAQ);
    }
}
