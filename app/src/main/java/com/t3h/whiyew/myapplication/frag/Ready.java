package com.t3h.whiyew.myapplication.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.whiyew.myapplication.R;

/**
 * Created by Whiyew on 22/05/2017.
 */

public class Ready extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.ready,container,false);
        return v;
    }
}
