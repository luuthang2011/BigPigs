/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigpigs.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigpigs.API;
import com.bigpigs.CONSTANT;
import com.bigpigs.R;
import com.bigpigs.fragments.NewsFragment;
import com.bigpigs.fragments.NotifcationFragment;
import com.bigpigs.fragments.OwnerFragment;
import com.bigpigs.fragments.PostNewsFragment;
import com.bigpigs.fragments.SearchFragment;
import com.bigpigs.fragments.SettingsFragment;
import com.bigpigs.fragments.SystemPitchsFragment;
import com.bigpigs.fragments.UserOrderManagement;
import com.bigpigs.model.News;
import com.bigpigs.model.SystemPitch;
import com.bigpigs.model.UserModel;
import com.bigpigs.support.NetworkUtils;
import com.bigpigs.support.ShowToast;
import com.bigpigs.support.TrackGPS;
import com.bigpigs.support.Utils;
import com.bigpigs.view.RoundedImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Provides UI for the main screen.
 */
public class MainActivity extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String TAG="MainActivity";
    private DrawerLayout mDrawerLayout;
    private TabLayout tabs;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private int permissionCode=1111;
    private TrackGPS gps;
    private LatLng currentLatLng;
    private GoogleApiClient mGoogleApiClient;
    private Bundle data;
    private UserModel userModel;
    private TextView tv_userName,tv_email;
    private RoundedImageView userAvatar;
    private SharedPreferences sharedPreferences;
    private View navHeader;
    private SearchFragment mSearchFragment;
    private boolean isOpened=false;
    private OkHttpClient okHttpClient;
    private String mString="hello";
    private ArrayList<SystemPitch> listSystem=new ArrayList<>();
    private ArrayList<News> listNews=new ArrayList<>();
    private String listSystemData="",listNewsData="";
    private String fcmToken ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();
        getData();
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},permissionCode);
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        if(fcmToken != null)
            updateToken(fcmToken);
        initNavMenu();
        initGoogleAPI();

        Log.d("Startup","Main Acitivty");
        Log.d("TYPE",userModel.getUserType());

        // neu mo ung dung tu thong bao
        if(getIntent().getBooleanExtra(CONSTANT.FROM_NOTIFICATION,false))
        {
            ShowToast.showToastLong(MainActivity.this,"from noticaiton");
            replaceFragment(NotifcationFragment.newInstance("", ""), NotifcationFragment.class.getName());
            mDrawerLayout.closeDrawers();
        }
    }
    private void getData() {
        listSystem = new ArrayList<>();
        listNews = new ArrayList<>();
        new MyTask().execute();

        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        data = getIntent().getBundleExtra("data");
        if (userModel != null) {
            tv_email.setText(userModel.getEmail());
            tv_userName.setText(userModel.getName());
        }
    }

    public void initView()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.container);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navHeader = navigationView.getHeaderView(0);
        tv_email = (TextView) navHeader.findViewById(R.id.user_email);
        tv_userName = (TextView) navHeader.findViewById(R.id.user_name);
        userAvatar = (RoundedImageView) navHeader.findViewById(R.id.user_avatar);

        Picasso.with(MainActivity.this).load(R.drawable.ic_avatar).fit().centerCrop().into(userAvatar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
//                    Log.d(TAG,listSystemData);
//                    Log.d(TAG,listNewsData);

                }
            });
        }

    }
    // update fcm token
    public void updateToken(String token)
    {
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        if(fcmToken != null)
        {
            HashMap<String,String> body = new HashMap<>();
            body.put("id",userModel.getId());
            body.put("tokenfcm",token);
            new UpdateToken(body).execute();
        }
    }
    public void replaceFragment(Fragment fragment,String tag)
    {

        tabs.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment,tag).commit();
    }
    public void backToFragment(final Fragment fragment) {

        // go back to something that was added to the backstack
        getSupportFragmentManager().popBackStackImmediate(
                fragment.getClass().getName(), 0);
        isOpened = true;
    }
    // init Nav menu
    public void initNavMenu()
    {
        if (navigationView != null) {
            if(userModel.getUserType().equals(UserModel.TYPE_OWNER)) {
                navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            // This method will trigger on item Click of navigation menu
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                menuItem.setChecked(true);
                                navigationView.setCheckedItem(menuItem.getItemId());

                                switch (menuItem.getItemId()) {
                                    case R.id.menu_home: {
                                        frameLayout.setVisibility(View.GONE);
                                        viewPager.setVisibility(View.VISIBLE);
                                        tabs.setVisibility(View.VISIBLE);
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_notification: {
                                        Utils.openDialog(MainActivity.this,"Chức năng hiện tại không khả dụng");

                                        break;
                                    }
                                    case R.id.menu_search: {
//                                    Log.d(TAG,);
                                        mSearchFragment = SearchFragment.newInstance(listSystemData, "");
                                        replaceFragment(mSearchFragment, mSearchFragment.getClass().getName());
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_manage: {
                                        replaceFragment(new OwnerFragment().newInstance("", ""), OwnerFragment.class.getName());
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_settings: {
                                        replaceFragment(new SettingsFragment().newInstance("", ""), NotifcationFragment.class.getName());
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_logout: {
                                        logoutDialog();
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }

                                }
                                return true;
                            }
                        });
            }
            else
            {
                navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            // This method will trigger on item Click of navigation menu
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                            ShowToast.showToastLong(MainActivity.this,mSystemPitchArrayList.size()+"");
                                menuItem.setChecked(true);
                                navigationView.setCheckedItem(menuItem.getItemId());

                                switch (menuItem.getItemId()) {
                                    case R.id.menu_home: {
                                        frameLayout.setVisibility(View.GONE);
                                        viewPager.setVisibility(View.VISIBLE);
                                        tabs.setVisibility(View.VISIBLE);
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_notification: {
                                        Utils.openDialog(MainActivity.this,"Chức năng hiện tại không khả dụng");
                                        break;
                                    }
                                    case R.id.menu_search: {
//                                    Log.d(TAG,);
                                        mSearchFragment = SearchFragment.newInstance(listSystemData, "");
                                        replaceFragment(mSearchFragment, mSearchFragment.getClass().getName());
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_manage: {
                                        replaceFragment(new UserOrderManagement(""), OwnerFragment.class.getName());
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_settings: {
                                        replaceFragment(new SettingsFragment().newInstance("", ""), NotifcationFragment.class.getName());
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                    case R.id.menu_logout: {
                                        logoutDialog();
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }

                                }
                                return true;
                            }
                        });
            }
        }
    }
    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        if(sharedPreferences !=null)
        {
            sharedPreferences.edit().clear().commit();
            sharedPreferences.edit().putBoolean("seen",true).commit();
        }
    }
    public void initGoogleAPI()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void logoutDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.logout2);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                dialog.dismiss();
                updateToken(" ");
            }
        });
        builder.create().show();
    }
    class UpdateToken extends AsyncTask<String,String,String>
    {
        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public UpdateToken(HashMap<String,String> body)
        {
            this.param=body;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                okHttpClient = new OkHttpClient();
                Response response = okHttpClient.newCall(NetworkUtils.createPutRequest(API.UpdateFCMToken+userModel.getId(),this.param)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("updatetoken", results);
                    return results;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "failed";
            }
            return "failed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            progressDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }

    }
    private class MyTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Request newsRequest = new Request.Builder()
                    .url(API.GetNews)
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response newsResponse = okHttpClient.newCall(newsRequest).execute();
                if(newsResponse.isSuccessful()) listNewsData = newsResponse.body().string();
                return "success";
            } catch (Exception e) {

                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals(null) || result.contains("failed")) {
                Utils.openDialog(MainActivity.this,"Đã có lỗi xảy ra, vui lòng thử lại sau");
            }
            else {
                progressDialog.dismiss();
                tabs = (TabLayout) findViewById(R.id.tabs);
                viewPager = (ViewPager) findViewById(R.id.viewpager);
                Adapter adapter = new Adapter(getSupportFragmentManager());
                adapter.addFragment(new SystemPitchsFragment(listSystemData), "Tìm nhanh");
                adapter.addFragment(new NewsFragment(listNewsData), "Tin tức");
                adapter.addFragment(PostNewsFragment.newInstance("", ""), "Đăng tin");
                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(3);
                tabs.setupWithViewPager(viewPager);
//            Log.d(TAG,listSystemData);
//            mSearchFragment = SearchFragment.newInstance(listSystemData,"");
//            replaceFragment(mSearchFragment, mSearchFragment.getClass().getName());
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {


            return true;
        }
        else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        return super.onOptionsItemSelected(item);
    }
}
