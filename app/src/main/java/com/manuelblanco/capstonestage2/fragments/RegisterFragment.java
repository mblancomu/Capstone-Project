package com.manuelblanco.capstonestage2.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.LogInActivity;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.presenter.RegisterPresenter;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 10/08/16.
 */
public class RegisterFragment extends BaseFragment {

    @BindView(R.id.nameField)
    EditText mUsername;
    @BindView(R.id.emailField)
    EditText mEmail;
    @BindView(R.id.passwordField)
    EditText mPassword;
    @BindView(R.id.passwordConfirmField)
    EditText mRepeatPassword;
    @BindView(R.id.registerButton)
    Button mSubmit;

    private Tracker mTracker;

    private RegisterPresenter presenter;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        setHasOptionsMenu(true);

        ((LogInActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.register));
        ((LogInActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);

        presenter = new RegisterPresenter(getActivity(), (SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                getActivity().findViewById(R.id.back_progress));

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.validateFieldsRegister(getActivity(),
                        mUsername,mEmail,mPassword,mRepeatPassword)) {
                    presenter.createRegister(mUsername.getText(), mEmail.getText(), mPassword.getText(),
                            mRepeatPassword.getText());
                }
            }
        });

        mRepeatPassword.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (Validator.validateFieldsRegister(getActivity(),
                                    mUsername,mEmail,mPassword,mRepeatPassword)) {
                                UtilsView.showProgress((SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                                        getActivity().findViewById(R.id.back_progress));
                                presenter.createRegister(mUsername.getText(), mEmail.getText(), mPassword.getText(),
                                        mRepeatPassword.getText());

                                return true;
                            }
                        }
                        return false;
                    }
                });

        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.detail_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}