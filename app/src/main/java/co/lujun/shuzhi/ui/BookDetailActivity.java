package co.lujun.shuzhi.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.anim.SwipViewAnimation;
import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.JSONRequest;
import co.lujun.shuzhi.ui.widget.SlidingActivity;
import co.lujun.shuzhi.util.BlurUtils;
import co.lujun.shuzhi.util.NetWorkUtils;

/**
 * Created by lujun on 2015/3/18.
 */
public class BookDetailActivity extends SlidingActivity {

    private Toolbar mToolBar;
    private ImageView ivBookImg;
    private View vBookImgBlur;
    private TextView tvBookTitle, tvBookAuthor, tvBookPublisher, tvBookPubdate, tvBookPages,
            tvBookPrice, tvBookIsbn, tvBookSummary, tvBookTags, tvAp;
    private LinearLayout llProgressBar, llContent;
    private Bundle mBundle;
    private SwipeRefreshLayout srlBookDetail;
    private String isbn = "";

    private CardView mCardView1, mCardView2;
    private View mContainer;
    private Button btnFlip;

    private SwipViewAnimation mSwipViewAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        mToolBar = (Toolbar) findViewById(R.id.book_detail_toolbar);
        ivBookImg = (ImageView) findViewById(R.id.iv_bda_book_img);
        vBookImgBlur = (View) findViewById(R.id.v_bda_book_img_blur);
        tvBookTitle = (TextView) findViewById(R.id.tv_bda_book_title);
        tvBookAuthor = (TextView) findViewById(R.id.tv_bda_book_author);
        tvBookPublisher = (TextView) findViewById(R.id.tv_bda_book_publisher);
        tvBookPubdate = (TextView) findViewById(R.id.tv_bda_book_pubdate);
        tvBookPages = (TextView) findViewById(R.id.tv_bda_book_pages);
        tvBookPrice = (TextView) findViewById(R.id.tv_bda_book_price);
        tvBookIsbn = (TextView) findViewById(R.id.tv_bda_book_isbn);
        tvBookSummary = (TextView) findViewById(R.id.tv_bda_book_summary);
        tvBookTags = (TextView) findViewById(R.id.tv_bda_book_tags);
        llContent = (LinearLayout) findViewById(R.id.ll_bda_content);
        llProgressBar = (LinearLayout) findViewById(R.id.ll_progressBar_bda_view);
        srlBookDetail =(SwipeRefreshLayout) findViewById(R.id.srl_bookdetail);
        mContainer = findViewById(R.id.fl_book_detail_container);
        mCardView1 = (CardView) findViewById(R.id.cv_book_detail_1);
        mCardView2 = (CardView) findViewById(R.id.cv_book_detail_2);
        tvAp = (TextView) findViewById(R.id.tv_bda_book_ap);
        btnFlip = (Button) findViewById(R.id.btn_book_detail_flip);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        srlBookDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TextUtils.isEmpty(isbn)) {
                    /*Toast.makeText(BookDetailActivity.this,
                            getResources().getString(R.string.msg_intent_extras_null),
                            Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(GlApplication.getContext(),
                            getResources().getString(R.string.msg_param_null),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                searchBook(isbn);
            }
        });
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwipViewAnimation == null){
                    mSwipViewAnimation = new SwipViewAnimation(mContainer, mCardView1, mCardView2);
                }
                if (0 == mSwipViewAnimation.getIndex() % 2) {
                    mSwipViewAnimation.applyRotation(0, 90);
                } else {
                    mSwipViewAnimation.applyRotation(0, -90);
                }
            }
        });
        if ((mBundle = getIntent().getExtras()) != null){
            String title = mBundle.getString(Config.BOOK.title.toString());
            String isbn10 = mBundle.getString(Config.BOOK.isbn10.toString());
            String isbn13 = mBundle.getString(Config.BOOK.isbn13.toString());
            setTitle((title == null || title.equals("")) ? "" : "《" + title + "》");
            if (isbn13 == null || isbn13.equals("")){
                if (isbn10 == null || isbn10.equals("")){
                    /*Toast.makeText(this, getResources().getString(R.string.msg_intent_extras_null),
                            Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(GlApplication.getContext(), getResources().getString(R.string.msg_param_null),
                            Toast.LENGTH_SHORT).show();
                }else {
                    isbn = isbn10;
                    searchBook(isbn10);
                }
            }else {
                isbn = isbn13;
                searchBook(isbn13);
            }
        }else{
            /*Toast.makeText(this,
                    getResources().getString(R.string.msg_intent_extras_null) + getIntent().getExtras(),
                    Toast.LENGTH_SHORT).show();*/
            Toast.makeText(GlApplication.getContext(), getResources().getString(R.string.msg_param_null),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void searchBook(String isbn){
        if (NetWorkUtils.getNetWorkType(this) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            Toast .makeText(this, getResources().getString(R.string.msg_no_internet),
                    Toast.LENGTH_SHORT).show();
            onLoadComplete();
            return;
        }
        JSONRequest<Book> jsonRequest = new JSONRequest<Book>(
                Api.GET_ISBNBOOK_URL + isbn,
                Book.class,
                new Response.Listener<Book>() {
                    @Override
                    public void onResponse(Book book) {
                        setData(book);
                        onLoadComplete();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast .makeText(GlApplication.getContext(), volleyError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        onLoadComplete();
                        /*Toast .makeText(getActivity(),
                                    getResources().getString(R.string.msg_find_error),
                                    Toast.LENGTH_SHORT).show();*/
                    }
                });
        GlApplication.getRequestQueue().add(jsonRequest);
    }

    private void setData(Book book){
        if (book == null){
            Toast .makeText(GlApplication.getContext(), getResources().getString(R.string.msg_no_find),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(book.getImages().getMedium())){
            ImageLoader.getInstance().loadImage(book.getImages().getLarge(),
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            final Bitmap bmp = loadedImage;
                            vBookImgBlur.getViewTreeObserver().addOnGlobalLayoutListener(
                                    new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        if (vBookImgBlur.getBackground() == null) {
                                            BlurUtils.blur(bmp, vBookImgBlur);
                                        }
                                    }
                                }
                            );
                            ivBookImg.setImageBitmap(loadedImage);
                        }
                    });
        }
        if (TextUtils.isEmpty(getTitle())){ setTitle(book.getTitle()); }
        tvBookTitle.setText(book.getTitle());
        String author = "";
        for (int j = 0; j < book.getAuthor().length; j++){
            author += book.getAuthor()[j] + "、";
        }
        if (author.length() > 0){ author = author.substring(0, author.length() - 1); }
        tvBookAuthor.setText(author);
        tvBookPublisher.setText(book.getPublisher());
        tvBookPubdate.setText(book.getPubdate());
        tvBookPages.setText(book.getPages() + getString(R.string.tv_unit_page));
        tvBookPrice.setText(book.getPrice());
        tvBookIsbn.setText(TextUtils.isEmpty(book.getIsbn13()) ? book.getIsbn10() : book.getIsbn13());
        String tags = "";
        for (Book.Tag tag : book.getTags()){
            tags += tag.getName() + "、";
        }
        if (tags.length() > 0){ tags = tags.substring(0, tags.length() - 1); }
        tvBookTags.setText(tags);
        tvAp.setText(author + " / " + book.getPublisher());
        tvBookSummary.setText(book.getSummary());
    }

    private void onLoadComplete(){
        llProgressBar.setVisibility(View.GONE);
        llContent.setVisibility(View.VISIBLE);
        srlBookDetail.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
