package com.example.rla12.prography;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private List<HashMap<String, String>> jlist = null;

    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions marker1;
    MarkerOptions marker2;
    Spinner spinner;
    DataDown task; // json 받아오기
    ArrayList<MarkerData> mlist;
   TextView temp;
    LatLng position;
    LocationManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = (TextView)findViewById(R.id.tvtext);


        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map); // 일종의 틀이야
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("MainActivity", "GoogleMap 객체가 준비 됨");

                map = googleMap;

                //데이터 가져오기
                task = new DataDown();

               task.execute("http://111.222.333.444:8080/TestPrography/dbTest.jsp") ; // 테스트 환경 구축 후 해당 서버 ip 적기 localhost로 하면 안 됨.


            }
        }); // 객체를 뽑아내는 것 콜백 함수 통해 전달해줌


        //예전 단말에 문제가 있을때 사용했던 방법

        MapsInitializer.initialize(this);

        //버튼 클릭시 동작하는 내용 넣어줌

        Button button = (Button)findViewById(R.id.btncheck);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMyLocation();
            }
        });


        //background thread
        Thread th = new Thread();
        th.start();


        // spinner 삽입
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    @Override
    protected void onPause() { // 중지
        super.onPause();

        if(map != null)
            map.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(map != null)
            map.setMyLocationEnabled(true);
    }


    private Marker setaddMarker(MarkerData makerData){

        position = new LatLng(makerData.getLat(),makerData.getLon());
        Log.i("position 확인",makerData.getLat()+"ff");
        String name = makerData.getName();

        marker2 = new MarkerOptions();
        Log.i("marker2","생성 yes");
        marker2.title(name);
        marker2.position(position);
        marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
        Log.i("marker2","내용 넣음");
        return map.addMarker(marker2);
    }

    public void requestMyLocation(){

        long minTime = 10000;
        float minDistance = 0;
        manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        showCurrentLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                }

        );
        /*Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation != null) {
            showCurrentLocation(lastLocation);
        }

        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        showCurrentLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }
        );*/

    }
    public  void showCurrentLocation(Location location){
        LatLng curPoint = new LatLng(location.getLatitude(),location.getLongitude());

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        showMarker(location);

    }


    public void showMarker(Location location) {
        if (marker1 == null) {
            marker1 = new MarkerOptions(); // 객체 생성 && 맵에 추가 가능
            marker1.position(new LatLng(location.getLatitude(), location.getLongitude())); // 값을 조정할 것.
            marker1.title("커피숍");
            marker1.snippet("커피숍에 대한 설명입니다.");
            marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));

            map.addMarker(marker1); // 위치에 대한 정보를 넣어줘야해
            /// 이렇게 해서 추가하는 작업 완료
        } else {
            marker1.position(new LatLng(location.getLatitude(), location.getLongitude())); // 값을 조정할 것.
        }
    }


    class DataDown extends AsyncTask<String,ArrayList<MarkerData>,ArrayAdapter> {
        ArrayAdapter adapter;
        JSONObject jsonObject;
        JSONArray jsonArray;

        @Override
        protected ArrayAdapter doInBackground(String... urls) {

            StringBuffer jsonHtml = new StringBuffer();
            String jsonjtml = new String();
            boolean jsonContext = false;
            int count = 0;

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn == null)
                    Log.i("err1", "Connection Error");

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while (true) {
                            String line = bufferedReader.readLine();
                            String changeLine = new String(line.getBytes("8859_1"), "UTF-8");
                            String Sline="" ;
                            Log.i("changsetting11", changeLine);

                            if (line == null) break;
                            if (changeLine.length() > 7) {
                                jsonContext = true;
                                Log.i("changsetting22222", changeLine);

                                jsonjtml=changeLine.toString(); // string 배열로 선언함
                                Sline=line;
                                continue;

                            }
                            if (jsonContext) {
                                Log.i("changing_jsonhtml", jsonjtml+"111");
                                Log.i("changing11_sline", Sline+"dddkjdkdj");
                                jsonContext = false;

                            }
                        }
                        bufferedReader.close();

                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Log.i("hererererererererererer", "ok");
                String json = jsonjtml;
                jsonArray = new JSONArray();
                jsonObject = new JSONObject(json);
               Log.i("jsoncheck1_222","jsonArray 만듬");
                for(int i=0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                }
                Log.i("jsoncheck222",jsonObject.length()+"ddd");
                JSONParser parser = new JSONParser();
                JSONParser.parse(jsonObject);
                Log.i("hererererere2222222222", "ok");
            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }

            JSONParser parser = new JSONParser();

            try {
                jlist = parser.parse(jsonObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            mlist = new ArrayList<MarkerData>();
            String[] from = {"tvname", "laitude", "longitude"};
           Log.i("데이터 파싱되어 넘어 왔는지 확인",jlist.size()+" 개 넘어 옴");

                for(int i =0 ; i< 4;i++){
                    MarkerData data1 = new MarkerData();
                    data1.setName(jlist.get(i).get("tvname"));
                    data1.setLat(Double.parseDouble(jlist.get(i).get("laitude")));
                    data1.setLon(Double.parseDouble(jlist.get(i).get("longitude")));
                    mlist.add(data1);
                }
            try {

                Log.i("mlist 내용 확인",mlist.size()+"개 탑재중");

                adapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,mlist);

                publishProgress(mlist);

            }catch (Exception e){
                e.printStackTrace();
            }
            return adapter;
        }

        @Override
        protected void onProgressUpdate(ArrayList... lists) {
            super.onProgressUpdate(lists);
            for(int i =0;i<mlist.size();i++){
                MarkerData item = mlist.get(i);
                Log.i("marker","marker에 데이터 삽입"+item.getName());
                position = new LatLng(item.getLat(),item.getLon());

                map.addPolyline(new PolylineOptions().add(position));
                map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(position).zoom(13).build()));

                setaddMarker(item);
            }

        }

        protected void onPostExecute(ArrayAdapter adapter){

            TextView tvtext = (TextView)findViewById(R.id.tvtext);
           spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, "선택된 아이템 이름"+ mlist.get(position).getLon(),Toast.LENGTH_SHORT).show();
                    temp.setText(mlist.get(position).getName());

                    //   settingSpinner(list);
                    // spinner list 선택시 카메라 view가 넘어가도록 하기
                    // spinner 리스트의 내용 이름을 tvname으로 하기
                    //spinner Itemselected...  사용하면 될듯
                    //현재 위치 버튼 클릭 이벤트를 spinner 이벤트로 옮기기

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

        });

        }

    }
}
