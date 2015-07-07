package co.lujun.shuzhi.anim;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Administrator on 2015/7/3.
 */
public class SwipViewAnimation {

    private int mIndex = 0;
    private int mDuration = 300;
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    float mDepthZ  = 500.0f;

    private View mContainer;
    private View mView1, mView2, mStartView;

    public SwipViewAnimation(View container, View view1, View view2){
        this.mContainer = container;
        this.mView1 = view1;
        this.mView2 = view2;
        this.mStartView = mView1;
        this.mCenterX = mContainer.getWidth() / 2;
        this.mCenterY = mContainer.getHeight() / 2;
    }

    public void applyRotation(float startAngle, float toAngle) {
        Rotate3dAnimation rotation = new Rotate3dAnimation(
                startAngle, toAngle, mCenterX, mCenterY, mDepthZ, true);
        rotation.setDuration(mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());

        mStartView.startAnimation(rotation);
    }

    public int getIndex(){
        return this.mIndex;
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {

            mContainer.post(new SwapViews());
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class SwapViews implements Runnable {
        @Override
        public void run()
        {
            Rotate3dAnimation rotation;

            mView1.setVisibility(View.GONE);
            mView2.setVisibility(View.GONE);

            mIndex++;
            if (0 == mIndex % 2){
                mStartView = mView1;
                rotation = new Rotate3dAnimation(
                        90,
                        0,
                        mCenterX,
                        mCenterY, mDepthZ, false);
            }else{
                mStartView = mView2;
                rotation = new Rotate3dAnimation(
                        -90,
                        0,
                        mCenterX,
                        mCenterY, mDepthZ, false);
            }

            mStartView.setVisibility(View.VISIBLE);
            mStartView.requestFocus();

            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            mStartView.startAnimation(rotation);
        }
    }
}
