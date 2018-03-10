package site.yvo11.ctranslate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
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


public class MainActivity extends AppCompatActivity {
    Intent startService;
    EditText editText1;
    TextView textview2;
    Handler handler;
    String str = "";
    String src_t = "";
    Spinner spinner1;
    Spinner spinner2;
    String selectLang1 = "";
    String selectLang2 = "";
    Button tButton;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
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


        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = editText1.getText().toString();
                String from = selectLang1;
                String to = selectLang2;
                String appid = "20180212000122458";
                final String salt = String.valueOf(System.currentTimeMillis());
                String sign;
                String securityKey = "G4GgX0B8HtfBYV7q9N9n";
                String src = appid + q + salt + securityKey; // 加密前的原文
                sign = MD5.md5(src);

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
                        str = response.body().string();
                        Log.d("aa", str);
                        try {
                            Gson g = new Gson();
                            baiduTranslate bdt = g.fromJson(str, baiduTranslate.class);
                            List<baiduTranslate.trans_result> transResultList = bdt.trans_result;
                            str = "";
                            src_t = "";
                            for (int i = 0; i < transResultList.size(); i++) {
                                str = str + transResultList.get(i).dst;
                                if(i != transResultList.size()-1){
                                    str = str + '\n';
                                }
                                src_t = src_t + transResultList.get(i).src + '\n';
                            }

                        } catch (Exception e) {
                            Log.d("code", str);
                            str = "";
                            src_t = "";
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //更新界面
                                textview2.setText(str);
                            }
                        });

                        if(str != "" && src_t != ""){
                            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            int sum = 0;
                            if(sharedPreferences.contains("sum")){
                                sum = sharedPreferences.getInt("sum", 0);
                            }
                            editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            String key = (sum+1)+"";
                            editor.putString(key, "src:"+'\n'+src_t +"dst:"+'\n'+ str);
                            editor.putInt("sum", sum+1);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(startService);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }
}
