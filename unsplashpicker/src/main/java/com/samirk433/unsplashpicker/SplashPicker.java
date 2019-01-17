package com.samirk433.unsplashpicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashPicker extends DialogFragment implements View.OnClickListener {
    private static final String TAG = SplashPicker.class.getSimpleName();

    /*
     * View instances
     * */
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private ImageView mImgClose;
    private TextView mTvLoadMore;
    private GridViewAdapter mAdapter;


    /*
     * Java instances
     * */
    private int mPageNo = 1;
    private List<Photo> mPhotos;
    private String mApiKey;


    //photo selection call-back
    private static OnPhotoSelection sOnPhotoSelection;


    /**
     * @param context
     * @param onPhotoSelection
     */
    public static void show(@NonNull Context context, @NonNull OnPhotoSelection onPhotoSelection) {

        sOnPhotoSelection = onPhotoSelection;

        //show dialog
        SplashPicker dialog = new SplashPicker();
        dialog.show(((FragmentActivity) context).getSupportFragmentManager(), TAG);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.splash_picker, null);
        initViews(view);
        readApiKey();
        getPhotos();

        dialog.setView(view);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*
         * for loading dialog in full view
         * */

        Dialog dialog = getDialog();

        //for some unknown reasons sometimes dialog is NULL
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.img_close) {
            dismiss();
        } else if (id == R.id.tv_next) {
            mTvLoadMore.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            getPhotos();
        }
    }


    private void initViews(View view) {

        mGridView = view.findViewById(R.id.grid_images);
        mProgressBar = view.findViewById(R.id.progressBar);
        mTvLoadMore = view.findViewById(R.id.tv_next);
        mImgClose = view.findViewById(R.id.img_close);

        mImgClose.setOnClickListener(this);
        mTvLoadMore.setOnClickListener(this);
    }


    /**
     * Read API_KEY from Manifest file
     */
    private void readApiKey() {

        try {

            ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo(
                    getContext().getPackageName(), PackageManager.GET_META_DATA);



            //read meta-data from app manifest
            Bundle bundle = applicationInfo.metaData;

            if (bundle == null)
                throw new IllegalAccessException(Constants.getExceptionMsg());


            mApiKey = bundle.getString(Constants.EXTRA_API_KEY, null);

            //show error if no api key is found
            if (mApiKey == null)
                throw new IllegalAccessException(Constants.getExceptionMsg());


        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

    }

    private void updateUi() {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            updateAdapter();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateAdapter();
                }
            });
        }
    }

    private void updateAdapter() {
        mTvLoadMore.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);


        mAdapter = new GridViewAdapter(getContext(), mPhotos, new OnPhotoSelection() {
            @Override
            public void onPhotoSelect(String url) {

                sOnPhotoSelection.onPhotoSelect(url);
                dismiss();
            }
        });
        mGridView.setAdapter(mAdapter);
    }

    private void showToast(final String msg) {
        Log.d(TAG, String.format("showThreadOnUiThread(%SplashPicker)", msg));


        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    /**
     * GET REQUEST - For popular images from UnSplash
     */
    private void getPhotos() {

        //final URL
        String url = Constants.BASE_URL + "photos?page=" + mPageNo + "&order_by=popular&client_id=" + mApiKey;

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, final IOException e) {

                        //toast error message to user
                        showToast(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        //init and append photos to the list
                        if (mPhotos == null)
                            mPhotos = new ArrayList<>();

                        String res = response.body().string();
                        Gson gson = new Gson();

                        try {
                            Photo[] array = gson.fromJson(res, Photo[].class);

                            //add photos to list
                            mPhotos.addAll(Arrays.asList(array));

                            //increment page # for more loading
                            mPageNo++;

                            //update UI
                            updateUi();
                        } catch (IllegalStateException e) {
                            Log.e(TAG, Constants.getExceptionMsg());
                        }

                    }
                });
    }
}
