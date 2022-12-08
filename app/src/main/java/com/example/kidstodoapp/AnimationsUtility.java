package com.example.kidstodoapp;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationsUtility {
  public static void toggleOpen(Context context, View view) {
    Animation toggleOpen = AnimationUtils.loadAnimation(context, R.anim.toggle_open);
    if (toggleOpen != null) {
      toggleOpen.reset();
      if (view != null) {
        view.clearAnimation();
        view.startAnimation(toggleOpen);
      }
    }
  }

  public static void toggleClose(Context context, View view) {
    Animation toggleClose = AnimationUtils.loadAnimation(context, R.anim.toggle_close);
    if (toggleClose != null) {
      toggleClose.reset();
      if (view != null) {
        view.clearAnimation();
        view.startAnimation(toggleClose);
      }
    }
  }
}
