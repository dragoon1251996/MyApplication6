package com.t3h.whiyew.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.model.Member;

import java.util.List;

/**
 * Created by Whiyew on 08/06/2017.
 */

public class ReciclerViewMember extends RecyclerView.Adapter<ReciclerViewMember.MyViewHolder> {
    int lastPosition = -1;
    private List<Member> listData;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public ReciclerViewMember(Context context, List<Member> listData) {
        this.listData = listData;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateList(List<Member> data) {
        listData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.item_group, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ReciclerViewMember.MyViewHolder holder, int position) {
        Member movie = listData.get(position);
        holder.title.setText(movie.getName());
        holder.year.setText(movie.getGmail());
        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        } else {
            if (position < lastPosition) {
                Animation animation1 = AnimationUtils.loadAnimation(mContext,
                        R.anim.anim_1);
                holder.itemView.startAnimation(animation1);
                lastPosition = position;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    /**
     * ViewHolder for item view of list
     */

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title, year;

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.name);
            year = (TextView) itemView.findViewById(R.id.access);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        listData.clear();
//        if (charText.length() == 0) {
//            listData.addAll(arraylist);
//        } else {
//            for (Member wp : arraylist) {
//                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    listData.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

}
