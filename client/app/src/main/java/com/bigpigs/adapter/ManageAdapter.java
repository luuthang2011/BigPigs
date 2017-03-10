package com.bigpigs.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigpigs.R;

import java.util.ArrayList;


public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.RecyclerViewHolder> {
    public static String TAG="ManageAdapter";
    public ArrayList<String> list;
    public Activity context;
    public ManageAdapter(Activity context, ArrayList<String> listData) {
        this.context = context;
        this.list = listData;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemview = inflater.inflate(R.layout.item_manage_order, parent, false);
            return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

    }


}
