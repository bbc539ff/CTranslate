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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
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
import java.util.List;

import site.yvo11.ctranslate.Shanbay.Shanbay;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by yvo11 on 2018/2/22.
 */

public class ClipboardService extends Service {
    CancelReceiver cancelReceiver;
    NextReceiver nextReceiver;
    StartReceiver startReceiver;
    ReplyReceiver replyReceiver;
    boolean reciteMode = false;
    private static final String KEY_TEXT_REPLY = "key_text_reply";

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }


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

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("site.yvo11.ctranslate.next");
        nextReceiver = new NextReceiver();
        registerReceiver(nextReceiver, intentFilter2);

        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("site.yvo11.ctranslate.start");
        startReceiver = new StartReceiver();
        registerReceiver(startReceiver, intentFilter3);

        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("site.yvo11.ctranslate.reply");
        replyReceiver = new ReplyReceiver();
        registerReceiver(replyReceiver, intentFilter4);

        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        reciteMode = sharedPreferences.getBoolean("reciteMode", false);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(cancelReceiver);
        unregisterReceiver(nextReceiver);
        unregisterReceiver(startReceiver);
        unregisterReceiver(replyReceiver);
    }

    @Override
    public int onStartCommand(Intent intentt, int flags, int startId) {

        if(setNotification == true && reciteMode == false){
            if(Build.VERSION.SDK_INT >= 24){
                String replyLabel = "...";
                RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                        .setLabel(replyLabel)
                        .build();
                PendingIntent replyPendingIntent =
                        PendingIntent.getBroadcast(getApplicationContext(),
                                0,
                                new Intent("site.yvo11.ctranslate.reply"),
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action action =
                        new NotificationCompat.Action.Builder(R.drawable.cc_noti,
                                "Search", replyPendingIntent)
                                .addRemoteInput(remoteInput)
                                .build();

                if(Build.VERSION.SDK_INT >= 26) {
                    NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    NotificationChannel notificationChannel = new NotificationChannel("default", "C翻译", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.enableLights(false);
                    notificationChannel.setSound(null, null);
                    notificationChannel.setDescription("Channel description");
                    notificationManager.createNotificationChannel(notificationChannel);

                    Intent intent = new Intent("site.yvo11.ctranslate.start");
                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                    Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                    PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), "default")
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                            .setContentTitle(selectLang1+" -> "+selectLang2)
                            .setSmallIcon(R.drawable.cc_noti)
                            .setContentIntent(pi)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("None..."))
                            .addAction(action)
                            .addAction(R.drawable.ic_launcher_foreground, "Cancel",
                                    cPi)
                            .build();
                    startForeground(1, notification);

                }else if(Build.VERSION.SDK_INT < 26 && Build.VERSION.SDK_INT >=24){
                    Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                    PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                    Intent intent = new Intent("site.yvo11.ctranslate.start");
                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                    Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                            .setContentTitle(selectLang1+" -> "+selectLang2)
                            .setSmallIcon(R.drawable.cc_noti)
                            .setContentIntent(pi)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("None..."))
                            .addAction(action)
                            .addAction(R.drawable.ic_launcher_foreground, "Cancel",
                                    cPi)
                            .build();
                    startForeground(1, notification);
                }
            } else {
                Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                Intent intent = new Intent("site.yvo11.ctranslate.start");
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                        .setContentTitle(selectLang1+" -> "+selectLang2)
                        .setSmallIcon(R.drawable.cc_noti)
                        .setContentIntent(pi)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("None..."))
                        .addAction(R.drawable.ic_launcher_foreground, "Cancel",
                                cPi)
                        .build();
                startForeground(1, notification);
            }
        }

        if(reciteMode == true){
            Intent nextInitIntent  = new Intent("site.yvo11.ctranslate.next");
            sendBroadcast(nextInitIntent);
        }

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            private long previousTime = 0;
            @Override
            public void onPrimaryClipChanged() {
                long now = System.currentTimeMillis();
                if(now-previousTime < 200){
                    previousTime = now;
                    return;
                }
                previousTime = now;
                ClipData clipData = clipboardManager.getPrimaryClip();
                String text = clipData.getItemAt(0).getText().toString();
                trans(text);
            }
        });

        return super.onStartCommand(intentt, flags, startId);
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

    String word;


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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int setSource = Integer.valueOf(prefs.getString("sourceListPreference", "1")).intValue();
        if(setSource == 2){
            word = text;
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
                    runNotification();


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

        } else{
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

                    runNotification();


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
    void runNotification(){
        new Thread(){
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(setToast == true){
                            Toast.makeText(getApplicationContext(), dst, Toast.LENGTH_LONG).show();
                        }

                        if(setNotification == true && reciteMode == false){
                            if(Build.VERSION.SDK_INT >= 24){
                                String replyLabel = "...";
                                RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                                        .setLabel(replyLabel)
                                        .build();
                                PendingIntent replyPendingIntent =
                                        PendingIntent.getBroadcast(getApplicationContext(),
                                                0,
                                                new Intent("site.yvo11.ctranslate.reply"),
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Action action =
                                        new NotificationCompat.Action.Builder(R.drawable.cc_noti,
                                                "Search", replyPendingIntent)
                                                .addRemoteInput(remoteInput)
                                                .build();

                                if(Build.VERSION.SDK_INT >= 26) {
                                    NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                    NotificationChannel notificationChannel = new NotificationChannel("default", "C翻译", NotificationManager.IMPORTANCE_DEFAULT);
                                    notificationChannel.enableLights(false);
                                    notificationChannel.setSound(null, null);
                                    notificationChannel.setDescription("Channel description");
                                    notificationManager.createNotificationChannel(notificationChannel);

                                    Intent intent = new Intent("site.yvo11.ctranslate.start");
                                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                                    Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                                    PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                                            .setContentTitle(src)
                                            .setSmallIcon(R.drawable.cc_noti)
                                            .setContentIntent(pi)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText("-> "+dst))
                                            .addAction(action)
                                            .addAction(R.drawable.ic_launcher_foreground, "Cancel",
                                                    cPi)
                                            .build();
                                    startForeground(1, notification);

                                }else if(Build.VERSION.SDK_INT < 26 && Build.VERSION.SDK_INT >=24){
                                    Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                                    PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                                    Intent intent = new Intent("site.yvo11.ctranslate.start");
                                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                                    Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                            .setContentTitle(src)
                                            .setSmallIcon(R.drawable.cc_noti)
                                            .setContentIntent(pi)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText("-> "+dst))
                                            .addAction(action)
                                            .addAction(R.drawable.ic_launcher_foreground, "Cancel",
                                                    cPi)
                                            .build();
                                    startForeground(1, notification);
                                }
                            } else {
                                Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                                PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                                Intent intent = new Intent("site.yvo11.ctranslate.start");
                                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                        .setContentTitle(src)
                                        .setSmallIcon(R.drawable.cc_noti)
                                        .setContentIntent(pi)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("-> "+dst))
                                        .addAction(R.drawable.ic_launcher_foreground, "Cancel",
                                                cPi)
                                        .build();
                                startForeground(1, notification);
                            }
                        }
                    }
                });
            }
        }.start();
    }

    class StartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("setStart", false);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
        }
    }

    class ReplyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence charSequence = getMessageText(intent);
            String text = charSequence.toString();
            trans(text);
        }
    }

    class CancelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopService(new Intent(getApplicationContext(), ClipboardService.class));
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    class NextReceiver extends BroadcastReceiver {
        int pos = 1;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int MAX = Integer.valueOf(prefs.getString("ListPreference", "50")).intValue();
        @Override
        public void onReceive(Context context, Intent intentt) {
            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            int sum = 0;
            if(sharedPreferences.contains("sum")){
                sum = sharedPreferences.getInt("sum", 0);
            }

            int top = sum - MAX + 1;
            if(top <= 0){
                top = 1;
            }
            String src = sharedPreferences.getString(pos+""+"src","");
            String dst = sharedPreferences.getString(pos+""+"dst","");

            if(Build.VERSION.SDK_INT >= 26) {
                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Channel description");
                notificationManager.createNotificationChannel(notificationChannel);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                Intent nIntent = new Intent("site.yvo11.ctranslate.next");
                PendingIntent nPi = PendingIntent.getBroadcast(getApplicationContext(), 0, nIntent, 0);

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "default")
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                        .setContentTitle(src)
                        .setSmallIcon(R.drawable.cc_noti)
                        .setContentIntent(pi)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("-> "+dst))
                        .addAction(R.mipmap.ic_launcher, "Next",
                                nPi)
                        .addAction(R.mipmap.ic_launcher, "Cancel",
                                cPi)
                        .build();
                startForeground(1, notification);

            }else{
                Intent cIntent = new Intent("site.yvo11.ctranslate.cancel");
                PendingIntent cPi = PendingIntent.getBroadcast(getApplicationContext(), 0, cIntent, 0);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                Intent nIntent = new Intent("site.yvo11.ctranslate.next");
                PendingIntent nPi = PendingIntent.getBroadcast(getApplicationContext(), 0, nIntent, 0);
                Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                                                .setContentTitle(selectLang1+" -> "+selectLang2)
                        .setContentTitle(src)
                        .setSmallIcon(R.drawable.cc_noti)
                        .setContentIntent(pi)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("-> "+dst))
                        .addAction(R.mipmap.ic_launcher, "Next",
                                nPi)
                        .addAction(R.mipmap.ic_launcher, "Cancel",
                                cPi)
                        .build();
                startForeground(1, notification);
            }
            if(pos < sum){
                pos++;
            }
        }
    }
}
