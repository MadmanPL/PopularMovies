package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView m_ivImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_ivImage = (ImageView)findViewById(R.id.iv_image);

        Context context = this;
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(m_ivImage);
    }
}
