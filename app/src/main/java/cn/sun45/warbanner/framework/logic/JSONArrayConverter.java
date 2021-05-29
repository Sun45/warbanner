package cn.sun45.warbanner.framework.logic;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Sun45 on 2019/10/28.
 * JSONArray变换器
 */
public class JSONArrayConverter implements Converter<ResponseBody, JSONArray> {
    @Override
    public JSONArray convert(ResponseBody value) throws IOException {
        String result = value.string();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
