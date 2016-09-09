package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.listeners.BackButtonListeners;
import com.manuelblanco.capstonestage2.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 6/09/16.
 */
public class WarningDialog extends DialogFragment {

    @BindView(R.id.text_info_dialog)
    TextView mWarning;
    private BackButtonListeners mBackButton;
    private String mTextWarning;
    private int mTypeWarning;

    public static WarningDialog newInstance(String text, int type) {
        WarningDialog fragment = new WarningDialog();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_DIALOG_TEXT, text);
        args.putInt(Constants.KEY_TYPE_WARNING, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTypeWarning == Constants.warningSave) {
            mBackButton.setSelectedFragment(this, 1);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTypeWarning = getArguments().getInt(Constants.KEY_TYPE_WARNING);
        mTextWarning = getArguments().getString(Constants.KEY_DIALOG_TEXT);

        if (mTypeWarning == Constants.warningSave) {
            try {
                mBackButton = (BackButtonListeners) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString()
                        + " must implement IFragmentToActivity");
            }
        }

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
                .setPositiveButton(getActivity().getResources().getString(mTypeWarning == Constants.warningSave
                                ? R.string.btn_ok : R.string.button_gps),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (mTypeWarning == Constants.warningSave) {
                                    getActivity().onBackPressed();
                                } else if (mTypeWarning == Constants.warningGPS) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(intent);
                                }
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_login, null);
        ButterKnife.bind(this, v);

        mWarning.setText(mTextWarning);

        b.setView(v);
        return b.create();
    }
}