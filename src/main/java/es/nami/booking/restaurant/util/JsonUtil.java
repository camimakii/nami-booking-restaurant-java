package es.nami.booking.restaurant.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {

    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        Gson gson = new Gson();
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, typeOfT);
    }


    public static <T> boolean areJsonEquals(String json1, String json2, Class<T> clazz) {
        T obj1 = fromJson(json1, clazz);
        T obj2 = fromJson(json2, clazz);
        return obj1.equals(obj2);
    }

}