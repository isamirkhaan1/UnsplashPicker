package com.samirk433.unsplashpicker.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samirk433.unsplashpicker.OnPhotoSelection;
import com.samirk433.unsplashpicker.UnsplashPicker;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ImageView imageView = findViewById(R.id.imageView);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UnsplashPicker.show(MainActivity.this, new OnPhotoSelection() {
                    @Override
                    public void onPhotoSelect(String url) {

                        if (url == null) return;
                        Glide.with(MainActivity.this)
                                .load(url)
                                .into(imageView);
                    }
                });
            }
        });

    }
}
