package com.bigpigs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bigpigs.CONSTANT;
import com.bigpigs.R;
import com.bigpigs.main.OrderManagementActivity;
import com.bigpigs.main.PitchManagementActivity;
import com.bigpigs.main.PriceManagementActivity;
import com.bigpigs.model.UserModel;
import com.bigpigs.support.Utils;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class OwnerFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "NewsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btManageOrder,btManagePitch,btManageSystem,btManagePrice;
    private UserModel userModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_owner, container, false);
        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        initView(rootView);
        return rootView;
    }

    public void initView(View v)
    {
        btManageOrder = (Button) v.findViewById(R.id.bt_manageOrder);
        btManagePitch = (Button) v.findViewById(R.id.btManagePitch);
        btManageSystem = (Button) v.findViewById(R.id.bt_manageSystem);
        btManagePrice = (Button) v.findViewById(R.id.bt_managerPrice);

        btManagePrice.setOnClickListener(this);
        btManageSystem.setOnClickListener(this);
        btManagePitch.setOnClickListener(this);
        btManageOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btManagePitch :
            {
                Intent intent = new Intent(getActivity(), PitchManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                break;
            }
            case R.id.bt_manageOrder :
            {
                Intent intent = new Intent(getActivity(), OrderManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                break;
            }
            case R.id.bt_manageSystem :
            {
//                Intent intent = new Intent(getActivity(), SystemManagementActivity.class);
//                intent.putExtra(CONSTANT.KEY_USER,userModel);
//                startActivity(intent);
                Utils.openDialog(getContext(),"Chức năng này chưa khả dụng");
                break;
            }
            case R.id.bt_managerPrice :
            {
                Intent intent = new Intent(getActivity(), PriceManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                break;
            }
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public OwnerFragment() {

    }
    public static OwnerFragment newInstance(String param1, String param2) {
        OwnerFragment fragment = new OwnerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


}

