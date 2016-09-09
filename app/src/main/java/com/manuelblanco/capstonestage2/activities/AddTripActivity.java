package com.manuelblanco.capstonestage2.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.dialogs.RecommendDialog;
import com.manuelblanco.capstonestage2.fragments.AddTripFragment;
import com.manuelblanco.capstonestage2.listeners.BackButtonListeners;
import com.manuelblanco.capstonestage2.listeners.DialogPhotoListener;
import com.manuelblanco.capstonestage2.listeners.DialogPositionListener;
import com.manuelblanco.capstonestage2.listeners.DialogRecommendsListener;
import com.manuelblanco.capstonestage2.listeners.DialogTypesListener;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.presenter.AddTripPresenter;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.PermissionsHelper;
import com.manuelblanco.capstonestage2.utils.StringsUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 13/08/16.
 */
public class AddTripActivity extends BaseActivity implements DialogPhotoListener, DialogPositionListener,
        DialogTypesListener, DialogRecommendsListener, BackButtonListeners {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.back_progress)
    View mBackground;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;

    private Tracker mTracker;
    private String TAG = MainActivity.class.getSimpleName();
    private AddTripPresenter presenter;
    private AddTripFragment mAddFragment;
    private RecommendDialog mRecommendFragment;
    private Fragment mBackFragment;
    private int fragmentCode;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_add_trip);
        ButterKnife.bind(this);

        fragmentCode = -1;
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        presenter = new AddTripPresenter(this, null, null);

        launchAddTripFragment(Constants.FRAGMENT_ADD_TRIP);
    }

    @Override
    public void onBackPressed() {

        switch (fragmentCode) {
            case 0:
                ((AddTripFragment)mBackFragment).onBackPressed();
                break;
            case 1:
                super.onBackPressed();
                break;
            default:
                super.onBackPressed();
                break;
        }
        /*if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStackImmediate();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_settings:
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void launchAddTripFragment(String tag) {

        AddTripFragment fr = AddTripFragment.newInstance();

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_main, fr, tag);
        fragmentTransaction.commit();

    }

    @Override
    public void onSelectedCamera() {
        cameraIntent();
    }

    @Override
    public void onSelectedGallery() {
        presenter.galleryIntent();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mAddFragment = (AddTripFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_ADD_TRIP);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE) {
                mAddFragment.setPhoto(presenter.onSelectFromGalleryResult(data));
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                //mAddFragment.setPhoto(presenter.onCaptureImageResult(data));
                grabImage(uriImage);
            }
        }
    }

    @Override
    public void onPositionSelected(String position) {
        mAddFragment = (AddTripFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_ADD_TRIP);
        mAddFragment.setPosition(StringsUtils.convertLatLngForDB(position));
    }

    @Override
    public void onPositionRecommends(String position) {
        mRecommendFragment = (RecommendDialog) getSupportFragmentManager().findFragmentByTag(Constants.DIALOG_RECOMMEND);
        mRecommendFragment.setPosition(position);
    }

    @Override
    public void onTypesSelected(ArrayList<String> types, String area) {
        mAddFragment = (AddTripFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_ADD_TRIP);
        mAddFragment.setTypes(StringsUtils.convertArrayListToString(types));
    }

    @Override
    public void onRecommendCompleted(Recommend recommend) {
        mAddFragment = (AddTripFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_ADD_TRIP);
        mAddFragment.setRecommends(recommend);
    }

    @Override
    public void setSelectedFragment(Fragment fragment, int code) {
        this.fragmentCode = code;
        this.mBackFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.add_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void grabImage(Uri mImageUri)
    {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            mAddFragment.setPhoto(presenter.onCaptureImageResult(bitmap));

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }
    }

    public void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photo = null;
        try
        {
            // place where to store camera taken picture
            photo = createTemporaryFile("picture", ".jpg");
            photo.delete();
        }
        catch(Exception e)
        {
        }

        uriImage = Uri.fromFile(photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);

        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    public File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

}