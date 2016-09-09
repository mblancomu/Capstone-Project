package com.manuelblanco.capstonestage2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.BackendlessUser;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.presenter.LogInPresenter;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 9/08/16.
 */
public class LogInFragment extends BaseFragment {

    @BindView(R.id.et_username)
    EditText mUsername;
    @BindView(R.id.et_password)
    EditText mPassword;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.tv_register)
    TextView mRegister;
    @BindView(R.id.loginFacebookButton)
    Button mFacebook;
    @BindView(R.id.loginTwitterButton)
    Button mTwitter;

    private LogInPresenter presenter;
    private static final int REGISTER_REQUEST_CODE = 1;
    private boolean mFromMenu;

    public static LogInFragment newInstance(boolean from) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.KEY_LOGIN_FROM_MENU,from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mFromMenu = getArguments().getBoolean(Constants.KEY_LOGIN_FROM_MENU);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,v);

        presenter = new LogInPresenter(getActivity(),(SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                getActivity().findViewById(R.id.back_progress),mFromMenu);

        initListeners();

        return v;
    }

    private void initListeners(){

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRegister();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validator.validateFieldsLogin(getActivity(),
                        mUsername,mPassword)) {
                    UtilsView.showProgress((SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                            getActivity().findViewById(R.id.back_progress));
                    presenter.createLogin(mUsername.getText(), mPassword.getText());
                }
            }
        });

        mFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsView.showProgress((SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                        getActivity().findViewById(R.id.back_progress));
                presenter.createLoginWithFacebook();
            }
        });

        mTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsView.showProgress((SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                        getActivity().findViewById(R.id.back_progress));
                presenter.createLoginWithTwitter();
            }
        });

        mPassword.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE ) {
                            if (Validator.validateFieldsLogin(getActivity(),
                                    mUsername,mPassword)) {
                                UtilsView.showProgress((SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                                        getActivity().findViewById(R.id.back_progress));
                                presenter.createLogin(mUsername.getText(), mPassword.getText());

                                return true;
                            }
                        }
                        return false;
                    }
                });

    }

    private void launchRegister() {

        RegisterFragment fr = RegisterFragment.newInstance();

        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_main, fr, Constants.FRAGMENT_REGISTER);
        fragmentTransaction.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if( resultCode == getActivity().RESULT_OK )
        {
            switch( requestCode )
            {
                case REGISTER_REQUEST_CODE:
                    String email = data.getStringExtra( BackendlessUser.EMAIL_KEY );
                    mUsername.setText( email );
                    mPassword.requestFocus();
                    UtilsView.hideProgress((SpinnerLoader) getActivity().findViewById(R.id.progressbar),
                            getActivity().findViewById(R.id.back_progress));

                    UtilsView.showSnackBar((CoordinatorLayout) getActivity().findViewById(R.id.login_coordinator),
                            getString( R.string.info_registered_success ), getResources().getColor(R.color.colorAccent));
            }
        }
    }
}
