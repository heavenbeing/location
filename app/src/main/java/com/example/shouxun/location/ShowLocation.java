package com.example.shouxun.location;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.jar.Manifest;

/**
 * Created by shouxun on 2018/1/12.
 */

public class ShowLocation extends AppCompatActivity {
    private String provider;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;
    private Button btn_show;
    private TextView tv_show;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
        init();//关联控件
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
        provider = judgeProvider(locationManager);

        //有位置提供器
        if (provider != null) {
            //限制getLastKnownLocation方法警告
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                return;
            }
        }

    }

    private void init() {
        btn_show = (Button) findViewById(R.id.btn_show_location);
        tv_show = (TextView) findViewById(R.id.tv_location_show);
    }

    //判断是否有可用的内容提供器
    private String judgeProvider(LocationManager locationManager) {
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(ShowLocation.this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
