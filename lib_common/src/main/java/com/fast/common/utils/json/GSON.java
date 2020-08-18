package com.fast.common.utils.json;


import android.net.Uri;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Iterator;

class GSON {

    private static final String tag = GSON.class.getSimpleName();

    private static double sVersion = -1.0d;

    public static final Gson sInstance = gsonBuilder().create();

    public static final String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        return sInstance.toJson(object);
    }

    public static final <T> T parseObject(String json, Class<T> clazz) {
        return sInstance.fromJson(json, clazz);
    }

    public static final <T> T parseObject(String json, Type type) {
        return sInstance.fromJson(json, type);
    }

    public static final JSONObject parseObject(String json) {
        if (json == null || json.length() <= 0 || json.trim().length() <= 0) {
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final JSONObject parseObject(Object object) {
        return parseObject(toJSONString(object));
    }

    public static final JSONArray parseArray(String json) {
        return parseObject(json, JSONArray.class);
    }

    /******************************************************************************************************************/
    /**
     * Create && Config Gson Builder
     *
     * @return
     */
    private static final GsonBuilder gsonBuilder() {
        return new GsonBuilder()
            //.serializeNulls()
            .disableHtmlEscaping()
            .setLenient()
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    Annotation annotation = f.getAnnotation(Expose.class);
                    if (annotation != null) {
                        return !((Expose) annotation).serialize();
                    }
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    Annotation annotation = f.getAnnotation(Expose.class);
                    if (annotation != null) {
                        return !((Expose) annotation).deserialize();
                    }
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .registerTypeAdapter(boolean.class, BooleanTypeAdapter.sInstance)
            .registerTypeAdapter(Boolean.class, BooleanTypeAdapter.sInstance)
            .registerTypeAdapter(int.class, IntegerTypeAdapter.sInstance)
            .registerTypeAdapter(Integer.class, IntegerTypeAdapter.sInstance)
            .registerTypeAdapter(String.class, StringTypeAdapter.sInstance)
            .registerTypeAdapter(JSONObject.class, JSONObjectAdapter.sInstance)
            .registerTypeAdapter(JSONArray.class, JSONArrayAdapter.sInstance)
            .registerTypeAdapter(Uri.class, UriAdapter.sInstance)
            .registerTypeAdapterFactory(new WrapperAdapterFactory())
            .setVersion(sVersion);
    }

    /******************************************************************************************************************/

    static class WrapperAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                    if (value instanceof JSONProcessCallback) {
                        ((JSONProcessCallback) value).finishSerialization();
                    }
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    try {
                        T t = delegate.read(in);
                        if (t instanceof JSONProcessCallback) {
                            ((JSONProcessCallback) t).finishDeserialization();
                        }
                        return t;
                    } catch (Exception e) {
                        String propertyName = in.getPath();
                        if (e != null && e.getMessage() != null && e.getMessage().contains("json parse property")) {
                            throw e;
                        }
                        throw new JsonSyntaxException("delegate = " + delegate + " json parse property \"" + propertyName +"\" Exception origin crash:\n" + e);
                    }
                }
            };
        }
    }
    
    static class JSONObjectAdapter implements JsonSerializer<JSONObject>, JsonDeserializer<JSONObject> {

        public static JSONObjectAdapter sInstance = new JSONObjectAdapter();

        @Override
        public JsonElement serialize(JSONObject src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return null;
            }

            JsonObject jsonObject = new JsonObject();
            Iterator<String> keys = src.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = src.opt(key);

                JsonElement jsonElement = context.serialize(value, value.getClass());
                jsonObject.add(key, jsonElement);
            }
            return jsonObject;
        }

        @Override
        public JSONObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null) {
                return null;
            }
            try {
                return new JSONObject(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                throw new JsonParseException(e);
            }
        }
    }


    static class JSONArrayAdapter implements JsonSerializer<JSONArray>, JsonDeserializer<JSONArray> {

        public static final JSONArrayAdapter sInstance = new JSONArrayAdapter();

        @Override
        public JsonElement serialize(JSONArray src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return null;
            }
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < src.length(); i++) {
                Object object = src.opt(i);
                JsonElement jsonElement = context.serialize(object, object.getClass());
                jsonArray.add(jsonElement);
            }
            return jsonArray;
        }

        @Override
        public JSONArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null) {
                return null;
            }
            try {
                return new JSONArray(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                throw new JsonParseException(e);
            }
        }
    }

    /**
     * String 转换适配，序列化时null写为空字符串
     * 反序列化时，null写为空字符串
     */
    static class StringTypeAdapter extends TypeAdapter<String> {

        public static final StringTypeAdapter sInstance = new StringTypeAdapter();

        @Override
        public String read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            /* coerce booleans to strings for backwards compatibility */
            if (peek == JsonToken.BOOLEAN) {
                return Boolean.toString(in.nextBoolean());
            }

            if (peek == JsonToken.BEGIN_OBJECT) {
                JsonElement jsonParser = new JsonParser().parse(in);
                return jsonParser.toString();
            }

            if (peek == JsonToken.BEGIN_ARRAY) {
                JsonElement jsonParser = new JsonParser().parse(in);
                return jsonParser.toString();
            }

            return in.nextString();
        }

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            if (value == null) {
                value = "";
            }
            out.value(value);
        }
    }


    /**
     * int 转换适配，反序列化支持string && boolean
     * 序列化支持空对象写为0
     */
    static class IntegerTypeAdapter extends TypeAdapter<Number> {

        public static final IntegerTypeAdapter sInstance = new IntegerTypeAdapter();

        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            if (in.peek() == JsonToken.STRING) {
                String value = in.nextString();
                if (value.equalsIgnoreCase("true")) {
                    return 1;
                }
                if (value.equalsIgnoreCase("false")) {
                    return 0;
                }
                return Integer.parseInt(value);
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                boolean value = in.nextBoolean();
                return value ? 1 : 0;
            }
            try {
                return in.nextInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            if (value == null) {
                value = 0;
            }
            out.value(value);
        }
    }

    /**
     * boolean 转换适配，反序列化支持int && string
     * 序列化支持空对象写为false
     */
    static class BooleanTypeAdapter extends TypeAdapter<Boolean> {

        public static final BooleanTypeAdapter sInstance = new BooleanTypeAdapter();

        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                value = false;
            }
            out.value(value);
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return false;
            } else if (peek == JsonToken.STRING) {
                String value = in.nextString();
                if (value.equalsIgnoreCase("1")) {
                    return true;
                }
                if (value.equalsIgnoreCase("0")) {
                    return false;
                }
                return Boolean.parseBoolean(value);
            } else if (peek == JsonToken.NUMBER) {
                String value = in.nextString();
                return parseInt(value) == 1;
            }
            return in.nextBoolean();
        }

        private int parseInt(String value) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                try {
                    return (int) Long.parseLong(value);
                } catch (NumberFormatException nfe) {
                    return new BigDecimal(value).intValue();
                }
            }
        }
    }

    static class UriAdapter implements JsonSerializer<Uri>, JsonDeserializer<Uri> {

        public static final UriAdapter sInstance = new UriAdapter();

        @Override
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return null;
            }
            return new JsonPrimitive(src.toString());
        }

        @Override
        public Uri deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.toString().replace("\"", ""));
        }
    }
}
