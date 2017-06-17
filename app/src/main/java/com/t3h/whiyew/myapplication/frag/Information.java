package com.t3h.whiyew.myapplication.frag;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.model.Member;

/**
 * Created by Whiyew on 22/05/2017.
 */

public class Information extends Fragment {
    Member member;
    private TextView editText1,editText2,editText3,editText4,editText5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.infor,container,false);
        editText1=(TextView) v.findViewById(R.id.edit1);
        editText2=(TextView)v.findViewById(R.id.edit2);
        editText3=(TextView)v.findViewById(R.id.edit3);
        editText4=(TextView)v.findViewById(R.id.edit4);
        editText5=(TextView)v.findViewById(R.id.edit5);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final DatabaseReference myRef = database.getReference(android_id);
        myRef.child("Intofmation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Member.class) == null) {
                    member = new Member(android_id, android_id + ".com", "0", "English", "Việt Nam");
                    myRef.child("Intofmation").setValue(member);
                } else {
                    member = dataSnapshot.getValue(Member.class);
                    editText1.setText(member.getName());
                    editText2.setText(member.getAge());
                    editText3.setText(member.getAddress());
                    editText4.setText(member.getGmail());
                    editText5.setText(member.getLanguage());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }
}
