package com.t3h.whiyew.myapplication.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.adapter.ReciclerViewMember;
import com.t3h.whiyew.myapplication.model.Member;

import java.util.ArrayList;

/**
 * Created by Whiyew on 29/03/2017.
 */

public class FragmentGroup extends Fragment {
    private ReciclerViewMember lvMemberAdapter;
    private RecyclerView listView;
    private ArrayList<Member> arraylist;
    private ArrayList<String> idMember;
    private LinearLayout linearLayout;
    private boolean aBoolean = true;
    private int temp;
    private ArrayList<String> arrayId;

    private ArrayList<String> ID = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.allgroup, container, false);
        arrayId = new ArrayList<>();
        listView = (RecyclerView) v.findViewById(R.id.lv_group);
        arraylist = new ArrayList<>();
        lvMemberAdapter = new ReciclerViewMember(getContext(), arraylist);
        listView.setAdapter(lvMemberAdapter);
        String strtext = getArguments().getString("edttext");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Groups");
        myRef.child(strtext).child("NameOfGroup").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // arrayId.add(dataSnapshot.getValue().toString());
                DatabaseReference myRef1 = database.getReference(dataSnapshot.getValue().toString());
                myRef1.child("Intofmation").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Member member;
                        if (dataSnapshot.getValue(Member.class) == null) {
                            member = new Member(ID.get(temp), ID.get(temp) + ".com", "0", "English", "Việt Nam");
                        } else {
                            member = dataSnapshot.getValue(Member.class);
                        }
                        arraylist.add(member);
                        lvMemberAdapter.notifyDataSetChanged();
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        listView.setLayoutManager(mLayoutManager);
                        listView.setAdapter(lvMemberAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }


}
