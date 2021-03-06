package com.bigpigs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigpigs.CONSTANT;
import com.bigpigs.R;
import com.bigpigs.main.EditPriceActivity;
import com.bigpigs.model.Price;
import com.bigpigs.support.Utils;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=PriceAdapter.class.getName();
    private ArrayList<Price> data;
    private LayoutInflater inflater;

    public PriceAdapter(Context context, ArrayList<Price> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public PriceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_price, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_des.setText(data.get(position).getDescription());

        if(data.get(position).getDayOfWeek().equals("1"))
        holder.tv_date.setText("Ngày nghỉ");
        else holder.tv_date.setText("Ngày thường");


        holder.tv_time.setText(data.get(position).getTime());
        holder.tv_price.setText(data.get(position).getPrice());
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPriceActivity.class);
                intent.putExtra(CONSTANT.PRICE,data.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();

            }
});
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDialog(context,"Chức năng hiện chưa khả dụng");
            }
        });

    }


    @Override
    public int getItemCount() {

        return data.size();
    }

    private Price getPitch(int position){

        return data.get(position);
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_name;
        TextView tv_time,tv_des,tv_price,tv_date;
        LinearLayout wrapper;
        Button btEdit,btDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_des = (TextView) itemView.findViewById(R.id.item_description);
            tv_time = (TextView) itemView.findViewById(R.id.item_time);
            tv_date = (TextView) itemView.findViewById(R.id.item_date);
            tv_price = (TextView) itemView.findViewById(R.id.item_price);
            btEdit = (Button) itemView.findViewById(R.id.bt_edit);
            btDelete = (Button) itemView.findViewById(R.id.bt_delete);
        }


    }
}
