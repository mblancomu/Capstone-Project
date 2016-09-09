package com.manuelblanco.capstonestage2.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.AddTripActivity;
import com.manuelblanco.capstonestage2.activities.LogInActivity;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.backend.LoadingCallback;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.model.User;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manuel on 10/08/16.
 */
public class LogInPresenter {

    private Context mContext;
    private SpinnerLoader mProgress;
    private View mBackground;
    private boolean mFromMenu;

    public LogInPresenter(Context context, SpinnerLoader progressBar, View view, boolean from) {
        this.mContext = context;
        this.mProgress = progressBar;
        this.mBackground = view;
        this.mFromMenu = from;
    }

    public boolean isLogged(){
        return true;
    }

    /**
     * Sends a request to Backendless to log in user by email and password.
     *
     * @param email         user's email
     * @param password      user's password
     * @param loginCallback a callback, containing actions to be executed on request result
     */
    public void loginUser( String email, String password, AsyncCallback<BackendlessUser> loginCallback )
    {
        Backendless.UserService.login( email, password, loginCallback );
    }

    /**
     * Sends a request to Backendless to log in user with Facebook account.
     * Fetches Facebook user's name and saves it on Backendless.
     *
     * @param loginCallback a callback, containing actions to be executed on request result
     */
    public void loginFacebookUser( AsyncCallback<BackendlessUser> loginCallback )
    {
        Map<String, String> fieldsMappings = new HashMap<>();
        fieldsMappings.put( "name", "name" );
        Backendless.UserService.loginWithFacebook((LogInActivity)mContext, null, fieldsMappings, Collections.<String>emptyList(), loginCallback );
    }

    /**
     * Sends a request to Backendless to log in user with Twitter account.
     * Fetches Twitter user's name and saves it on Backendless.
     *
     * @param loginCallback a callback, containing actions to be executed on request result
     */
    public void loginTwitterUser( AsyncCallback<BackendlessUser> loginCallback )
    {
        Map<String, String> fieldsMappings = new HashMap<>();
        fieldsMappings.put( "name", "name" );
        Backendless.UserService.loginWithTwitter((LogInActivity)mContext, null, fieldsMappings, loginCallback);
    }

    /**
     * Validates the values, which user entered on login screen.
     * Shows Toast with a warning if something is wrong.
     *
     * @param email    user's email
     * @param password user's password
     * @return true if all values are OK, false if something is wrong
     */
    public boolean isLoginValuesValid( CharSequence email, CharSequence password )
    {
        return Validator.isEmailValid(mContext, email ) && Validator.isPasswordValid(mContext, password );
    }

    /**
     * Creates a callback, containing actions to be executed on login request result.
     * Shows a Toast with BackendlessUser's objectId on success,
     * show a dialog with an error message on failure.
     *
     * @return a callback, containing actions to be executed on login request result
     */
    public LoadingCallback<BackendlessUser> createLoginCallback()
    {
        return new LoadingCallback<BackendlessUser>(mContext)
        {
            @Override
            public void handleResponse( BackendlessUser loggedInUser )
            {
                super.handleResponse( loggedInUser );
                Toast.makeText(mContext, mContext.getString( R.string.info_logged_in ), Toast.LENGTH_LONG ).show();

                AppController appController = ((AppController)mContext.getApplicationContext());
                appController.setLogin(true);
                appController.setmLogState(mContext.getResources().getString(R.string.item_login));
                String userName = loggedInUser.getEmail().split("\\@")[0];
                appController.setmUserActive(userName);

                User user = new User();
                user.setUsername(userName);
                user.setState(mContext.getResources().getString(R.string.item_login));
                SqlHandler.putUserState(user);

                if (mFromMenu){
                    ((LogInActivity)mContext).finish();
                }else {
                    mContext.startActivity(new Intent(mContext,AddTripActivity.class));
                    ((LogInActivity)mContext).finish();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
                Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                UtilsView.hideProgress(mProgress,mBackground);
            }
        };
    }

    public void createLogin(CharSequence email, CharSequence password){

        if( isLoginValuesValid( email, password ) )
        {
            LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

            loginUser( email.toString(), password.toString(), loginCallback );
        }else{
            UtilsView.hideProgress(mProgress,mBackground);
        }

    }

    public void createLoginWithFacebook(){

        LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

        loginFacebookUser( loginCallback );
    }

    public void createLoginWithTwitter(){

        LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

        loginTwitterUser( loginCallback );
    }
}
