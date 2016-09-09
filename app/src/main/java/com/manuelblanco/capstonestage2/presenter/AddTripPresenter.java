package com.manuelblanco.capstonestage2.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.AddTripActivity;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.model.Route;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.UtilsView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by manuel on 13/08/16.
 */
public class AddTripPresenter {

    private Context mContext;
    private SpinnerLoader mProgress;
    private View mBackProgress;
    private Uri mUriImage;

    public AddTripPresenter(Context context, SpinnerLoader spinnerLoader, View back) {
        this.mContext = context;
        this.mBackProgress = back;
        this.mProgress = spinnerLoader;

    }

    public void uploadCompleteInfo(final ArrayList<Recommend> recommends, Trip trip){

        UtilsView.showProgress(mProgress,mBackProgress);

        Backendless.Persistence.of(Trip.class).save( trip, new AsyncCallback<Trip>() {
            public void handleResponse( Trip response )
            {
                if (recommends == null){
                    UtilsView.hideProgress(mProgress,mBackProgress);
                   finishActivity();
                }else {
                    final int size = recommends.size();

                    for (int i = 0; i < size; i++) {
                        Backendless.Persistence.of(Recommend.class).save(recommends.get(i), new AsyncCallback<Recommend>() {
                            public void handleResponse(Recommend response) {
                            }

                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(mContext, fault.toString(), Toast.LENGTH_LONG).show();
                                UtilsView.hideProgress(mProgress,mBackProgress);
                            }
                        });

                        if (i == size - 1) {
                            UtilsView.hideProgress(mProgress,mBackProgress);
                            finishActivity();
                        }

                    }
                }
            }

            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                UtilsView.hideProgress(mProgress,mBackProgress);
            }
        });
    }

    public void uploadPhoto(Bitmap photo, String name) {
        Backendless.Files.Android.upload(photo,
                Bitmap.CompressFormat.PNG,
                100,
                name,
                Constants.PATH_PHOTOS,
                new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                        UtilsView.hideProgress(mProgress,mBackProgress);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        UtilsView.hideProgress(mProgress,mBackProgress);
                        Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((AddTripActivity) mContext).startActivityForResult(Intent.createChooser(intent,
                mContext.getString(R.string.select_file)), Constants.SELECT_FILE);
    }

    public Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(mContext.getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bm;
    }

    public Bitmap onCaptureImageResult(Bitmap bitmap) {
        Bitmap thumbnail = bitmap;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 40, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbnail;
    }


    public String setPhotoName(String title, String country){

        StringBuilder sb = new StringBuilder();
        sb.append(title.toLowerCase());
        sb.append("_");
        sb.append(country.toLowerCase());
        sb.append("_");
        sb.append(System.currentTimeMillis());
        sb.append(".png");
        return sb.toString().replace(" ","_");
    }

    private void finishActivity(){
        UtilsView.hideProgress(mProgress,mBackProgress);
        Toast.makeText(mContext, mContext.getString(R.string.success_data_uploaded), Toast.LENGTH_LONG).show();
        ((AddTripActivity)mContext).finish();
    }

    public Bitmap rotateImage(File file) {
        Bitmap correctBmp = null;
        try {
            ExifInterface ei = new ExifInterface(file.getAbsolutePath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(file),
                    null, null);
            Bitmap rotateBm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), mat, true);

            int height = (512 * rotateBm.getHeight())
                    / rotateBm.getWidth();

            correctBmp = Bitmap.createScaledBitmap(rotateBm,
                    512, height, true);

        } catch (IOException e) {
            Log.w("TAG", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- OOM Error in setting image");
        }

        return correctBmp;
    }

}
