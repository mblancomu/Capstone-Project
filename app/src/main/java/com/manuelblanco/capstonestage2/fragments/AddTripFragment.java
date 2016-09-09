package com.manuelblanco.capstonestage2.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.dialogs.InfoDialog;
import com.manuelblanco.capstonestage2.dialogs.PhotoDialog;
import com.manuelblanco.capstonestage2.dialogs.PositionDialog;
import com.manuelblanco.capstonestage2.dialogs.RecommendDialog;
import com.manuelblanco.capstonestage2.dialogs.TypesDialog;
import com.manuelblanco.capstonestage2.dialogs.WarningDialog;
import com.manuelblanco.capstonestage2.listeners.BackButtonListeners;
import com.manuelblanco.capstonestage2.listeners.DialogMapListener;
import com.manuelblanco.capstonestage2.listeners.DialogTypesListener;
import com.manuelblanco.capstonestage2.model.Country;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.presenter.AddTripPresenter;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.DialogUtils;
import com.manuelblanco.capstonestage2.utils.StringsUtils;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.views.CustomArrayAdapterForSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 13/08/16.
 */
public class AddTripFragment extends BaseFragment implements DialogTypesListener {

    @BindView(R.id.et_add_title)
    EditText mTitle;
    @BindView(R.id.et_add_description)
    EditText mDescription;
    @BindView(R.id.sp_country)
    Spinner mCountry;
    @BindView(R.id.sp_region)
    Spinner mRegion;
    @BindView(R.id.ib_eat)
    TextView mEat;
    @BindView(R.id.ib_camera)
    TextView mPhoto;
    @BindView(R.id.ib_sleep)
    TextView mSleep;
    @BindView(R.id.ib_position)
    TextView mPosition;
    @BindView(R.id.ib_type)
    TextView mTypes;
    @BindView(R.id.tv_submit)
    TextView mSubmit;

    private SpinnerLoader mProgress;
    private View mView;

    private String mSelectedTypes, mCountrySelected, mTitleSelected, mRegionSelected;
    private AddTripPresenter presenter;
    private Bitmap mPhotoSelected;
    private String mUser;
    private String mPositionSelected;
    private ArrayList<Recommend> mRecommendsInsert;
    private String mPhotoName;
    private String mIdTrip;
    private BackButtonListeners mBackListener;
    private boolean isSubmit;
    public Uri uriImage;

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    public static AddTripFragment newInstance() {
        AddTripFragment fragment = new AddTripFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_trip, container, false);
        ButterKnife.bind(this, v);

        mProgress = (SpinnerLoader) getActivity().findViewById(R.id.progressbar);
        mView = (View) getActivity().findViewById(R.id.back_progress);

        presenter = new AddTripPresenter(getActivity(), mProgress, mView);
        mRecommendsInsert = new ArrayList<>();
        AppController appController = ((AppController) getActivity().getApplicationContext());
        mUser = appController.getmUserActive();

