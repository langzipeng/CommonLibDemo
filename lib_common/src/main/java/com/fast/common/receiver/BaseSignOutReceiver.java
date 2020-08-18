package com.fast.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.blankj.utilcode.util.SPUtils;
import com.fast.common.utils.ActivityToActivity;
import com.fast.common.utils.ToastUtils;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;

/**
 * app强制退出广播
 */
public class BaseSignOutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (Objects.equals(intent.getAction(), "com.lzp.commonlib.LOG_OUT")) {//强制退出
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("该账号在其他手机中登录，已强制登出。若非本人操作，请在登录界面中重置密码");
            // 给按钮添加注册监听
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 点击按钮所调用的方法
                    //退出重新登录
                    ToastUtils.showShort("退出登录");
                    SPUtils.getInstance().put("login_state", 0);
                    SPUtils.getInstance().put("uid", 0);
                    ActivityToActivity.finishToActivity("/user/login");
                }
            });
            //不会点击外面和按返回键消失
            builder.setCancelable(false);
            builder.show();

        }
    }

}
