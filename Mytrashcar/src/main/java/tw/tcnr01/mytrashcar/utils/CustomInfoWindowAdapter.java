package tw.tcnr01.mytrashcar.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import tw.tcnr01.mytrashcar.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Activity activity;

    public CustomInfoWindowAdapter(Activity activity){
        this.activity = activity;//存起來
    }
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        // 依指定layout檔，建立地標訊息視窗View物件
        // --------------------------------------------------------------------------------------
        // 單一框
         View infoWindow=
         activity.getLayoutInflater().inflate(R.layout.custom_info_content,
         null);
        // 有指示的外框
        infoWindow.setAlpha(0.8f);
        // ----------------------------------------------
        // 顯示地標title
        TextView title = ((TextView) infoWindow.findViewById(R.id.title));
        title.setText(marker.getTitle());
        // 顯示地標snippet
        TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
        snippet.setText(marker.getSnippet());
        return infoWindow;
    }
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        Toast.makeText(activity.getApplicationContext(), "getInfoContents", Toast.LENGTH_LONG).show();
        return null;
    }
}