        initSpinners();
        initListeners();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mBackListener = (BackButtonListeners) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mBackListener.setSelectedFragment(this, 0);
    }

    private void initListeners() {

        mTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTitle != null && !mTitle.getText().toString().matches("") && mCountrySelected != null &&
                        !mCountrySelected.isEmpty()) {
                    TypesDialog dialog = TypesDialog.newInstance("*");
                    dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_TYPES);
                } else {
                    DialogUtils.launchInfoDialog(getActivity(), getString(R.string.info_take_photo_dialog));
                }
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTitle != null && !mTitle.getText().toString().matches("") && mCountrySelected != null &&
                        !mCountrySelected.isEmpty()) {
                    PhotoDialog dialog = PhotoDialog.newInstance();
                    dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_PHOTO);
                    mIdTrip = setIdTrip();
                } else {
                    DialogUtils.launchInfoDialog(getActivity(), getString(R.string.info_take_photo_dialog));
                }
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitAllData();

            }
        });

        mPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTitle != null && !mTitle.getText().toString().matches("") && mCountrySelected != null &&
                        !mCountrySelected.isEmpty()) {
                    PositionDialog dialog = PositionDialog.newInstance(mTitle.getText().toString().matches("") ? "TripTop" :
                            mTitle.getText().toString(), Constants.FROM_ADD_TRIP);
                    dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_POSITION);
                } else {
                    DialogUtils.launchInfoDialog(getActivity(), getString(R.string.info_take_photo_dialog));
                }
            }
        });

        mSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecommend(Constants.TYPE_SLEEP, mIdTrip);
            }
        });

        mEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecommend(Constants.TYPE_EAT, mIdTrip);
            }
        });
    }

    private void initSpinners() {

        ArrayList<String> countries = SqlHandler.getAllCountries();
        Collections.sort(countries, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        countries.add(getString(R.string.hint_spinner_countries));

        CustomArrayAdapterForSpinner adapterCountries = new CustomArrayAdapterForSpinner(getActivity(), R.layout.spinner_item,
                countries);
        adapterCountries.setDropDownViewResource(R.layout.spinner_item);

        ArrayList<String> convertRegion = StringsUtils.convertToArrayList(getResources().getStringArray(R.array.regions),
                getString(R.string.hint_spinner_region));

        CustomArrayAdapterForSpinner regions = new CustomArrayAdapterForSpinner(getActivity(), R.layout.spinner_item, convertRegion);
        regions.setDropDownViewResource(R.layout.spinner_item);

        mRegion.setAdapter(regions);
        mRegion.setSelection(regions.getCount());
        mCountry.setAdapter(adapterCountries);
        mCountry.setSelection(adapterCountries.getCount());

        mCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCountrySelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCountrySelected = "";
            }
        });

        mRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRegionSelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRegionSelected = "";
            }
        });
    }

    @Override
    public void onTypesSelected(ArrayList<String> types, String area) {

        for (String s : types) {
            mSelectedTypes += s + "\t";
        }
    }

    public void setPhoto(Bitmap photo) {
        mPhotoSelected = photo;
    }

    public void setPosition(String position) {
        mPositionSelected = position;
    }

    public void setTypes(String types) {
        mSelectedTypes = types;
    }

    public void setRecommends(Recommend recommend) {
        mRecommendsInsert.add(recommend);
    }

    public String setIdTrip() {
        AppController appController = AppController.getInstance();
        return appController.getmUserActive() + "_" + System.currentTimeMillis();
    }

    private void launchRecommend(String type, String idTrip) {
        if (mTitle != null && !mTitle.getText().toString().matches("") && mCountrySelected != null &&
                !mCountrySelected.isEmpty()) {

            RecommendDialog dialog = RecommendDialog.newInstance(type, idTrip);
            dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_RECOMMEND);
        } else {
            DialogUtils.launchInfoDialog(getActivity(), getString(R.string.info_take_photo_dialog));
        }
    }

    private String getCountryCode(String name) {
        Country country = SqlHandler.getCountryCode(name);
        return country.getIso();
    }

    private void submitAllData() {

        mTitleSelected = mTitle.getText().toString();
        mPhotoName = presenter.setPhotoName(mTitleSelected, mCountrySelected);

        if (verificateFiledsForSubmit()) {

            presenter.uploadPhoto(mPhotoSelected, mPhotoName);

            Trip myTrip = new Trip();
            myTrip.setId_trip(mIdTrip);
            myTrip.setTitle(mTitle.getText().toString());
            myTrip.setCountry(getCountryCode(mCountrySelected));
            myTrip.setContinent(mRegionSelected);
            myTrip.setPhoto(mPhotoName);
            myTrip.setCoords(mPositionSelected);
            myTrip.setType(mSelectedTypes);
            myTrip.setRate(getString(R.string.default_rate));
            myTrip.setHasRoute(getString(R.string.default_bool));
            myTrip.setFavorite(getString(R.string.default_bool));
            myTrip.setDescription(mDescription.getText().toString());
            myTrip.setUser(mUser);
            myTrip.setVoted(getString(R.string.default_bool));


            presenter.uploadCompleteInfo(mRecommendsInsert, myTrip);
            isSubmit = true;

            UtilsView.showSnackBar((CoordinatorLayout) getActivity().findViewById(R.id.add_coordinator),
                    getString(R.string.upload_info_warning),
                    getResources().getColor(R.color.colorAccent));

            clearFields();
        } else {
            UtilsView.showSnackBar((CoordinatorLayout) getActivity().findViewById(R.id.add_coordinator),
                    getString(R.string.all_fields_info),
                    getResources().getColor(R.color.colorAccent));
        }
    }

    public void clearFields(){

        mTitle.setText("");
        mDescription.setText("");
        mTitleSelected = "";
        mPhotoName = "";
        mPhotoSelected = null;
        mIdTrip = "";
        mCountry.setSelection(0);
        mRegion.setSelection(0);
        mPositionSelected = "";
        mSelectedTypes = "";
        mUser = "";

    }

    public void onBackPressed() {

        if (!mTitle.getText().toString().isEmpty() && !isSubmit) {
            WarningDialog dialog = WarningDialog.newInstance(getString(R.string.close_without_save_trip),
                    Constants.warningSave);
            dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_WARNING);
        }else if (mTitle.getText().toString().isEmpty() && !isSubmit){
            mBackListener.setSelectedFragment(this, 1);
        }else if (mTitle.getText().toString().isEmpty() && isSubmit){
            mBackListener.setSelectedFragment(this, 1);
        }else {
            mBackListener.setSelectedFragment(this, 1);
        }
    }

    private boolean verificateFiledsForSubmit() {

        if (mIdTrip == null || mIdTrip.isEmpty()) {
            return false;
        }

        if (mTitle.getText().length() == 0) {
            return false;
        }

        if (mCountrySelected == null || mCountrySelected.isEmpty()) {
            return false;
        }

        if (mRegionSelected == null || mRegionSelected.isEmpty()) {
            return false;
        }
        if (mPhotoName == null || mPhotoName.isEmpty()) {
            return false;
        }

        if (mPositionSelected == null || mPositionSelected.isEmpty()) {
            return false;
        }
        if (mSelectedTypes == null || mSelectedTypes.isEmpty()) {
            return false;
        }

        return true;

    }

}