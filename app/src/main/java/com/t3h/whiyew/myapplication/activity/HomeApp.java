package com.t3h.whiyew.myapplication.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.adapter.MyFragmentPagerAdapter;
import com.t3h.whiyew.myapplication.adapter.SimpleFragmentAdapter;
import com.t3h.whiyew.myapplication.broadcast.Erro;
import com.t3h.whiyew.myapplication.broadcast.GetMessenger;
import com.t3h.whiyew.myapplication.broadcast.ReService;
import com.t3h.whiyew.myapplication.custom.ExtensiblePageIndicator;
import com.t3h.whiyew.myapplication.custom.FloatingActionButton;
import com.t3h.whiyew.myapplication.custom.FloatingActionsMenu;
import com.t3h.whiyew.myapplication.frag.MapFind;
import com.t3h.whiyew.myapplication.frag.Hello;
import com.t3h.whiyew.myapplication.frag.Information;
import com.t3h.whiyew.myapplication.frag.ManagerGroup;
import com.t3h.whiyew.myapplication.frag.NotificaTion;
import com.t3h.whiyew.myapplication.frag.YourName;
import com.t3h.whiyew.myapplication.service.ChatHeadService;

import java.util.List;
import java.util.Vector;

/**
 * Created by Whiyew on 17/05/2017.
 */

public class HomeApp extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    SharedPreferences preferences ;
    private MyFragmentPagerAdapter myViewPagerAdapter;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;
    private ViewPager viewPager;
    private FloatingActionsMenu floatingActionsMenu;
    private SimpleFragmentAdapter mSimpleFragmentAdapter;
    private ViewPager mViewPager;
    ExtensiblePageIndicator extensiblePageIndicator;
    List<Fragment> fragments;
    private PendingIntent pendingIntent,pendingIntent1,pendingIntent2;
    public static HomeApp activityA;
    public static HomeApp getInstance(){
        return   activityA;
    }
    public static boolean active = false;
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(HomeApp.active==true){
            finish();
        }
        activityA=this;
        preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            setContentView(R.layout.start);
            extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
            mSimpleFragmentAdapter = new SimpleFragmentAdapter(getSupportFragmentManager());

            mSimpleFragmentAdapter.addFragment(new Hello());
            mSimpleFragmentAdapter.addFragment(new YourName());

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSimpleFragmentAdapter);
            extensiblePageIndicator.initViewPager(mViewPager);


//            imageButton=(ImageButton)findViewById(R.id.btn_next);
//            imageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("RanBefore", true);
//                    editor.commit();
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                }
//            });
        } else {
            setContentView(R.layout.home);
            startmessenger();
            upLocation();
            Warnning();
            ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(getResources().getColor(R.color.white));
            floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
            floatingActionButton = (FloatingActionButton) findViewById(R.id.action_a);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    floatingActionsMenu.expand();
                    floatingActionsMenu.collapse();
                    startActivity(new Intent(getApplicationContext(), CreatGroup.class));
                    finish();
                    overridePendingTransition(R.anim.changeractivity, R.anim.changer1);
                }
            });
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.warning);
            tabLayout.getTabAt(1).setIcon(R.drawable.grouping);
            tabLayout.getTabAt(2).setIcon(R.drawable.notificationing);
            tabLayout.getTabAt(3).setIcon(R.drawable.inforing);
        }
    }
    private void startmessenger(){
        long interval = 1000;
        Intent intent=new Intent(this, GetMessenger.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + interval, 1000,
                pendingIntent);
    }
    private void upLocation(){
        long interval = 1000;
        Intent intent=new Intent(this, ReService.class);
        pendingIntent1=PendingIntent.getBroadcast(this,1,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + interval, 5000,
                pendingIntent1);
    }

//    private boolean isFirstTime() {
//        boolean ranBefore = preferences.getBoolean("RanBefore", false);
//        if (!ranBefore) {
//            // first time
//            setContentView(R.layout.start);
//            imageButton=(ImageButton)findViewById(R.id.btn_next);
//            imageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("RanBefore", true);
//                    editor.commit();
//                }
//            });
//
//
//        }
//        return !ranBefore;
//    }
private void Warnning(){
    long interval = 1000;
    Intent intent=new Intent(this, Erro.class);
    pendingIntent2=PendingIntent.getBroadcast(this,2,intent,0);
    AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + interval, 5000,
            pendingIntent2);
}
    private void setupViewPager(ViewPager viewPager) {
        fragments = new Vector<Fragment>();

        fragments.add(new MapFind());
        fragments.add(new ManagerGroup());
        fragments.add(new NotificaTion());
        fragments.add(new Information());
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
       if(position==2) ((NotificaTion)myViewPagerAdapter.getItem(position)).refresh();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(HomeApp.this, ChatHeadService.class));
    }

}
