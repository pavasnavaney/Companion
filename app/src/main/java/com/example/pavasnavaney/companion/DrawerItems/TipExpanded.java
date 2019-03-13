package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pavasnavaney.companion.R;

public class TipExpanded extends AppCompatActivity {

    String tit,con,ph;
    TextView title,content;
    ProgressBar progressBar;
    private Toolbar toolbar;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_expanded);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView)findViewById(R.id.title);
        content = (TextView)findViewById(R.id.textView3);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        photo = (ImageView)findViewById(R.id.photo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        tit = i.getStringExtra("title");
        con = i.getStringExtra("body");
        ph = i.getStringExtra("photo");
        getSupportActionBar().setTitle(tit);

        title.setText(tit);
        content.setText(con);
        Glide.with(this).load(ph).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(photo);
    }

    @Override
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
