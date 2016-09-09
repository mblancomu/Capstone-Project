package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.listeners.DialogRecommendsListener;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.StringsUtils;
import com.manuelblanco.capstonestage2.views.CustomArrayAdapterForSpinner;

import java.util.ArrayList;

/**
 * Created by manuel on 21/08/16.
 */
public class RecommendDialog extends DialogFragment {

    private DialogRecommendsListener mListener;
    private TextView mPosition, mAdd, mClear;
    private EditText mName, mDescription, mPhone, mWebsite;
    private Spinner mTypes;

    private String mTypeSelected, mPositionSelected, mIdTrip;

    private String mTypeOfRecommend;


    public static RecommendDialog newInstance(String typeOfRecommend, String idTrip) {
        RecommendDialog fragment = new RecommendDialog();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_RECOMMEND, typeOfRecommend);
        args.putString(Constants.KEY_IDTRIP, idTrip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTypeOfRecommend = getArguments().getString(Constants.KEY_RECOMMEND);
        mIdTrip = getArguments().getString(Constants.KEY_IDTRIP);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogRecommendsListener) context;
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
                .setPositiveButton(getActivity().getResources().getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                submitRecommend();
                            }
                        }
                )
                .setNegativeButton(getActivity().getResources().getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_recommend, null);

        mTypes = (Spinner) v.findViewById(R.id.sp_add_recommend_type);
        mPosition = (TextView) v.findViewById(R.id.tv_add_position);
        mAdd = (TextView) v.findViewById(R.id.tv_add_recommend);
        mClear = (TextView) v.findViewById(R.id.tv_clear);
        mName = (EditText) v.findViewById(R.id.et_add_recommend_title);
        mDescription = (EditText) v.findViewById(R.id.et_add_recommend_description);
        mPhone = (EditText) v.findViewById(R.id.et_add_recommend_phone);
        mWebsite = (EditText) v.findViewById(R.id.et_add_recommend_website);

        initListeners();
        initSpinners();

        b.setView(v);
        return b.create();
    }

    private void initListeners() {

        mPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PositionDialog fragment = (PositionDialog) getActivity().getSupportFragmentManager().findFragmentByTag(Constants.DIALOG_POSITION);
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }

                PositionDialog dialog = PositionDialog.newInstance(mTypeOfRecommend, Constants.FROM_ADD_TRIP);
                dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_POSITION);
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitRecommend();
                clearRecommend();
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRecommend();
            }
        });
    }

    private void initSpinners() {

     /*   ArrayAdapter selector_types = ArrayAdapter.createFromResource(getActivity(), mTypeOfRecommend.equals(Constants.TYPE_EAT) ?
                R.array.types_eat : R.array.types_sleep, android.R.layout.simple_spinner_item);*/

        String[] types = getResources().getStringArray(mTypeOfRecommend.equals(Constants.TYPE_EAT) ?
                R.array.types_eat : R.array.types_sleep);
        ArrayList<String> convertTypes = StringsUtils.convertToArrayList(types, getString(R.string.hint_spinner_types));

        CustomArrayAdapterForSpinner selector_types = new CustomArrayAdapterForSpinner(
                getActivity(), R.layout.spinner_item, convertTypes);

        selector_types.setDropDownViewResource(R.layout.spinner_item);

        mTypes.setAdapter(selector_types);
        mTypes.setSelection(selector_types.getCount());

        mTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTypeSelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setPosition(String position) {
        mPositionSelected = position;
    }

    private void clearRecommend() {

        mName.setText("");
        mDescription.setText("");
        mPhone.setText("");
        mWebsite.setText("");
        setPosition("");
    }

    private void submitRecommend() {

        Recommend recommend = new Recommend();
        recommend.setType(mTypeOfRecommend);
        recommend.setCoords(mPositionSelected);
        recommend.setResource(mTypeSelected);
        recommend.setWebsite(mWebsite.getText().toString());
        recommend.setPhone(mPhone.getText().toString());
        recommend.setDescription(mDescription.getText().toString());
        recommend.setName(mName.getText().toString());
        recommend.setIdTrip(mIdTrip);

        mListener.onRecommendCompleted(recommend);
    }

}
