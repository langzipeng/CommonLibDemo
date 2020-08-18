package com.fast.common.net.converter;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.fast.common.net.BaseHttpResult;
import com.fast.common.net.ResponseParams;
import com.fast.common.net.exception.ApiException;
import com.fast.common.utils.json.JSON;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * Description:最外层模型类,自定义的Gson响应体变
 */

public class RxResponseBodyConverter<T> implements Converter<ResponseBody, BaseHttpResult<T>> {
    private final Gson gson;
    private final Type type;

    RxResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public BaseHttpResult<T> convert(@NonNull ResponseBody value) throws IOException {
        String responseStr = value.string();
        BaseHttpResult<T> httpResult;
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            String status = jsonObject.getString(ResponseParams.RES_STATUS);
            if (status.equals("ok") ) {
                httpResult = JSON.parseObject(responseStr, type);
                //httpResult = gson.fromJson(responseStr, type);
                //httpResult = JSON.parseObject(responseStr, BaseHttpResult.class);
            } else {
                int code = jsonObject.getInt(ResponseParams.RES_CODE);
                String msg = jsonObject.getString(ResponseParams.RES_MSG);
                httpResult = new BaseHttpResult<>();
                httpResult.setCode(code);
                httpResult.setMsg(msg);
                httpResult.setData(null);
                //抛一个自定义ResultException 传入失败时候的状态码，和信息
                throw new ApiException(httpResult.getCode(), httpResult.getMsg());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            httpResult = new BaseHttpResult<>();
            httpResult.setCode(ResponseParams.PARSING_ERROR);
            httpResult.setMsg("解析异常");
            throw new ApiException(ResponseParams.PARSING_ERROR, "解析异常");
        } finally {
            value.close();
        }
        return httpResult;
    }
}
