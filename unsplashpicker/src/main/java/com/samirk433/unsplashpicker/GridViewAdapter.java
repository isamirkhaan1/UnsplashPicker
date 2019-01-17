package com.samirk433.unsplashpicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

class GridViewAdapter extends BaseAdapter {
    private final String TAG = GridViewAdapter.class.getSimpleName();

    private Context mContext;

    /*
     * List of Photos
     * */
    private List<Photo> mPhotos;


    /*
     * Interface instance for list item selection callback
     * */
    private OnPhotoSelection mOnPhotoSelection;


    public GridViewAdapter(Context context, List<Photo> list) {
        this.mContext = context;
        this.mPhotos = list;
    }

    public GridViewAdapter(Context context, List<Photo> list, OnPhotoSelection onPhotoSelection) {
        this.mContext = context;
        this.mOnPhotoSelection = onPhotoSelection;
        this.mPhotos = list;
    }


    @Override
    public int getCount() {
        return mPhotos == null ? 0 : mPhotos.size();
    }

    @Override
    public Object getItem(int i) {
        return mPhotos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view;
        if (convertView == null) {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_photo, null);
        } else {
            view = convertView;
        }


        // centerCrop() is used because it doesn't stretch the image

        ImageView img = view.findViewById(R.id.img_thumb);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.img_loading);
        options.error(R.drawable.img_error);
        options.centerCrop();

        Glide.with(mContext).load(mPhotos.get(position).urls.thumb)
                .apply(options).into(img);


        TextView tvPhotographer = view.findViewById(R.id.tv_photographer);
        tvPhotographer.setText(mPhotos.get(position).userInfo.name);

        /*
         * On photographer name click,
         * redirect user to the photographer UnSplash profile via the default browser
         * */
        tvPhotographer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mPhotos.get(position).userInfo.getUrl()));
                mContext.startActivity(intent);

            }
        });



        /*
         * on photo click
         * 1. activate the callback
         * 2. finish the dialog
         * */

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //if  image is not loaded
                //do nothing
                if (mOnPhotoSelection == null) {
                    Log.e(TAG, "No listener is registered");
                } else {
                    mOnPhotoSelection.onPhotoSelect(mPhotos.get(position).getUrl());
                }
            }
        });

        return view;
    }
}
