package com.t3h.whiyew.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.model.Group;

import java.util.Random;

/**
 * Created by Whiyew on 21/05/2017.
 */

public class CreatGroup extends Activity {
    Button Creat;
    EditText Name, Access, Language;
    ProgressBar bar;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatgroup);
        Name = (EditText) findViewById(R.id.name);
        Access = (EditText) findViewById(R.id.access);
        Language = (EditText) findViewById(R.id.language);

        Creat = (Button) findViewById(R.id.creat);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        Creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().equals("")==false){
                    relativeLayout.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.VISIBLE);
                    final String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    String nameid = Name.getText().toString().replaceAll("\\s+","");
                    String language = Language.getText().toString();
                    String access = Access.getText().toString();
                    final String name = nameid + " " + android_id + " " + CreatGroup.random();
                    final Group group = new Group(name, language, access);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference(android_id);
                    final DatabaseReference allGroup = database.getReference("Groups");
                    allGroup.child(name).child("InforGroup").setValue(group, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){
                                allGroup.child(name).child("NameOfGroup").child(android_id).setValue(android_id, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError==null){
                                            myRef.child("Group").child(group.getName()).setValue(group, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    relativeLayout.setVisibility(View.GONE);
                                                    bar.setVisibility(View.GONE);
                                                    Toast.makeText(getBaseContext(), "You are Creat a group! ", Toast.LENGTH_LONG).show();
                                                    Intent refresh = new Intent(getBaseContext(), HomeApp.class);
                                                    startActivity(refresh);
                                                    finish();
                                                    overridePendingTransition(R.anim.changeractivity, R.anim.changer1);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getBaseContext(),"Name???",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10) + 10;
        int tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (int) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar + "");
        }
        return randomStringBuilder.toString();
    }

}
