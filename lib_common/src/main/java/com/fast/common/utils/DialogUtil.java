package com.fast.common.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


/**
 * Describe：DialogUtil
 * Created by lzp on 2020/8/12.
 */
public class DialogUtil {

    private static DialogUtil instance = null;

    public static DialogUtil getInstance() {
        if (instance == null) {
            instance = new DialogUtil();
        }
        return instance;
    }


    /**
     * 显示确定提示弹窗
     */
    public void showSureDialog(Activity activity, String title, String message, boolean... isBack) {

        showDialog(activity, title, message, null,
                isBack.length < 1 || isBack[0], "确定");

    }


    /**
     * 弹窗
     *
     * @param activity
     * @param title    标题
     * @param message  消息
     * @param callBack 按钮监听
     * @param isBack   是否可以按Back键返回
     * @param action   点击按钮文字
     */
    public void showDialog(Activity activity, String title, String message,
                           DialogListener callBack, Boolean isBack,
                           String... action) {

        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(activity);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setCancelable(isBack).setCanceledOnTouchOutside(isBack);
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        for (String str : action) {
            builder.addAction(str, new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {

                    if (callBack != null) {
                        callBack.onClickAction(dialog, index);
                    }
                    dialog.dismiss();

                }
            });
        }

        builder.show();

    }


    public interface DialogListener {
        void onClickAction(QMUIDialog dialog, int index);
    }

}