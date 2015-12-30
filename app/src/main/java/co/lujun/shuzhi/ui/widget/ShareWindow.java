package co.lujun.shuzhi.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import co.lujun.shuzhi.R;
import co.lujun.shuzhi.ui.listener.CustomAnimationListener;

/**
 * Created by lujun on 2015/7/4.
 */
public class ShareWindow extends PopupWindow {

    private static View mView;
    private static CardView cvGptv;
    private static Animation showAnim, hideAnim;
    private ImageButton btnWXF, btnWXT, btnWB, btnQF, btnQT;

    public ShareWindow(Context context, View.OnClickListener listener) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.view_share, null);
        cvGptv = (CardView) mView.findViewById(R.id.cv_gptv_share);
        btnWXF = (ImageButton) mView.findViewById(R.id.btn_share_wechatf);
        btnWXT = (ImageButton) mView.findViewById(R.id.btn_share_wechatt);
        btnWB = (ImageButton) mView.findViewById(R.id.btn_share_weibo);
        btnQF = (ImageButton) mView.findViewById(R.id.btn_share_qqf);
        btnQT = (ImageButton) mView.findViewById(R.id.btn_share_qqt);

        btnWXF.setOnClickListener(listener);
        btnWXT.setOnClickListener(listener);
        btnWB.setOnClickListener(listener);
        btnQF.setOnClickListener(listener);
        btnQT.setOnClickListener(listener);

        this.setContentView(mView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        showAnim = AnimationUtils.loadAnimation(context, R.anim.anim_in);
        showAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        hideAnim = AnimationUtils.loadAnimation(context, R.anim.anim_out);
        hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        ColorDrawable bgColor = new ColorDrawable(0x60000000);
        this.setBackgroundDrawable(bgColor);
        mView.setOnTouchListener(new MenuItemOnTouchListener());
    }

    private class MenuItemOnTouchListener implements View.OnTouchListener {

        @Override public boolean onTouch(View v, MotionEvent event) {
            int y = (int) event.getY();
            int x = (int) event.getX();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < cvGptv.getTop() || y > cvGptv.getBottom()
                        || x < cvGptv.getLeft() || x > cvGptv.getRight()) {
                    hide();
                }
            }
            return true;
        }
    }

    public void show(View parent, int gravity, int x, int y) {
        this.showAtLocation(parent, gravity, x, y);
        cvGptv.clearAnimation();
        cvGptv.startAnimation(showAnim);
    }

    public void hide() {
        cvGptv.clearAnimation();
        cvGptv.startAnimation(hideAnim);
        hideAnim.setAnimationListener(new CustomAnimationListener() {
            @Override public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });
    }
}
