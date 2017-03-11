package com.bigpigs.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bigpigs.CONSTANT;
import com.bigpigs.R;
import com.bigpigs.model.UserModel;
import com.bigpigs.view.RoundedImageView;

public class SystemManagementActivity extends AppCompatActivity {
    private RoundedImageView btAdd;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_management);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);

        initView();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý hệ thống sân");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void initView()
    {
        btAdd = (RoundedImageView) findViewById(R.id.bt_addSystem);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemManagementActivity.this,AddSystemPitchActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
            }
        });
    }
}
