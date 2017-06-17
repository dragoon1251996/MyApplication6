package com.t3h.whiyew.myapplication.frag;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.activity.Addmember;
import com.t3h.whiyew.myapplication.activity.Seen;
import com.t3h.whiyew.myapplication.adapter.ReciclerViewAdapter;
import com.t3h.whiyew.myapplication.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Whiyew on 26/03/2017.
 */

public class ManagerGroup extends Fragment {
    public static final String KEY_STRING = "KEY";
    ReciclerViewAdapter lvGroup;
    RecyclerView listView;
    List<Group> arrayList;
    ArrayList<String> arrayNameGroup;
    ArrayList<String> arrayId;
    String nameOfGroupClick;
    boolean checker = true;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        View v = inflater.inflate(R.layout.allgroup, container, false);
        View v = inflater.inflate(R.layout.allgroup, container, false);
        listView = (RecyclerView) v.findViewById(R.id.lv_group);
        arrayList = new ArrayList<>();
        arrayNameGroup = new ArrayList<>();
        arrayId = new ArrayList<>();

        lvGroup = new ReciclerViewAdapter(getContext(), arrayList);
        lvGroup.SetOnItemClickListener(new ReciclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
            }
        });

        listView.setAdapter(lvGroup);
        lvGroup.SetOnItemClickListener(new ReciclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View v = inflater.inflate(R.layout.custom_dialog, null);
                Button see=(Button)v.findViewById(R.id.btn_yes) ;
                Button add=(Button)v.findViewById(R.id.btn_no);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String text=arrayNameGroup.get(position);
                        Intent intent=new Intent(getContext(), Seen.class);
                        intent.putExtra(KEY_STRING,text);
                        startActivity(intent);
                    }
                });
                see.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String text=arrayNameGroup.get(position);
                        Intent intent=new Intent(getContext(),Addmember.class);
                        intent.putExtra(KEY_STRING,text);
                        startActivity(intent);
                    }
                });
                dialog.setCancelable(true);
                dialog.setContentView(v);
                dialog.show();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = database.getReference(android_id);
        myRef.child("Group").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getValue() != null) {
                    Group group = dataSnapshot.getValue(Group.class);
                    arrayNameGroup.add(group.getName());
                    String[] split = group.getName().split(" ");
                    arrayList.add(new Group(split[0], group.getLanguage(), group.getAccess()));
                    // lvGroup = new ReciclerViewAdapter(getContext(), arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    listView.setLayoutManager(mLayoutManager);
                 //   listView.setAdapter(lvGroup);
                    lvGroup.notifyDataSetChanged();
//                    lvGroup.SetOnItemClickListener(new ReciclerViewAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, final int position) {
//
//                            dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
//                            LayoutInflater inflater = LayoutInflater.from(getContext());
//                            View v = inflater.inflate(R.layout.custom_dialog, null);
//                            Button see=(Button)v.findViewById(R.id.btn_yes) ;
//                            Button add=(Button)v.findViewById(R.id.btn_no);
//
//                            see.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog.dismiss();
//                                    String text=arrayNameGroup.get(position);
//                                    Intent intent=new Intent(getContext(),Addmember.class);
//                                    intent.putExtra(KEY_STRING,text);
//                                    startActivity(intent);
//                                }
//                            });
//                            add.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            dialog.setCancelable(true);
//                            dialog.setContentView(v);
//                            dialog.show();
//                        }
//                    });
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

        return v;
    }

    class My extends AsyncTask<Void, Integer, Void> {
        Activity mContex;
        boolean temp = true;

        public My(Activity contex) {
            this.mContex = contex;
        }

        @Override
        protected Void doInBackground(Void... params) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            DatabaseReference myRef = database.getReference("Groups");
            myRef.child(nameOfGroupClick).child("NameOfGroup").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    arrayId.add(dataSnapshot.getValue().toString());
                    checker = false;
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
            int i = 0;
            while (temp == true) {
                if (checker == false) {
                    publishProgress(1);
                    break;
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checker == false) {
                checker = true;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentPanel, new MapFind());
                fragmentTransaction.commit();


            }
        }
    }

    class My1 extends AsyncTask<Void, Integer, Void> {
        Activity mContex;
        boolean temp = true;

        public My1(Activity contex) {
            this.mContex = contex;
        }

        @Override
        protected Void doInBackground(Void... params) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            DatabaseReference myRef = database.getReference("Groups");
            myRef.child(nameOfGroupClick).child("NameOfGroup").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    arrayId.add(dataSnapshot.getValue().toString());
                    checker = false;
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
            int i = 0;
            while (temp == true) {
                try {
                    Thread.sleep(500);
                    if (checker == false) {
                        publishProgress(1);
                        break;
                    }
                } catch (InterruptedException e) {

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checker == false) {
                checker = true;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentPanel, new MapFind());
                fragmentTransaction.commit();

            }
        }
    }
}