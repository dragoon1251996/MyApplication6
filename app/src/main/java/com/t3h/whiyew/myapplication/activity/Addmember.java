package com.t3h.whiyew.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.adapter.MyFragmentPagerAdapter;
import com.t3h.whiyew.myapplication.adapter.SimpleFragmentAdapter;
import com.t3h.whiyew.myapplication.custom.ExtensiblePageIndicator;
import com.t3h.whiyew.myapplication.frag.AddFragment;
import com.t3h.whiyew.myapplication.frag.FragmentGroup;
import com.t3h.whiyew.myapplication.frag.ManagerGroup;

import java.util.List;
import java.util.Vector;

/**
 * Created by Whiyew on 08/06/2017.
 */

public class Addmember extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter myViewPagerAdapter;
    private SimpleFragmentAdapter mSimpleFragmentAdapter;
    ExtensiblePageIndicator extensiblePageIndicator;
    private TabLayout tabLayout;
    String namegroupcheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Intent intent = getIntent();
        namegroupcheck = intent.getStringExtra(ManagerGroup.KEY_STRING);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.groupc);
        tabLayout.getTabAt(1).setIcon(R.drawable.plusmember);

//        Toast.makeText(this,a+"",Toast.LENGTH_SHORT).show();

    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragments = new Vector<Fragment>();
        Bundle bundle = new Bundle();
        bundle.putString("edttext", namegroupcheck);
// set Fragmentclass Arguments
        AddFragment add = new AddFragment();
        FragmentGroup fragment = new FragmentGroup();
        fragment.setArguments(bundle);
        add.setArguments(bundle);
        fragments.add(fragment);
        fragments.add(add);
        this.myViewPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(this.myViewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setPivotX(position < 0f ? page.getWidth() : 0f);
                page.setPivotY(page.getHeight() * 0.5f);
                page.setRotationY(90f * position);

            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            ((AddFragment)myViewPagerAdapter.getItem(position)).Load();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
