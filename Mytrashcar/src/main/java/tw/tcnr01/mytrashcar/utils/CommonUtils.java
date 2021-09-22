package tw.tcnr01.mytrashcar.utils;

import android.app.ProgressDialog;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import tw.tcnr01.mytrashcar.R;

public class CommonUtils {
    /**
     * ?????
     * @param jsonArray
     * @param keyName
     * @return
     */
    public static JSONArray sortJsonArray(JSONArray jsonArray, String keyName) {//把相同的car號碼擺在一起
        ArrayList<JSONObject> jsons = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsons.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {//做排序
            @Override
            public int compare(JSONObject t1, JSONObject t2) {
                String lid = "";
                String rid = "";
                try {
                    lid = t1.getString(keyName);//1-2 2-3 3-4
//                    Log.e("lid", lid);
                    rid = t2.getString(keyName);
//                    Log.e("rid", rid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

    /**
     * ?????
     * @param context
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//progressbox  跳出視窗顯示
        pd.setTitle("連結伺服器");
        pd.setMessage("Loading.........");
        pd.setIcon(R.drawable.garbagetruckicon1);
        pd.setIndeterminate(false);
        pd.show();

        return pd;
    }
}