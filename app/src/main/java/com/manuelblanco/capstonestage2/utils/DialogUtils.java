package com.manuelblanco.capstonestage2.utils;

import android.content.Context;

import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.dialogs.InfoDialog;

/**
 * Created by manuel on 20/08/16.
 */
public class DialogUtils {

    public static void launchInfoDialog(Context context, String text) {
        InfoDialog dialog = InfoDialog.newInstance(text);
        dialog.show(((BaseActivity) context).getSupportFragmentManager(), Constants.DIALOG_INFO);
    }
}
