package com.t3h.whiyew.myapplication.frag;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.adapter.ReciclerViewAdapter;
import com.t3h.whiyew.myapplication.custom.ExpandableLayout;
import com.t3h.whiyew.myapplication.model.FromGroup;
import com.t3h.whiyew.myapplication.model.Group;
import com.t3h.whiyew.myapplication.model.Member;

import java.util.ArrayList;

/**
 * Created by Whiyew on 30/03/2017.
 */

public class NotificaTion extends Fragment {

    ListView listView;
    //    AdapterNotificaion adapterNotificaion;
    ArrayList<FromGroup> arrayList;
    public static ArrayList<FromGroup> arr;
    SimpleAdapter adapter;
    View v;
    public static ArrayList<String> idpush;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.notification, container, false);
//        listView = (ListView) v.findViewById(R.id.lv_group);
//        arrayList = new ArrayList<>();
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//        DatabaseReference myRef = database.getReference(android_id);
//        myRef.child("Notification").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        FromGroup fromGroup=dataSnapshot.getValue(FromGroup.class);
//                String[] name=fromGroup.getNamegroup().split(" ");
//                        arrayList.add(new FromGroup(name[0],fromGroup.getNamefrom()));
//                        adapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter= new SimpleAdapter(recyclerView, arrayList);
//        recyclerView.setAdapter(adapter);
        return v;
    }

    public void refresh() {
        listView = (ListView) v.findViewById(R.id.lv_group);
        idpush = new ArrayList<>();
        arrayList = new ArrayList<>();
        arr = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = database.getReference(android_id);
        myRef.child("Notification").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FromGroup fromGroup = dataSnapshot.getValue(FromGroup.class);
                arr.add(fromGroup);
                String[] name = fromGroup.getNamegroup().split(" ");
                arrayList.add(new FromGroup(name[0], fromGroup.getNamefrom()));
                idpush.add(dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();
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
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SimpleAdapter(getContext(), recyclerView, arrayList);
        recyclerView.setAdapter(adapter);
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;

        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;
        private ArrayList<FromGroup> listData;
        private LayoutInflater mLayoutInflater;
        private Context mContext;
        ReciclerViewAdapter.OnItemClickListener mItemClickListener;

        public SimpleAdapter(Context context, RecyclerView recyclerView, ArrayList<FromGroup> a) {
            this.recyclerView = recyclerView;
            this.listData = a;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
            private ExpandableLayout expandableLayout;
            private LinearLayout expandButton;
            TextView name, group;
            private int position;
            ImageView yes, no;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                group = (TextView) itemView.findViewById(R.id.access);
                yes = (ImageView) itemView.findViewById(R.id.yes);
                no = (ImageView) itemView.findViewById(R.id.no);

                expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                expandableLayout.setOnExpansionUpdateListener(this);
                expandButton = (LinearLayout) itemView.findViewById(R.id.expand_button);
                expandButton.setOnClickListener(this);
            }

            public void bind(final int position) {
                this.position = position;
                //   name.setText(listData.get(position).getNamegroup());

                name.setText(listData.get(position).getNamefrom());
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database.getReference(listData.get(position).getNamegroup());
                myRef1.child("Intofmation").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        group.setText(dataSnapshot.getValue(Member.class).getName().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        DatabaseReference myRef = database.getReference(android_id);
                        myRef.child("Group").child(arr.get(position).getNamegroup()).setValue(new Group(arr.get(position).getNamegroup(), "", ""));
                        DatabaseReference databaseReference = database.getReference("Groups");
                        databaseReference.child(arr.get(position).getNamegroup()).child("NameOfGroup").child(android_id).setValue(android_id);
                        myRef.child("Notification").child(NotificaTion.idpush.get(position)).removeValue();
                        listData.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "You are join Group! ", Toast.LENGTH_LONG).show();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        DatabaseReference myRef = database.getReference(android_id);
                        myRef.child("Notification").child(NotificaTion.idpush.get(position)).removeValue();
                        listData.remove(position);
                        notifyDataSetChanged();

                    }
                });
                expandButton.setSelected(false);
                expandableLayout.collapse(false);
            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                }

                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    expandButton.setSelected(true);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }

            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout", "State: " + state);
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }
}
