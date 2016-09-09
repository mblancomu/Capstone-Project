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
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.LogInActivity;
import com.manuelblanco.capstonestage2.activities.SplashActivity;
import com.manuelblanco.capstonestage2.utils.Constants;

/**
 * Created by manuel on 20/08/16.
 */
public class InfoDialog extends DialogFragment{

    private String mText;

    public static InfoDialog newInstance(String text) {
        InfoDialog fragment = new InfoDialog();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_DIALOG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mText = getArguments().getString(Constants.KEY_DIALOG_TEXT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setPositiveButton(getActivity().getResources().getString(R.string.btn_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (getActivity() instanceof SplashActivity){
                                    dialog.dismiss();
                                    getActivity().finish();
                                }else{
                                    dialog.dismiss();
                                }
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_login, null);

        TextView textDialog = (TextView)v.findViewById(R.id.text_info_dialog);
        textDialog.setText(mText);

        b.setView(v);
        return b.create();
    }
}
