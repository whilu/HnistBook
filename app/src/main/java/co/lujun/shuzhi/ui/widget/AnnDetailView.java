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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import co.lujun.shuzhi.R;

/**
 * Created by lujun on 2015/7/4.
 */
public class AnnDetailView extends PopupWindow {
    private static View mView;
    private static CardView cvGptv;
    private static Animation showAnim, hideAnim;
    private static TextView tvTitle, tvWriteInfo, tvContent;
    private static ScrollView svContent;
    private static AnnDetailView mInstance;


    private AnnDetailView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //关联布局文件
        mView = inflater.inflate(R.layout.ann_detail_view, null);
        cvGptv = (CardView) mView.findViewById(R.id.cv_gptv);
        tvTitle = (TextView) mView.findViewById(R.id.tv_adv_title);
        tvWriteInfo = (TextView) mView.findViewById(R.id.tv_adv_write_info);
        tvContent = (TextView) mView.findViewById(R.id.tv_adv_content);
        svContent = (ScrollView) mView.findViewById(R.id.sv_adv_content);

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

    public static void show(View parent, int gravity, int x, int y) {
        throwIfNotInit();
        svContent.setScrollY(0);
        mInstance.showAtLocation(parent, gravity, x, y);
        cvGptv.clearAnimation();
        cvGptv.startAnimation(showAnim);
    }

    public static void hide() {
        throwIfNotInit();
        cvGptv.clearAnimation();
        cvGptv.startAnimation(hideAnim);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override public void onAnimationStart(Animation animation) {}

            @Override public void onAnimationRepeat(Animation animation) {}

            @Override public void onAnimationEnd(Animation animation) {
                mInstance.dismiss();
            }
        });
    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new AnnDetailView(context);
        }
    }

    public static void setTitle(CharSequence title) {
        throwIfNotInit();
        tvTitle.setText(title);
    }

    public static void setWriteInfo(CharSequence writeInfo) {
        throwIfNotInit();
        tvWriteInfo.setText(writeInfo);
    }

    public static void setContent(CharSequence content) {
        throwIfNotInit();
        tvContent.setText(content);
    }

    private static void throwIfNotInit() {
        if (mInstance == null) {
            throw new IllegalStateException("AnnDetailView was't init, please init before use it!");
        }
    }
}
