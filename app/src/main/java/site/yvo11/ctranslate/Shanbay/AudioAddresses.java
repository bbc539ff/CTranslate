
package site.yvo11.ctranslate.Shanbay;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioAddresses {

    @SerializedName("uk")
    @Expose
    private List<String> uk = null;
    @SerializedName("us")
    @Expose
    private List<String> us = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AudioAddresses() {
    }

    /**
     * 
     * @param uk
     * @param us
     */
    public AudioAddresses(List<String> uk, List<String> us) {
        super();
        this.uk = uk;
        this.us = us;
    }

    public List<String> getUk() {
        return uk;
    }

    public void setUk(List<String> uk) {
        this.uk = uk;
    }

    public List<String> getUs() {
        return us;
    }

    public void setUs(List<String> us) {
        this.us = us;
    }

}
