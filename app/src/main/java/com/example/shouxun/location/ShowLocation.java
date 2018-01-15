package com.example.shouxun.location;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                getLocation(location);//得到当前经纬度并开启线程反向地理编码
            } else {
                tv_show.setText("暂时无法获得当前位置");
            }
        }
    }

    //初始化绑定
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

    public void getLocation(Location location) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
        //百度地图gps反向位置接口
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=ZfHtMHY7FGolEirx3yCNjGWzQtyufVWC&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        new MyAsyncTask(url).execute();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        String url = null;//要请求的网址
        String str = null;//服务器返回数据
        String address = null;

        public MyAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        //调接口
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                str = str.replace("renderReverse&&renderReverse", "");
                str = str.replace("(", "");
                str = str.replace(")", "");
                JSONObject jsonObject = new JSONObject(str);
                JSONObject address = jsonObject.getJSONObject("result");
                String city = address.getString("formatted_address");
                String district = address.getString("sematic_description");
                tv_show.setText("当前位置:" + city + district);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }

    }

}
