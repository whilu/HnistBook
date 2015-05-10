package cn.hnist.lib.android.hnistbook.bean;

/**
 * Created by lujun on 2015/5/10.
 */
public class JsonData {

    private String data;

    private String info;

    private int status;

    public void setData(String data){
        this.data = data;
    }

    public String getData(){
        return data;
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
}
