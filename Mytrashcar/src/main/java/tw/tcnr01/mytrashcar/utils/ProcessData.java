package tw.tcnr01.mytrashcar.utils;

import static tw.tcnr01.mytrashcar.utils.CommonUtils.sortJsonArray;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.tcnr01.mytrashcar.R;

public class ProcessData {
    public static int process(Context context, ListView listView, String response, String[] keys, String filterLocation) {
        ArrayList<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = new JSONArray();
        if (filterLocation != null) Log.e("filterLocation", filterLocation);
        try {
            jsonArray = new JSONArray(response);
            // 解析JSON
            // ------JSON 排序-
            jsonArray = sortJsonArray(jsonArray, keys[0]);
            // ----+表頭---------
            // lineid(清運點編號)、car(車牌號碼)、time(回傳時間)、location(回傳地點)、X(x座標)、Y(y座標)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);//讀取一筆資料
                Map<String, Object> item = new HashMap<String, Object>();//hashmap可以一筆一筆存
                item.put(keys[0], jsonData.getString(keys[0])); //key+value 車號
                item.put(keys[1], jsonData.getString(keys[1]));//時間
                item.put(keys[2], jsonData.getString(keys[2]));//地點
                item.put(keys[3], jsonData.getString(keys[3]));//x
                item.put(keys[4], jsonData.getString(keys[4]));//y

                if (null == filterLocation || filterLocation.equals("選擇區域")) {
                    mList.add(item);
                } else if (jsonData.getString(keys[2]).contains(filterLocation)) {
                    mList.add(item);
                }
                //=========設定listview========
                SimpleAdapter adapter = new SimpleAdapter(
                        context,
                        mList,
                        R.layout.garslist01,//應該garsitem
                        keys,
                        new int[]{R.id.gs_tt001, R.id.gs_tt002, R.id.gs_tt003, R.id.gs_tt004, R.id.gs_tt005}
                );
                listView.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.length();
    }
}
