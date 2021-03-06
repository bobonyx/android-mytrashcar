package tw.tcnr01.mytrashcar.utils;

import static tw.tcnr01.mytrashcar.utils.CommonUtils.showProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ListView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import tw.tcnr01.mytrashcar.R;

public class GetTrashData {
    public enum LOCATION {
        TAICHUNG, PENGHU, HSINCHU
    }

    public static void setDataToGoogleMap(Activity activity, GoogleMap googleMap, LOCATION location) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3";
        switch (location) {
            case TAICHUNG:
                url = "https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3";
//                keys = new String[]{"car", "time", "location", "X", "Y"};
                break;
            case PENGHU:
                url = "https://loshenghuan.tcnrcloud110a.com/PENGHU.txt";
                break;
            case HSINCHU:
                url = "https://odws.hccg.gov.tw/001/Upload/25/opendata/9059/165/f91f9475-42b8-407c-89d3-f0dd5dc2e2f8.json";
                break;
        }
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            private float Anchor_x = 0.6f;
            private float Anchor_y = 0.6f;
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    activity.runOnUiThread(() -> {
                        googleMap.clear();
                    });
                    String result = response.body().string();
                    try {
                        JSONArray trashCarLocationList = new JSONArray(result);
                        for (int i = 0; i < trashCarLocationList.length(); i += 1) {
                            JSONObject carLocation = trashCarLocationList.getJSONObject(i);
                            double lan = carLocation.getDouble("Y");
                            double lon = carLocation.getDouble("X");
                            String carName = carLocation.getString("car");
                            String carTime = carLocation.getString("time");
                            String carStay = carLocation.getString("location");
//                            Log.e("lan", String.valueOf(lan));
//                            Log.e("lon", String.valueOf(lon));
                            activity.runOnUiThread(() -> {//????????????????????????????????????  ??????????????????????????????????????????????????????????????????
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lan, lon))
                                        .title("??????:" + carName)//????????????
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carcar1))
                                        .snippet(carTime + "\n" + carStay)
                                        .infoWindowAnchor(Anchor_x, Anchor_y)
                                );
                                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(activity));//????????????
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * ????????? ??????????????????
     *
     * @param activity
     * @param gslist001
     * @param location
     */
    public static void setDataToListview(Activity activity, ListView gslist001, LOCATION location) {
        setDataToListview(activity, gslist001, location, null);
    }

    /**
     * ??????filterLocation ??????????????????
     *
     * @param activity
     * @param gslist001
     * @param location
     * @param filterLocation
     */
    public static void setDataToListview(Activity activity, ListView gslist001, LOCATION location, String filterLocation) {
        //********??????????????????????????????*****************************
        ProgressDialog pd = showProgressDialog(activity);
        //***************************************************************
        OkHttpClient client = new OkHttpClient();

        String url = "https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3";
        String[] keys = new String[]{"car", "time", "location", "X", "Y"};
        switch (location) {
            case TAICHUNG:
                url = "https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3";
                keys = new String[]{"car", "time", "location", "X", "Y"};
                break;
            case PENGHU:
                url = "https://loshenghuan.tcnrcloud110a.com/PENGHU.txt";
                keys = new String[]{"??????", "?????????", "?????????", "????????????", "???????????????????????????"};
                break;
            case HSINCHU:
                url = "https://odws.hccg.gov.tw/001/Upload/25/opendata/9059/165/f91f9475-42b8-407c-89d3-f0dd5dc2e2f8.json";
                keys = new String[]{"??????", "??????????????????", "????????????????????????", "?????????_?????????", "?????????_?????????"};
                break;
        }
        Request request = new Request.Builder().url(url).build();

        String[] finalKeys = keys;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    activity.runOnUiThread(() -> {
                        int dataCount = ProcessData.process(activity, gslist001, result, finalKeys, filterLocation);
                        pd.cancel();
                    });
                }
            }
        });
    }
}
