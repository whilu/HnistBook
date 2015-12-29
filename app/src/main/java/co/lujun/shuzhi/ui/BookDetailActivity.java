package co.lujun.shuzhi.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.lujun.shuzhi.App;
import co.lujun.shuzhi.BuildConfig;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.anim.SwipViewAnimation;
import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.SystemUtil;
import co.lujun.tpsharelogin.utils.WXUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/3/18.
 */
public class BookDetailActivity extends BaseActivity {

    private Button btnFlip;
    private CardView mCardView1, mCardView2;
    private ImageView ivBookImg, vBookImgBlur;
    private LinearLayout llProgressBar, llContent;
    private SwipeRefreshLayout srlBookDetail;
    private Toolbar mToolBar;
    private TextView tvBookTitle, tvBookAuthor, tvBookPublisher, tvBookPubdate, tvBookPages,
            tvBookPrice, tvBookIsbn, tvBookSummary, tvBookTags, tvAp;
    private View mContainer;

    private Bundle mBundle;
    private String isbn = "";

    private SwipViewAnimation mSwipViewAnimation;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        init();
    }

    private void init(){
        mToolBar = (Toolbar) findViewById(R.id.book_detail_toolbar);
        ivBookImg = (ImageView) findViewById(R.id.iv_bda_book_img);
        vBookImgBlur = (ImageView) findViewById(R.id.iv_bda_book_img_blur);
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
            @Override public void onRefresh() {
                searchBook(isbn);
            }
        });
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
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
            setTitle(TextUtils.isEmpty(title) ? "" : "《" + title + "》");
            if (!TextUtils.isEmpty(isbn13)){
                isbn = isbn13;
            }else if (!TextUtils.isEmpty(isbn10)){
                isbn = isbn10;
            }
            searchBook(isbn);
        }
    }

    private void searchBook(String isbn){
        if (TextUtils.isEmpty(isbn)) {
            onLoadComplete();
            return;
        }
        if (NetWorkUtils.getNetWorkType(this) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_no_internet);
            onLoadComplete();
            return;
        }

        App.getDbApiService()
                .getDbBook(isbn, BuildConfig.DB_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Book>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {
                        SystemUtil.showToast(R.string.msg_find_error);
                        onLoadComplete();
                    }

                    @Override public void onNext(Book book) {
                        onLoadComplete();
                        setData(book);
                    }
                });
    }

    private void setData(final Book book){
        if (book == null){
            SystemUtil.showToast(R.string.msg_no_find);
            return;
        }
        String imgUrl = book.getImages().getLarge();
        if (!TextUtils.isEmpty(imgUrl)){
            Observable.just(imgUrl)
                    .map(new Func1<String, Bitmap[]>() {
                        @Override public Bitmap[] call(String s) {
                            Bitmap bmp = WXUtil.getBitmapFromUrl(s);
                            return new Bitmap[]{bmp, SystemUtil.blurImage(
                                    BookDetailActivity.this, bmp, Config.BLUR_RADIUS)};
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap[]>() {
                        @Override public void call(Bitmap[] bmps) {
                            ivBookImg.setImageBitmap(bmps[0]);
                            vBookImgBlur.setImageBitmap(bmps[1]);
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

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
