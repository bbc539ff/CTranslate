package site.yvo11.ctranslate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yvo11 on 2018/2/21.
 */

public class hisActivity extends AppCompatActivity {
    private List<String> dataList = new ArrayList<String>();
    ClipboardAdapter recAdapter;
    int MAX = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        MAX = Integer.valueOf(prefs.getString("ListPreference", "50")).intValue();

        Log.d("aaaaaa",MAX+"");

        RecyclerView recyclerView = findViewById(R.id.his_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        int sum = 0;
        if(sharedPreferences.contains("sum")){
            sum = sharedPreferences.getInt("sum", 0);
        }
        int start = sum - MAX + 1;
        if(start <= 0){
            start = 1;
        }
        for(int i = sum;i>=start;i--){
            String str = sharedPreferences.getString(i+"","");
            dataList.add(str);
        }

        recAdapter = new ClipboardAdapter(dataList);
        recyclerView.setAdapter(recAdapter);
    }
}
