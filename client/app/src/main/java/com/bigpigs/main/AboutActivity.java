package com.bigpigs.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigpigs.R;
import com.bigpigs.model.Pitch;

public class AboutActivity extends AppCompatActivity {

    ImageView imageView;
    TextView namePitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


    }
}
