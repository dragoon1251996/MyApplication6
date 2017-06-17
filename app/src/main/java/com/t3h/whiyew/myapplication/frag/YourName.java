package com.t3h.whiyew.myapplication.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.activity.HomeApp;
import com.t3h.whiyew.myapplication.model.Member;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Whiyew on 22/05/2017.
 */

public class YourName extends Fragment {
    private Button button;
    private EditText editText1,editText2,editText3,editText4,editText5;
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    ProgressBar bar;
    RelativeLayout relativeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.yourname,container,false);
        button=(Button)v.findViewById(R.id.button);
        preferences = getActivity().getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        editText1=(EditText)v.findViewById(R.id.edit1);
        editText2=(EditText)v.findViewById(R.id.edit2);
        editText3=(EditText)v.findViewById(R.id.edit3);
        editText4=(EditText)v.findViewById(R.id.edit4);
        editText5=(EditText)v.findViewById(R.id.edit5);
        bar = (ProgressBar) v.findViewById(R.id.progressBar);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.rl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                bar.setVisibility(View.VISIBLE);
                editor = preferences.edit();
                editor.putBoolean("RanBefore", true);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                DatabaseReference myRef = database.getReference(android_id);
                myRef.child("Intofmation").setValue(new Member(editText1.getText().toString(),
                        editText4.getText().toString(), editText2.getText().toString(), editText5.getText().toString(),
                        editText3.getText().toString())).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editor.commit();
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        HomeApp.active=false;
                        startActivity(intent);
                        relativeLayout.setVisibility(View.GONE);
                        bar.setVisibility(View.GONE);
                    }
                });


            }
        });
        return v;
    }

}
