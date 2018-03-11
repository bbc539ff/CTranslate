package site.yvo11.ctranslate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;


/**
 * Created by yvo11 on 2018/2/22.
 */

public class ClipboardService extends Service {
    String text;
    Boolean exit = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("site.yvo11.ctranslate.cancel");
        cancelReceiver = new CancelReceiver();
        registerReceiver(cancelReceiver, intentFilter);
        exit = false;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(cancelReceiver);
        exit = true;
    }
    CancelReceiver cancelReceiver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(setNotification == true){
            if(Build.VERSION.SDK_INT >= 26) {
                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Channel description");
                notificationManager.createNotificationChannel(notificationChannel);

                Intent intentM = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intentM, 0);

                Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                        .setContentTitle(selectLang1+" -> "+selectLang2)
                        .setSmallIcon(R.drawable.cc_noti)
                        .setContentIntent(pi)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("None..."))
                        .addAction(R.mipmap.ic_launcher, "Cancel",
                                cPi)
                        .build();
                startForeground(1, notification);

            }else{
                Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                Intent intentM = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intentM, 0);
                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(selectLang1+" -> "+selectLang2)
                        .setSmallIcon(R.drawable.cc_noti)
                        .setContentIntent(pi)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("None..."))
                        .addAction(R.mipmap.ic_launcher, "Cancel",
                                cPi)
                        .build();
                startForeground(1, notification);
            }
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        String temp = "";
                        if(clipboardManager.hasPrimaryClip()){
                            ClipData clipData = clipboardManager.getPrimaryClip();
                            temp = clipData.getItemAt(0).getText().toString();
                        }

                        while(!exit){
                            if(clipboardManager.hasPrimaryClip()){
                                ClipData clipData = clipboardManager.getPrimaryClip();
                                if(clipData != null) {
                                    text = clipData.getItemAt(0).getText().toString();
                                    if (!temp.equals(text)) {
                                        temp = text;
                                        Log.d(temp, text);
                                        trans(text);
                                    }
                                }
                            }
                        }
                    }
                });
                Looper.loop();

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private static ClipboardService instance;
    public static ClipboardService getInstance() {
        return instance;
    }

    String dst = "";
    String src = "";
    String selectLang1 = "auto";
    String selectLang2 = "zh";
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_s;
    Handler mHandler = new Handler();


    Boolean setToast = true; // default
    Boolean setNotification = true; // default


    void trans(final String text){
        editor_s = getSharedPreferences("setting", MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences1 = getSharedPreferences("setting", MODE_PRIVATE);
        if(sharedPreferences1.contains("selectLang1")){
            int pos = sharedPreferences1.getInt("selectLang1", 0);
            String[] languages = getResources().getStringArray(R.array.languages);
            selectLang1 = languages[pos];
        }
        if(sharedPreferences1.contains("selectLang2")){
            int pos = sharedPreferences1.getInt("selectLang2", 0);
            String[] languages = getResources().getStringArray(R.array.languages2);
            selectLang2 = languages[pos];
        }


        setToast = sharedPreferences1.getBoolean("setToast", true);
        setNotification = sharedPreferences1.getBoolean("setNotification", true);


        String q = text;
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
                String Raw = response.body().string();
                Log.d("aa", Raw);
                try {
                    Gson g = new Gson();
                    baiduTranslate bdt = g.fromJson(Raw, baiduTranslate.class);
                    List<baiduTranslate.trans_result> transResultList = bdt.trans_result;
                    dst = "";
                    src = "";
                    for (int i = 0; i < transResultList.size(); i++) {
                        dst = dst + transResultList.get(i).dst;
                        if(i != transResultList.size()-1){
                            dst = dst + '\n';
                        }
                        src = src + transResultList.get(i).src + '\n';
                    }

                } catch (Exception e) {
                    Log.d("code", src);
                    src = "";
                    dst = "";
                }

                new Thread(){
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(setToast == true){
                                    Toast.makeText(getApplicationContext(), dst, Toast.LENGTH_LONG).show();
                                }
                                if(setNotification == true){
                                    if(Build.VERSION.SDK_INT >= 26) {
                                        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                        NotificationChannel notificationChannel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
                                        notificationChannel.setDescription("Channel description");
                                        notificationManager.createNotificationChannel(notificationChannel);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                                        Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                                        PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "default")
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                                                .setContentTitle(src)
                                                .setSmallIcon(R.drawable.cc_noti)
                                                .setContentIntent(pi)
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText("-> "+dst))
                                                .addAction(R.mipmap.ic_launcher, "Cancel",
                                                        cPi)
                                                .build();
                                        startForeground(1, notification);

                                    }else{
                                        Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                                        PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                                                .setContentTitle(src)
                                                .setSmallIcon(R.drawable.cc_noti)
                                                .setContentIntent(pi)
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText("-> "+dst))
                                                .addAction(R.mipmap.ic_launcher, "Cancel",
                                                 cPi)
                                                .build();
                                        startForeground(1, notification);
                                    }

                                }

                            }
                        });
                    }
                }.start();



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
    class CancelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopService(new Intent(getApplicationContext(), ClipboardService.class));
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
}
