package com.t3h.whiyew.myapplication.frag;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.adapter.ListViewAdapter;
import com.t3h.whiyew.myapplication.model.Member;

import java.util.ArrayList;

/**
 * Created by Whiyew on 29/03/2017.
 */

public class AddFragment extends Fragment implements SearchView.OnQueryTextListener {
    boolean aBoolean = true;
    private ListView lv;
    private SearchView sv;
    private int temp;
    public static ArrayList<String> id = new ArrayList<>();
    private String[] teams = {"Man Utd", "Man City", "Chelsea", "Arsenal", "Liverpool", "Totenham"};
    private ArrayList<Member> member;
    //    ArrayAdapter<String> AdapterAdd;
    private ListViewAdapter adapter;
    String strtext;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.addmember, container, false);
        strtext = getArguments().getString("edttext");
        return v;
    }

    public void Load() {
        member = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.myCustomListView);
        sv = (SearchView) v.findViewById(R.id.mySearchView);
        new Load(getActivity()).execute();
    }

    public void filter() {
        sv.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    class Load extends AsyncTask<Void, Void, Void> {
        Activity mContex;

        public Load(Activity mContex) {
            this.mContex = mContex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            strtext = getArguments().getString("edttext");
            boolean cher = true;
            final String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference();
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getKey().equals("Groups") == false
                            && dataSnapshot.getKey().equals(android_id) == false
                            ) {
                        id.add(dataSnapshot.getKey());
                        DatabaseReference myRef1 = database.getReference(dataSnapshot.getKey());

                        myRef1.child("Intofmation").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Member members;
                                if (dataSnapshot.getValue(Member.class) == null) {
                                    members = new Member(id.get(temp), id.get(temp) + ".com", "0", "English", "Việt Nam");
                                } else {
                                    members = dataSnapshot.getValue(Member.class);
                                }
                                member.add(members);
                                aBoolean = false;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

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
            while (cher == true) {
                try {
                    Thread.sleep(500);
                    if (aBoolean == false) {
                        break;
                    }
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ListViewAdapter(getContext(), member, strtext);
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
            filter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
