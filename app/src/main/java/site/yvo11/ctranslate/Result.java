package site.yvo11.ctranslate;

/**
 * Created by yvo11 on 2018/3/11.
 */

public class Result {
    private String src;
    private String dst;

    Result(String src, String dst){
        this.src = src;
        this.dst = dst;
    }

    public String getDst() {
        return dst;
    }

    public String getSrc() {
        return src;
    }
}
