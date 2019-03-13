package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pavasnavaney.companion.R;
import com.example.pavasnavaney.companion.TabFragments.Notes;
import com.example.pavasnavaney.companion.TabFragments.Papers;

import java.util.ArrayList;
import java.util.List;

public class DrawerFBL extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    String subject,course,semester;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent i = getIntent();
        subject = i.getStringExtra("subject");
        course = i.getStringExtra("course");
        semester=i.getStringExtra("semester");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subject);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterfbl adapter = new ViewPagerAdapterfbl(getSupportFragmentManager());
        adapter.addFragment(new Notes(subject,semester,course), "Notes");
        adapter.addFragment(new Papers(subject,semester,course), "Papers");
        viewPager.setAdapter(adapter);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        super.onBackPressed();
    }
}
class ViewPagerAdapterfbl extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapterfbl(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}