package tw.tcnr01.mytrashcar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static tw.tcnr01.mytrashcar.utils.GetTrashData.setDataToGoogleMap;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import tw.tcnr01.mytrashcar.databinding.ActivityTrashcarMapsBinding;
import tw.tcnr01.mytrashcar.utils.GetTrashData;

public class TrashcarMapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityTrashcarMapsBinding binding;
    String TAG = "tcnr01=>";
    private Marker markerMe;
    float Anchor_x = 0.6f;
    float Anchor_y = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrashcarMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//
//        MarkerOptions markerOpt = new MarkerOptions();
//
//        markerMe = mMap.addMarker(markerOpt);
        mMap = googleMap;
//        mUiSettings = map.getUiSettings();//
//        開啟 Google Map 拖曳功能
        mMap.getUiSettings().setScrollGesturesEnabled(true);

//    右下角的導覽及開起googlemap功能
        mMap.getUiSettings().setMapToolbarEnabled(true);

//     左上角顯示指北針，要兩指旋轉才出現
        mMap.getUiSettings().setCompassEnabled(true);

//    右下角顯示縮放按鈕的放大縮小功能
        mMap.getUiSettings().setZoomControlsEnabled(true);

        String location = getIntent().getStringExtra("location");//檢驗
        if (null == location) {
            Toast.makeText(this, "Something went wrong ! Location not found !", Toast.LENGTH_SHORT).show();
            return;//early return 就不會往下跑
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getDataFromAPI(location);
            }
        }, 0, 60000);

        //----------取得定位許可-----------------------
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //----顯示我的位置ICO-------
            Toast.makeText(getApplicationContext(), "GPS定位權限未允許", Toast.LENGTH_LONG).show();
        } else {
            //----顯示我的位置ICO-------
            mMap.setMyLocationEnabled(true);
            return;
        }
    }

    //*** 增加 Marker 監聽 使用Animation動畫*/
    @Override
    public boolean onMarkerClick(final Marker marker_Animation) {
        if (!marker_Animation.getTitle().substring(0, 4).equals("Move")) {
            //非GPS移動位置;設定動畫
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500; //連續時間
            final Interpolator interpolator = new BounceInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                    marker_Animation.setAnchor(Anchor_x, Anchor_y + 2 * t); //設定標的位置
                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        } else {//GPS移動位置,不使用動畫
            TrashcarMapsActivity.this.markerMe.hideInfoWindow();
        }
        return false;
    }

    private void getDataFromAPI(String location) {
        runOnUiThread(() -> {
            GetTrashData.LOCATION focusLocation = GetTrashData.LOCATION.valueOf(location);
            LatLng locationLatLng;
            switch (focusLocation) {
                case HSINCHU:
                    locationLatLng = new LatLng(24.840168872835232, 121.00953756776431);
                    Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();
                    break;
                case PENGHU:
                    locationLatLng = new LatLng(23.584269524454367, 119.58198096937105);
                    Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    locationLatLng = new LatLng(24.15470473208519, 120.6536969352455);
                    break;
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 14)); //移動位置上
            setDataToGoogleMap(this, mMap, focusLocation);
        });
    }
}