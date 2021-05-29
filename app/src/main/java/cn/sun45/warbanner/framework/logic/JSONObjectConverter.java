package cn.sun45.warbanner.framework.logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Sun45 on 2019/10/28.
 * JSONObject变换器
 */
public class JSONObjectConverter implements Converter<ResponseBody, JSONObject> {
    @Override
    public JSONObject convert(ResponseBody value) throws IOException {
        String result = value.string();
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
