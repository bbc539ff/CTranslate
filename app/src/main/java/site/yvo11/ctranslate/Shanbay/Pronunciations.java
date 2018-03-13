
package site.yvo11.ctranslate.Shanbay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pronunciations {

    @SerializedName("uk")
    @Expose
    private String uk;
    @SerializedName("us")
    @Expose
    private String us;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Pronunciations() {
    }

    /**
     * 
     * @param uk
     * @param us
     */
    public Pronunciations(String uk, String us) {
        super();
        this.uk = uk;
        this.us = us;
    }

    public String getUk() {
        return uk;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    public String getUs() {
        return us;
    }

    public void setUs(String us) {
        this.us = us;
    }

}
