package co.lujun.shuzhi.ui.listener;

import android.view.animation.Animation;

/**
 * Created by lujun on 2015/10/1.
 */
public abstract class CustomAnimationListener implements Animation.AnimationListener {

    @Override public void onAnimationStart(Animation animation) {}

    @Override public void onAnimationRepeat(Animation animation) {}

    @Override public void onAnimationEnd(Animation animation) {}
}
