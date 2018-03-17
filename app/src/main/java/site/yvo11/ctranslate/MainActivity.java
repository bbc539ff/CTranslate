package site.yvo11.ctranslate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import site.yvo11.ctranslate.Shanbay.Shanbay;


public class MainActivity extends AppCompatActivity {
    Intent startService;
    EditText editText1;
    TextView textview2;
    Handler handler;
    String src = "";
    String dst = "";
    Spinner spinner1;
    Spinner spinner2;
    String selectLang1 = "";
    String selectLang2 = "";
    Button tButton;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;
    int setSource = 1;
    String word;
    boolean isNeedTransparent;

    @Override
    public void setTheme(int resid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isNeedTransparent = sharedPreferences.getBoolean("setStart", false);

        Intent intent = getIntent();
        isNeedTransparent = intent.getBooleanExtra("setStart", isNeedTransparent);


        if(isNeedTransparent){
            super.setTheme(R.style.AppTheme_NoDisplay);
        }else{
            super.setTheme(R.style.AppTheme_NoActionBar);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService = new Intent(this, ClipboardService.class);
        startService(startService);


        editor1 = getSharedPreferences("setting", MODE_PRIVATE).edit();
        editText1 = findViewById(R.id.editText1);
        tButton = findViewById(R.id.tButton);
        textview2 = findViewById(R.id.textview2);
        handler = new Handler();

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.languages);
                selectLang1 = languages[position];
                editor1.putInt("selectLang1", position);
                editor1.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages2 = getResources().getStringArray(R.array.languages2);
                selectLang2 = languages2[position];
                editor1.putInt("selectLang2", position);
                editor1.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SharedPreferences sharedPreferences1 = getSharedPreferences("setting", MODE_PRIVATE);
        if(sharedPreferences1.contains("selectLang1")){
            int pos = sharedPreferences1.getInt("selectLang1", 0);
            String[] languages = getResources().getStringArray(R.array.languages);
            selectLang1 = languages[pos];
            spinner1.setSelection(pos,true);
        }

        if(sharedPreferences1.contains("selectLang2")){
            int pos = sharedPreferences1.getInt("selectLang2", 0);
            String[] languages = getResources().getStringArray(R.array.languages2);
            selectLang2 = languages[pos];
            spinner2.setSelection(pos,true);
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setSource = Integer.valueOf(prefs.getString("sourceListPreference", "1")).intValue();
        if(setSource == 2){
            spinner1.setVisibility(View.INVISIBLE);
            spinner2.setVisibility(View.INVISIBLE);
        }else{
            spinner1.setVisibility(View.VISIBLE);
            spinner2.setVisibility(View.VISIBLE);
        }
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setSource == 2){
                    word = editText1.getText().toString();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("https://api.shanbay.com/bdc/search/?word="+word)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            String enDefinitions;
                            String defn;
                            String uk;
                            String us;
                            String raw = response.body().string();
                            Gson g = new Gson();
                            Shanbay shanbayresult = g.fromJson(raw, Shanbay.class);
                            int statusCode = shanbayresult.getStatusCode();
                            if(statusCode == 0){
                                List<String> adj = shanbayresult.getData().getEnDefinitions().getAdj();
                                List<String> adv = shanbayresult.getData().getEnDefinitions().getAdv();
                                List<String> art = shanbayresult.getData().getEnDefinitions().getArt();
                                List<String> conj = shanbayresult.getData().getEnDefinitions().getConj();
                                List<String> interj = shanbayresult.getData().getEnDefinitions().getInterj();
                                List<String> n = shanbayresult.getData().getEnDefinitions().getN();
                                List<String> num = shanbayresult.getData().getEnDefinitions().getNum();
                                List<String> prep = shanbayresult.getData().getEnDefinitions().getPrep();
                                List<String> pron = shanbayresult.getData().getEnDefinitions().getPron();
                                List<String> v = shanbayresult.getData().getEnDefinitions().getV();

                                String str = "";
                                if(adj != null){
                                    str = str + "adj. ";
                                    for(String tmp:adj){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(adv != null){
                                    str = str + "adv. ";
                                    for(String tmp:adv){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(art != null){
                                    str = str + "art. ";
                                    for(String tmp:art){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(conj != null){
                                    str = str + "conj. ";
                                    for(String tmp:conj){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(interj != null){
                                    str = str + "interj. ";
                                    for(String tmp:interj){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(n != null){
                                    str = str + "n. ";
                                    for(String tmp:n){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(num != null){
                                    str = str + "num. ";
                                    for(String tmp:num){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(prep != null){
                                    str = str + "prep. ";
                                    for(String tmp:prep){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(pron != null){
                                    str = str + "pron. ";
                                    for(String tmp:pron){
                                        str = str + tmp + '\n';
                                    }
                                }
                                if(v != null){
                                    str = str + "v. ";
                                    for(String tmp:v){
                                        str = str + tmp + '\n';
                                    }
                                }
                                Log.d("enDefinitions", str);
                                enDefinitions = str;

                                defn = shanbayresult.getData().getCnDefinition().getDefn();
                                Log.d("defn", defn);

                                uk = shanbayresult.getData().getPronunciations().getUk();
                                us = shanbayresult.getData().getPronunciations().getUs();
                                Log.d("uk", uk);
                                Log.d("us", us);
                                dst = "UK: " + "[" + uk + "]" +" US: " + "[" + us + "]" + '\n'
                                        + defn + '\n' + enDefinitions;
                                src = word;
                            }else{
                                src = statusCode + "";
                                dst = shanbayresult.getMsg();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //更新界面
                                    textview2.setText(dst);
                                }
                            });

                            if(src != "" && dst != "" && dst != statusCode + ""){
                                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                int sum = 0;
                                if(sharedPreferences.contains("sum")){
                                    sum = sharedPreferences.getInt("sum", 0);
                                }
                                editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                String key = (sum+1)+"";
                                editor.putString(key+"src",src);
                                editor.putString(key+"dst",dst);
                                editor.putInt("sum", sum+1);
                                editor.apply();
                            }
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {

                        }
                    });

                } else {
                    String q = editText1.getText().toString();
                    String from = selectLang1;
                    String to = selectLang2;
                    String appid = "20180212000122458";
                    final String salt = String.valueOf(System.currentTimeMillis());
                    String sign;
                    String securityKey = "G4GgX0B8HtfBYV7q9N9n";
                    String str = appid + q + salt + securityKey; // 加密前的原文
                    sign = MD5.md5(str);

                    try {
                        q = URLEncoder.encode(q, "utf-8");
                    } catch (UnsupportedEncodingException e) {

                    }
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://api.fanyi.baidu.com/api/trans/vip/translate?" + "q=" + q + "&from=" + from + "&to=" + to + "&appid=" + appid + "&salt=" + salt + "&sign=" + sign)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            String raw = response.body().string();
                            Log.d("aa", raw);
                            try {
                                Gson g = new Gson();
                                baiduTranslate bdt = g.fromJson(raw, baiduTranslate.class);
                                List<baiduTranslate.trans_result> transResultList = bdt.trans_result;
                                src = "";
                                dst = "";
                                for (int i = 0; i < transResultList.size(); i++) {
                                    dst = dst + transResultList.get(i).dst;
                                    if (i != transResultList.size() - 1) {
                                        dst = dst + '\n';
                                    }
                                    src = src + transResultList.get(i).src + '\n';
                                }

                            } catch (Exception e) {
                                Log.d("code", dst);
                                dst = "";
                                src = "";
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //更新界面
                                    textview2.setText(dst);
                                }
                            });

                            if(src != "" && dst != ""){
                                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                int sum = 0;
                                if(sharedPreferences.contains("sum")){
                                    sum = sharedPreferences.getInt("sum", 0);
                                }
                                editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                String key = (sum+1)+"";
                                editor.putString(key+"src",src);
                                editor.putString(key+"dst",dst);
                                editor.putInt("sum", sum+1);
                                editor.apply();
                            }
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {

                        }
                    });
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setSource = Integer.valueOf(prefs.getString("sourceListPreference", "1")).intValue();
        if(setSource == 2){
            spinner1.setVisibility(View.INVISIBLE);
            spinner2.setVisibility(View.INVISIBLE);
        }else{
            spinner1.setVisibility(View.VISIBLE);
            spinner2.setVisibility(View.VISIBLE);
        }
        if(isNeedTransparent){
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        杀掉进程
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_his){
            Intent intent = new Intent(MainActivity.this, hisActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.action_setting){
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.action_exit){
            stopService(startService);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }

}
