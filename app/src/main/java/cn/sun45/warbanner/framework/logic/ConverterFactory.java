package cn.sun45.warbanner.framework.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Sun45 on 2019/10/28.
 * 变换器工厂
 */
public class ConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == JSONObject.class) {
            return new JSONObjectConverter();
        } else if (type == JSONArray.class) {
            return new JSONArrayConverter();
        } else if (type==String.class) {
            return new StringConverter();
        }
        return null;
    }
}
