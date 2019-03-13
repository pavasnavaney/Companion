package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavasnavaney.companion.R;

public class Contact_Us extends AppCompatActivity {

    TextView call,msg;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        call =(TextView)findViewById(R.id.call);
        msg = (TextView)findViewById(R.id.whatsapp);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"9910335755"));
                startActivity(intent);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendSMS();
            }
        });
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

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("9910335755"));
        smsIntent.putExtra("sms_body"  , "Hello I'm Interested To Join Companion , Kindly Contact Me Back! :)");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Contact_Us.this,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
