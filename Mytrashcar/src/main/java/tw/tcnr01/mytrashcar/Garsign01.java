package tw.tcnr01.mytrashcar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

import tw.tcnr01.mytrashcar.utils.GetTrashData;
import tw.tcnr01.mytrashcar.utils.SelectLocationListener;


public class Garsign01 extends AppCompatActivity implements View.OnClickListener {

    private ListView gslist001;
    private ArrayAdapter<String> adapter;
    private TextView t_title;
    private String check_t = null;
    private TableRow gstab01;
    private Spinner s001, s002;
    private String m_Response;
    private TextView gs_t001, gs_t002, gs_t003, gs_t004, gs_t005;
    private Uri uri;
    private Intent its;
    private ImageButton searchcar;
    private LocationManager manager;
    private String TAG = "tcnr01=>";
    private LocationManager locationManager;
    private GetTrashData.LOCATION focusLocation = GetTrashData.LOCATION.TAICHUNG;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    //-----------------所需要申請的權限數組---------------
    private static final String[] permissionsArray = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private List<String> permissionsList = new ArrayList<String>();
    //申請權限後的返回碼
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    //---------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkRequiredPermission(this);     //  檢查SDK版本, 確認是否獲得權限.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garsign01);//把畫面跟程式碼綁一起
        // 取得系統服務的LocationManager物件
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        u_checkgps();//檢查GPS是否開啟
        setupViewComponent();
    }

    private void u_checkgps() {
        // 取得系統服務的LocationManager物件
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 檢查是否有啟用GPS
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 顯示對話方塊啟用GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("定位管理")
                    .setMessage("GPS目前狀態是尚未啟用.\n"
                            + "請問你是否現在就設定啟用GPS?")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 使用Intent物件啟動設定程式來更改GPS設定
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("不啟用", null).create().show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void checkRequiredPermission(Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void setupViewComponent() {
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5
        //---------------------------------------
        gslist001 = (ListView) findViewById(R.id.gs_list001);
        searchcar = (ImageButton) findViewById(R.id.gs_search001);
        gslist001.getLayoutParams().height = newscrollheight;
        gslist001.setLayoutParams(gslist001.getLayoutParams()); // 重定ScrollView大小
        gstab01 = (TableRow) findViewById(R.id.gs_tab01);
        gs_t001 = (TextView) findViewById(R.id.gs_t001);
        gs_t002 = (TextView) findViewById(R.id.gs_t002);
        gs_t003 = (TextView) findViewById(R.id.gs_t003);
        gs_t004 = (TextView) findViewById(R.id.gs_t004);
        gs_t005 = (TextView) findViewById(R.id.gs_t005);
        searchcar.setOnClickListener(this);
        s001 = (Spinner) findViewById(R.id.city_s001);
        s002 = (Spinner) findViewById(R.id.city_s002);
        //初始顯示spinner值
        ArrayAdapter<CharSequence> s001adt = ArrayAdapter.createFromResource(this, R.array.city_a001, android.R.layout.simple_spinner_item);
        s001adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s001.setAdapter(s001adt);
        s001.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter spinzoneAdapter = null;
                Log.e("ppos", String.valueOf(position));
                switch (position) {
                    case 0://台中
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a001, android.R.layout.simple_spinner_item);
                        gs_t001.setText(getString(R.string.t001));
                        gs_t002.setText(getString(R.string.t002));
                        gs_t003.setText(getString(R.string.t003));
                        gs_t004.setText(getString(R.string.t004));
                        gs_t005.setText(getString(R.string.t005));
                        focusLocation = GetTrashData.LOCATION.TAICHUNG;
                        s002.setVisibility(View.VISIBLE);
                        s002.setEnabled(true);
                        searchcar.setVisibility(View.VISIBLE);
                        break;
                    case 1://澎湖
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a003, android.R.layout.simple_spinner_item);
                        gs_t001.setText(getString(R.string.t011));
                        gs_t002.setText(getString(R.string.t012));
                        gs_t003.setText(getString(R.string.t013));
                        gs_t004.setText(getString(R.string.t014));
                        gs_t005.setText(getString(R.string.t015));
                        focusLocation = GetTrashData.LOCATION.PENGHU;
                        s002.setVisibility(View.VISIBLE);
                        searchcar.setVisibility(View.INVISIBLE);
                        break;
                    case 2://新竹
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a004, android.R.layout.simple_spinner_item);
                        gs_t001.setText(getString(R.string.t001));
                        gs_t002.setText(getString(R.string.t023));
                        gs_t003.setText(getString(R.string.t022));
                        gs_t004.setText(getString(R.string.t024));
                        gs_t005.setText(getString(R.string.t025));
                        focusLocation = GetTrashData.LOCATION.HSINCHU;
                        s002.setVisibility(View.INVISIBLE);
                        searchcar.setVisibility(View.INVISIBLE);
                        break;
                }
                if (null != spinzoneAdapter) {
                    spinzoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s002.setAdapter(spinzoneAdapter);
                    s002.setOnItemSelectedListener(new SelectLocationListener(Garsign01.this
                            , gslist001, focusLocation));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    //-----------------------選單---------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_notify:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.menu_notify)
                        .setMessage(getString(R.string.menu_message))
                        .setCancelable(false)
                        .setIcon(R.drawable.icplaystore)
                        .setPositiveButton(R.string.menu_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.menu_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;

            case R.id.menu_member:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.menu_member)
                        .setMessage(getString(R.string.menu_member_message) + "\n" + "維尼、大神、佳佳、波波、柏榕、老大、培揚")
                        .setCancelable(false)
                        .setIcon(R.drawable.icplaystore)
                        .setPositiveButton(R.string.menu_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.menu_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;
            case R.id.menu_logout:
                this.finish();
                break;
//            case R.id.menu_search:
//                Intent intent = new Intent(Garsign01.this, TrashcarMapsActivity.class);//高級寫法
//                intent.putExtra("location", focusLocation.name());//因為是抓String 所以加上name才能吃到focusLocation這個enum自己創的變數
//                startActivity(intent);
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.gs_search001:
                Intent intent = new Intent(Garsign01.this, TrashcarMapsActivity.class);//高級寫法
                intent.putExtra("location", focusLocation.name());//因為是抓String 所以加上name才能吃到focusLocation這個enum自己創的變數
                startActivity(intent);
                break;
        }
    }
}


