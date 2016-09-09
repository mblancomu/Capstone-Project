package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.db.TripsTypesDB;
import com.manuelblanco.capstonestage2.listeners.DialogTypesListener;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 7/08/16.
 */
public class TypesDialog extends DialogFragment {

    private DialogTypesListener mListener;
    private String mCurrentCountry;

    @BindView(R.id.ll_container)
    LinearLayout mLinearContainer;
    @BindView(R.id.rb_alls)
    RadioButton mAllCountries;
    @BindView(R.id.rb_your_country)
    RadioButton mYourCountry;

    public static TypesDialog newInstance(String currentCountry) {
        TypesDialog fragment = new TypesDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_COUNTRY, currentCountry);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentCountry = getArguments().getString(Constants.KEY_COUNTRY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogTypesListener) context;
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
                                mListener.onTypesSelected(getResultTypes(mLinearContainer),
                                        mCurrentCountry.equals("*") ? "*" : getArea());
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

        View v = i.inflate(R.layout.dialog_types, null);
        ButterKnife.bind(this,v);

        if (mCurrentCountry.equals("*")){
            mAllCountries.setVisibility(View.GONE);
            mYourCountry.setVisibility(View.GONE);
        }else{
            mAllCountries.setChecked(getFromSP(String.valueOf(mAllCountries.getId())));
            mYourCountry.setChecked(getFromSP(String.valueOf(mYourCountry.getId())));

            if (mAllCountries.isChecked() == false && mYourCountry.isChecked() == false){
                mAllCountries.setChecked(true);
            }
        }

        addCheckBox(mLinearContainer);

        b.setView(v);
        return b.create();
    }

    private void addCheckBox(LinearLayout ll) {

        ArrayList<String> types;

        if (mCurrentCountry.equals("*")){
           types =  new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.trips_types)));
        }else{
            types = SqlHandler.getColumnList(TripsTypesDB.COLUMN_TYPE, TripsTypesDB.TABLE_TRIPSTYPES, 2);
        }

        for (int i = 0; i < types.size(); i++) {
            CheckBox cb = new CheckBox(getActivity());
            cb.setText(types.get(i));
            cb.setId(i);
            cb.setChecked(getFromSP(String.valueOf(cb.getId())));
            ll.addView(cb);
        }

    }

    private ArrayList<String> getResultTypes(ViewGroup parent) {
        ArrayList<String> addTypes = new ArrayList<>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                if (cb.isChecked()) {
                    saveInSp(String.valueOf(cb.getId()),cb.isChecked());
                    addTypes.add((String) cb.getText());
                }
            }
        }

        return addTypes;
    }

    private String getArea() {
        String area;
        if (mYourCountry.isChecked()) {
            saveInSp(String.valueOf(mYourCountry.getId()),mYourCountry.isChecked());
            area = mCurrentCountry;
        } else {
            saveInSp(String.valueOf(mAllCountries.getId()),mAllCountries.isChecked());
            area = "*";
        }

        return area;
    }

    private boolean getFromSP(String key){
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE_TYPES, android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    private void saveInSp(String key,boolean value){
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE_TYPES, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
