
package site.yvo11.ctranslate.Shanbay;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnDefinitions {

    @SerializedName("v")
    @Expose
    private List<String> v = null;
    @SerializedName("n")
    @Expose
    private List<String> n = null;
    @SerializedName("pron")
    @Expose
    private List<String> pron = null;
    @SerializedName("adj")
    @Expose
    private List<String> adj = null;
    @SerializedName("adv")
    @Expose
    private List<String> adv = null;
    @SerializedName("num")
    @Expose
    private List<String> num = null;
    @SerializedName("art")
    @Expose
    private List<String> art = null;
    @SerializedName("prep")
    @Expose
    private List<String> prep = null;
    @SerializedName("conj")
    @Expose
    private List<String> conj = null;
    @SerializedName("interj")
    @Expose
    private List<String> interj = null;
    /**
     * No args constructor for use in serialization
     * 
     */
    public EnDefinitions() {
    }

    /**
     * 
     * @param v
     * @param n
     */
    public EnDefinitions(List<String> v, List<String> n) {
        super();
        this.v = v;
        this.n = n;
        this.pron = pron;
        this.adj = adj;
        this.adv = adv;
        this.num = num;
        this.art = art;
        this.prep = prep;
        this.conj = conj;
        this.interj = interj;
    }

    public List<String> getV() {
        return v;
    }

    public void setV(List<String> v) {
        this.v = v;
    }

    public List<String> getN() {
        return n;
    }

    public void setN(List<String> n) {
        this.n = n;
    }

    public List<String> getAdj() {
        return adj;
    }

    public void setAdj(List<String> adj) {
        this.adj = adj;
    }

    public List<String> getAdv() {
        return adv;
    }

    public void setAdv(List<String> adv) {
        this.adv = adv;
    }

    public List<String> getArt() {
        return art;
    }

    public void setArt(List<String> art) {
        this.art = art;
    }

    public List<String> getConj() {
        return conj;
    }

    public void setConj(List<String> conj) {
        this.conj = conj;
    }

    public List<String> getInterj() {
        return interj;
    }

    public void setInterj(List<String> interj) {
        this.interj = interj;
    }

    public List<String> getNum() {
        return num;
    }

    public void setNum(List<String> num) {
        this.num = num;
    }

    public List<String> getPrep() {
        return prep;
    }

    public void setPrep(List<String> prep) {
        this.prep = prep;
    }

    public List<String> getPron() {
        return pron;
    }

    public void setPron(List<String> pron) {
        this.pron = pron;
    }
}
