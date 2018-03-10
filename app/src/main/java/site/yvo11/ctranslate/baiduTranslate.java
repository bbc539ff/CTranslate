package site.yvo11.ctranslate;

import java.util.List;

/**
 * Created by yvo11 on 2018/2/18.
 */

public class baiduTranslate {

    public String from;
    public String to;
    public List<trans_result> trans_result = null;
    public class trans_result {
        public String src;
        public String dst;
    }


}
