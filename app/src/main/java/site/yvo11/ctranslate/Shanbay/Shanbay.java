
package site.yvo11.ctranslate.Shanbay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shanbay {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Shanbay() {
    }

    /**
     * 
     * @param statusCode
     * @param data
     * @param msg
     */
    public Shanbay(String msg, Integer statusCode, Data data) {
        super();
        this.msg = msg;
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
