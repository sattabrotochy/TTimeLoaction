package com.shuvo.ttimeloaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttimeloaction.Class.DataModel;

import java.util.ArrayList;

public class LoactionAdapter extends RecyclerView.Adapter<LoactionAdapter.MyViewHolder> {

    private int lastPosition = -1;
    Context context;
    ArrayList<DataModel> dataModelArrayList;
    public  LoactionAdapter( Context context,ArrayList<DataModel> dataModelArrayList)
    {
        this.context=context;
        this.dataModelArrayList=dataModelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_location,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        DataModel model=dataModelArrayList.get(position);
        holder.location.setText(model.getLoaction());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());

        setAnimation(holder.itemView,position);


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView location,date,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            location=itemView.findViewById(R.id.userLocation);
            date=itemView.findViewById(R.id.userDate);
            time=itemView.findViewById(R.id.userTime);


        }
    }
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.setDuration(550);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;

        }
    }
}
