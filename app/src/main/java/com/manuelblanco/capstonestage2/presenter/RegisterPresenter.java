package com.manuelblanco.capstonestage2.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.LogInActivity;
import com.manuelblanco.capstonestage2.backend.LoadingCallback;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

/**
 * Created by manuel on 10/08/16.
 */
public class RegisterPresenter {

    private Context mContext;
    private SpinnerLoader mProgress;
    private View mBackground;

    public RegisterPresenter(Context context, SpinnerLoader progressBar, View view) {
        this.mContext = context;
        this.mProgress = progressBar;
        this.mBackground = view;
    }

    /**
     * Validates the values, which user entered on registration screen.
     * Shows Toast with a warning if something is wrong.
     *
     * @param name            user's name
     * @param email           user's email
     * @param password        user's password
     * @param passwordConfirm user's password confirmation
     * @return true if all values are OK, false if something is wrong
     */
    public boolean isRegistrationValuesValid( CharSequence name, CharSequence email, CharSequence password,
                                              CharSequence passwordConfirm )
    {
        return Validator.isNameValid( mContext, name )
                && Validator.isEmailValid( mContext, email )
                && Validator.isPasswordValid( mContext, password )
                && isPasswordsMatch( password, passwordConfirm );
    }

    /**
     * Determines whether password and password confirmation are the same.
     * Displays Toast with a warning if not.
     *
     * @param password        password
     * @param passwordConfirm password confirmation
     * @return true if password and password confirmation match, else false
     */
    public boolean isPasswordsMatch( CharSequence password, CharSequence passwordConfirm )
    {
        if( !TextUtils.equals( password, passwordConfirm ) )
        {
            Toast.makeText( mContext, mContext.getString( R.string.warning_passwords_do_not_match ), Toast.LENGTH_LONG ).show();
            return false;
        }

        return true;
    }

    /**
     * Sends a request to Backendless to register user.
     *
     * @param name                 user's name
     * @param email                user's email
     * @param password             user's password
     * @param registrationCallback a callback, containing actions to be executed on request result
     */
    public void registerUser( String name, String email, String password,
                              AsyncCallback<BackendlessUser> registrationCallback )
    {
        BackendlessUser user = new BackendlessUser();
        user.setEmail( email );
        user.setPassword( password );
        user.setProperty( "name", name );

        //Backendless handles password hashing by itself, so we don't need to send hash instead of plain text
        Backendless.UserService.register( user, registrationCallback );
    }

    /**
     * Creates a callback, containing actions to be executed on registration request result.
     * Shows a Toast with BackendlessUser's objectId on success,
     * show a dialog with an error message on failure.
     *
     * @return a callback, containing actions to be executed on registration request result
     */
    public LoadingCallback<BackendlessUser> createRegistrationCallback()
    {
        return new LoadingCallback<BackendlessUser>(mContext)
        {
            @Override
            public void handleResponse( BackendlessUser registeredUser )
            {
                super.handleResponse( registeredUser );
                Toast.makeText( mContext, String.format( mContext.getString( R.string.info_registered ), registeredUser.getObjectId() ), Toast.LENGTH_LONG ).show();

                Fragment fragment = ((LogInActivity)mContext).getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_REGISTER);
                if(fragment != null)
                    ((LogInActivity)mContext).getSupportActionBar().setTitle(mContext.getString(R.string.item_login));
                    ((LogInActivity)mContext).getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                UtilsView.hideProgress(mProgress, mBackground);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
                Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                UtilsView.hideProgress(mProgress, mBackground);
            }
        };
    }

    /**
     * Creates a listener, which proceeds with registration on button click.
     *
     * @return a listener, handling registration button click
     */
    public void createRegister(CharSequence name,CharSequence email, CharSequence password,
                                              CharSequence passwordConfirmation){

                if( isRegistrationValuesValid( name, email, password, passwordConfirmation ) )
                {
                    LoadingCallback<BackendlessUser> registrationCallback = createRegistrationCallback();

                    registerUser( name.toString(), email.toString(), password.toString(), registrationCallback );
                }else{
                    UtilsView.hideProgress(mProgress,mBackground);
                }
            }

}
