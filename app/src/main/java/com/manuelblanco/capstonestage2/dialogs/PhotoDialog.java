package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.listeners.DialogPhotoListener;
import com.manuelblanco.capstonestage2.utils.Utils;

/**
 * Created by manuel on 18/08/16.
 */
public class PhotoDialog extends DialogFragment {

    private TextView mCamera;
    private TextView mGallery;
    private DialogPhotoListener mListener;
    private String userChoosenTask;

    public static PhotoDialog newInstance() {
        PhotoDialog fragment = new PhotoDialog();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogPhotoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setNegativeButton(getActivity().getResources().getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_photo, null);

        mCamera = (TextView) v.findViewById(R.id.ib_camera);
        mGallery = (TextView) v.findViewById(R.id.ib_gallery);

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = getString(R.string.action_take_photo);
                boolean result= Utils.checkPermission(getActivity());
                mListener.onSelectedCamera();
                dismiss();
            }
        });

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = getString(R.string.action_gallery);
                boolean result= Utils.checkPermission(getActivity());
                mListener.onSelectedGallery();
                dismiss();
            }
        });

        b.setView(v);
        return b.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(R.string.action_take_photo))
                        mListener.onSelectedCamera();
                    else if(userChoosenTask.equals(R.string.action_gallery))
                        mListener.onSelectedGallery();
                } else {
                }
                break;
        }
    }

}
