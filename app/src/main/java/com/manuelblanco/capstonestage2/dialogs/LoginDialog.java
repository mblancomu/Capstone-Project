package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.LogInActivity;
import com.manuelblanco.capstonestage2.utils.Constants;

/**
 * Created by manuel on 17/07/16.
 */
public class LoginDialog extends DialogFragment {


    public static LoginDialog newInstance() {
        LoginDialog fragment = new LoginDialog();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setNegativeButton(getActivity().getResources().getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton(getActivity().getResources().getString(R.string.btn_login),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent login = new Intent(getActivity(), LogInActivity.class);
                                login.putExtra(Constants.KEY_LOGIN_FROM_MENU,false);
                                startActivity(login);
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_login, null);

        b.setView(v);
        return b.create();
    }
}
