package cn.hnist.lib.android.hnistbook.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/5/23.
 */
public class Photo extends DataSupport {

    private String id;

    private String alt;

    private String album_id;

    private String album_title;

    private String icon;

    private String thumb;

    private String cover;

    private String image;

    private String desc;

    private String created;

    private String privacy;

    private int position;

    private String prev_photo;

    private String next_photo;

    private int liked_count;

    private int recs_count;

    private int comments_count;

    private User author;

    private boolean liked;

    private Size sizes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPrev_photo() {
        return prev_photo;
    }

    public void setPrev_photo(String prev_photo) {
        this.prev_photo = prev_photo;
    }

    public String getNext_photo() {
        return next_photo;
    }

    public void setNext_photo(String next_photo) {
        this.next_photo = next_photo;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }

    public int getRecs_count() {
        return recs_count;
    }

    public void setRecs_count(int recs_count) {
        this.recs_count = recs_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Size getSizes() {
        return sizes;
    }

    public void setSizes(Size sizes) {
        this.sizes = sizes;
    }

    public class Size{

        private int[] icon;

        private int[] thumb;

        private int[] cover;

        private int[] image;

        public int[] getIcon() {
            return icon;
        }

        public void setIcon(int[] icon) {
            this.icon = icon;
        }

        public int[] getThumb() {
            return thumb;
        }

        public void setThumb(int[] thumb) {
            this.thumb = thumb;
        }

        public int[] getCover() {
            return cover;
        }

        public void setCover(int[] cover) {
            this.cover = cover;
        }

        public int[] getImage() {
            return image;
        }

        public void setImage(int[] image) {
            this.image = image;
        }
    }
}