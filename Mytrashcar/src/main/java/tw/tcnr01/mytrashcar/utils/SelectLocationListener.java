package tw.tcnr01.mytrashcar.utils;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import tw.tcnr01.mytrashcar.R;

public class SelectLocationListener implements AdapterView.OnItemSelectedListener {
    private Activity activity;
    private ListView gslist001;
    private GetTrashData.LOCATION location;

    public SelectLocationListener(Activity activity, ListView gslist001, GetTrashData.LOCATION location) {
        this.activity = activity;//存起來
        this.gslist001 = gslist001;
        this.location = location;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.d("onItemSelected: ", String.valueOf(id));
        String[] taichungLocationList = activity.getResources().getStringArray(R.array.area_a001);
//        Log.d("Location", taichungLocationList[position]); // 北屯區、中區...

        GetTrashData.setDataToListview(activity, gslist001, location, taichungLocationList[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}