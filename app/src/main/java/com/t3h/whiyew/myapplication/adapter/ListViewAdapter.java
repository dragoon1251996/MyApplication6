package com.t3h.whiyew.myapplication.adapter;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.frag.AddFragment;
import com.t3h.whiyew.myapplication.model.FromGroup;
import com.t3h.whiyew.myapplication.model.Member;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Member> animalNamesList = null;
    private ArrayList<Member> arraylist;
    String name;
    public ListViewAdapter(Context context, ArrayList<Member> animalNamesList,String name) {
        mContext = context;
        this.animalNamesList = animalNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Member>();
        this.arraylist.addAll(animalNamesList);
        this.name=name;
    }

    public class ViewHolder {
        TextView name;
        TextView gmail;
        ImageView add;
    }

    @Override
    public int getCount() {
        return animalNamesList.size();
    }

    @Override
    public Member getItem(int position) {
        return animalNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemmember, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.gmail = (TextView) view.findViewById(R.id.access);
            holder.add=(ImageView)view.findViewById(R.id.add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(animalNamesList.get(position).getName());
        holder.gmail.setText(animalNamesList.get(position).getGmail());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_clickbuttom));
                String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(AddFragment.id.get(position));
                myRef.child("Messenger").setValue("true");
                arraylist.remove(position);
                animalNamesList.remove(position);
                notifyDataSetChanged();
                myRef.child("Notification").push().setValue(new FromGroup(android_id, name));
                Toast.makeText(mContext,"Wait for Feedback!",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalNamesList.clear();
        if (charText.length() == 0) {
            animalNamesList.addAll(arraylist);
        } else {
            for (Member wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}