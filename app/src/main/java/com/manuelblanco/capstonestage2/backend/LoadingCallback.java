package com.manuelblanco.capstonestage2.backend;

import android.content.Context;
import android.view.View;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.dialogs.DialogHelper;

/**
 * A callback, which has ability to show loading dialog while response is being received.
 * Shows Toast with result's toString() on success.
 * Shows AlertDialog with error message on failure.
 * If overriding handleResponse and/or handleFault, should manually dismiss ProgressDialog with hideLoading() method
 * or calling super.handleResponse() or super.handleFault().
 *
 * @param <T> class to be received from server
 */
public class LoadingCallback<T> implements AsyncCallback<T>
{
  private Context context;

  /**
   * Creates an instance with given message.
   *
   * @param context        context to which ProgressDialog should be attached
   */
  public LoadingCallback(Context context)
  {
    this.context = context;
  }

  @Override
  public void handleResponse( T response )
  {

  }

  @Override
  public void handleFault( BackendlessFault fault )
  {
    DialogHelper.createErrorDialog( context, "BackendlessFault", fault.getMessage() ).show();
  }

}
