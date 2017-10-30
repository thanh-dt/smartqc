package app.com.hss.cooking.magatama.utils;

import org.json.JSONObject;

public class JsonParserUtils {

    public static int getIntValues(JSONObject object, String node,
                                   int defaultValue) {
        int value = defaultValue;
        value = object.optInt(node);
        return value;
    }

    public static String getStringValues(JSONObject object, String node) {
        String value = "";
        value = object.optString(node);
        return value;
    }
}
