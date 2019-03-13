package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.pavasnavaney.companion.R;
import com.example.pavasnavaney.companion.TabFragments.TabList;
import com.example.pavasnavaney.companion.URLConstants.URList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Tutpoint extends AppCompatActivity {

    ListView listview;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListAdapterForTutorials adapter;
    String subject;
    private List<TabList> tablist = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutpoint);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Tutorial");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        subject=i.getStringExtra("subject");
        listview = (ListView)findViewById(R.id.list_view);
        tablist = new ArrayList<TabList>();
        Background_Tutorials back = new Background_Tutorials(Tutpoint.this);
        back.execute();
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

    public class Background_Tutorials extends AsyncTask<String,Void,String>
    {
        String final_res;
        String json_string;
        Context ctx;
        SweetAlertDialog sweetAlertDialog;

        public Background_Tutorials(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#FFA000"));
            sweetAlertDialog.setTitleText("Loading Videos...");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URList ur = new URList();
            String course_get_url = ur.tutloader;
            try {
                URL url = new URL(course_get_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url
                        .openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("subject", "UTF-8") + "="
                        + URLEncoder.encode(subject, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            sweetAlertDialog.dismissWithAnimation();
            try {
                final_res = result;
                jsonObject = new JSONObject(final_res);
                jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                if(jsonArray.length()==0)
                {

                }
                else {
                    while (count < jsonArray.length()) {
                        TabList map = new TabList();
                        JSONObject explore = jsonArray.getJSONObject(count);
                        map.setTitle((String) explore.get("title"));
                        map.setContents((String) explore.get("contents"));
                        String down = explore.getString("download");
                        map.setDownlink(down);
                        tablist.add(map);
                        adapter = new ListAdapterForTutorials(Tutpoint.this, tablist);
                        listview.setAdapter(adapter);
                        count++;
                    }
                }
            }
            catch (Exception e) {
                sweetAlertDialog = new SweetAlertDialog(ctx,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Some Network Error Occurred! Please Check Your Internet Connection Or Try Again Later!");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();
            }
        }
    }
}
