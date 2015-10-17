package co.lujun.shuzhi.ui.widget;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import co.lujun.shuzhi.R;

/**
 * Created by lujun on 2015/9/15.
 */
public class LoadingWindow extends PopupWindow {
    private View mView;
    private TextView tvProgress;

    public LoadingWindow(View view){
        super(view);
        this.mView = view;
        tvProgress = (TextView) mView.findViewById(R.id.tv_vl_progress);
        this.setContentView(mView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * set text
     * @param text
     */
    public void setProgressText(String text){
        tvProgress.setText(text);
    }
}
