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
        //关联布局文件
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

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        showAnim = AnimationUtils.loadAnimation(context, R.anim.anim_in);
        showAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        hideAnim = AnimationUtils.loadAnimation(context, R.anim.anim_out);
        hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable bgColor = new ColorDrawable(0x60000000);
        //设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(bgColor);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new MenuItemOnTouchListener());
    }

    //触摸在popupWindow上方则取消显示
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
