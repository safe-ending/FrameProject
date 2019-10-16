package common.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ParseBox {

    public static String TAG = "ParseBox";

    private static Gson gson;

    static {
        gson = new Gson();
    }


    public static String JsonBean2Str(Object o) {
        String jsonStr = "";
        try {
            jsonStr = gson.toJson(o);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        LogBox.i(TAG, jsonStr);
        return jsonStr;
    }

    public static <T> T StrToJsonBean(String json, Class<T> clazz) {
        T bean = null;
        if (null != gson) {
            try {
                bean = gson.fromJson(json, clazz);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public static String JsonListToStr(List list) {
        String str = null;
        if (null != gson && list.size() > 0) {
            str = "[";
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    str += gson.toJson(list.get(i)) + ",";
                }else if (i == list.size() - 1){
                    str +=  gson.toJson(list.get(i)) + "]";
                }
            }
        }
        return str;
    }
}
