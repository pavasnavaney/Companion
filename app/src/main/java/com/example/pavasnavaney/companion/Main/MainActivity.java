package com.example.pavasnavaney.companion.Main;


import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;


import com.example.pavasnavaney.companion.IntroManager.ListAdapterForSubject;
import com.example.pavasnavaney.companion.IntroManager.Subject_Loader;
import com.example.pavasnavaney.companion.R;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar toolbar;
    private Spinner semlist;
    private ListView listview;
    private ArrayList<Semester> semesterlist;
    String name, email, course;
    String Titles[] = {"English", "Behavioural Science", "Foreign Business Language", "Tutorials", "Exam Tips", "Change Department", "Contact Us"};
    int icons[] = {R.drawable.english, R.drawable.bs, R.drawable.fbl, R.drawable.tutorials, R.drawable.idea, R.drawable.department, R.drawable.contactus};
    RecyclerView mRecyclerView;
    String semester;
    ListAdapterForSubject adapter;
    private List<Subject_Loader> sublist =null;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        SharedPreferences pref =  getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        name = pref.getString("name",null);
        email = pref.getString("email",null);
        course = pref.getString("course",null);
        getSupportActionBar().setTitle(course);
       semlist = (Spinner)findViewById(R.id.spinner);
        sublist = new ArrayList<Subject_Loader>();
        semlist.setOnItemSelectedListener(this);
        semesterlist= new ArrayList<Semester>();
       listview = (ListView) findViewById(R.id.list_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DrawerAdapter(Titles, icons, name, email,this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        Drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        Background_Semester back = new Background_Semester(MainActivity.this);
        back.execute();
    }


    public class Background_Semester extends AsyncTask<String,Void,String> {

    String json_string;
    Context ctx;
    String final_string;
    SweetAlertDialog sweetAlertDialog;

    public Background_Semester(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#E64A19"));
        sweetAlertDialog.setTitleText("Loading Semesters...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        URList ur = new URList();
        String course_get_url = ur.semester_selector;
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
            String data = URLEncoder.encode("course", "UTF-8") + "="
                    + URLEncoder.encode(course, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            os.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while ((json_string = bufferedReader.readLine()) != null) {
                stringBuilder.append(json_string + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();
        } catch (MalformedURLException | ProtocolException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
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
            final_string = result;
            jsonObject = new JSONObject(final_string);
            jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while (count < jsonArray.length())
            {
                JSONObject explore = jsonArray.getJSONObject(count);
                Semester s = new Semester(explore.getString("Semester"));
                semesterlist.add(s);
                count++;
            }
            populate();
        } catch (Exception e) {
            sweetAlertDialog = new SweetAlertDialog(ctx,SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Some Network Error Occurred! Please Check Your Internet Connection Or Try Again Later!");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }
    }
}
    public void populate()
    {
        List<String> lables = new ArrayList<String>();
        for (int i = 0; i < semesterlist.size(); i++) {
            lables.add(semesterlist.get(i).getSemester());
        }
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                           R.layout.main_spinner,lables);
        adapter_state.setDropDownViewResource(R.layout.spinnerdropdown);
        semlist.setAdapter(adapter_state);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        semester = parent.getItemAtPosition(position).toString();
        Background_Subjects back = new Background_Subjects(MainActivity.this);
        back.execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class Background_Subjects extends AsyncTask<String,Void,String> {

        String final_res;
        String json_string;
        Context ctx;
        SweetAlertDialog sweetAlertDialog;

        public Background_Subjects(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#E64A19"));
            sweetAlertDialog.setTitleText("Loading Subjects...");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URList ur = new URList();
            String course_get_url = ur.subject_selector;
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
                String data = URLEncoder.encode("course", "UTF-8") + "="
                        + URLEncoder.encode(course, "UTF-8") + "&"
                        + URLEncoder.encode("semester", "UTF-8") + "="
                        + URLEncoder.encode(semester, "UTF-8");
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
                String subject;
                sublist.clear();
                final_res = result;
                jsonObject = new JSONObject(final_res);
                jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                while (count < jsonArray.length()) {
                    Subject_Loader map = new Subject_Loader();
                    JSONObject jo = jsonArray.getJSONObject(count);
                    subject=jo.getString("Semester");
                    map.setSemester(semester);
                    map.setCourse(course);
                    map.setSubject(subject);
                    sublist.add(map);
                    adapter = new ListAdapterForSubject(MainActivity.this,sublist);
                    listview.setAdapter(adapter);
                    count++;
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                sweetAlertDialog = new SweetAlertDialog(ctx,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Some Network Error Occurred! Please Check Your Internet Connection Or Try Again Later!");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();
            }
        }
    }
}
