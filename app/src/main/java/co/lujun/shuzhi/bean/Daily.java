package co.lujun.shuzhi.bean;

import java.io.Serializable;

/**
 * Created by lujun on 2015/5/10.
 */
public class Daily implements Serializable {

    private static final long serialVersionUID = 10L; // 序列化ID

    private Book book;

    private String info;

    private int status;

    private Extra extra;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public String getInfo(){
        return info;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    public void setExtra(Extra extra){
        this.extra = extra;
    }

    public Extra getExtra(){
        return extra;
    }

    public class Extra implements Serializable{

        private static final long serialVersionUID = 11L; // 序列化ID

        private String date;

        private String brief;

        private String vol;

        public void setDate(String date){
            this.date = date;
        }

        public String getDate(){
            return date;
        }

        public void setBrief(String brief){
            this.brief = brief;
        }

        public String getBrief(){
            return brief;
        }

        public void setVol(String vol){
            this.vol = vol;
        }

        public String getVol(){
            return vol;
        }

        /**
         * get year, month, day
         * @return String[] year,month. day
         */
        public String[] getYMD(){
            String[] ymd = this.getDate().split("-");
            return ymd;
        }
    }
}
