package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.pavasnavaney.companion.IntroManager.Dept_Loader;
import com.example.pavasnavaney.companion.R;
import com.example.pavasnavaney.companion.URLConstants.URList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Drawer_Tutorials extends AppCompatActivity {

    private ListView listView;
    String final_res;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListAdapterForTut adapter;
    private List<Dept_Loader> deptlist =null;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer__tutorials);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Subject");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.list_view);
        deptlist = new ArrayList<Dept_Loader>();

        Background_Tut back = new Background_Tut(Drawer_Tutorials.this);
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
    public class Background_Tut extends AsyncTask<String,Void,String>
    {

        String final_res;
        String json_string;
        Context ctx;
        SweetAlertDialog sweetAlertDialog;

        public Background_Tut(Context ctx)
        {
            this.ctx = ctx;
        }


        @Override
        protected void onPreExecute() {
            sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#FFA000"));
            sweetAlertDialog.setTitleText("Loading Subjects...");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URList ur = new URList();
            String course_get_url = ur.tutlist;
            try {
                URL url = new URL(course_get_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((json_string =bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
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
                String department;
                final_res = result;
                jsonObject = new JSONObject(final_res);
                jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                while (count < jsonArray.length()) {

                    Dept_Loader map=new Dept_Loader();
                    JSONObject jo = jsonArray.getJSONObject(count);
                    department = jo.getString("Subject");
                    map.setDepartment(department);
                    deptlist.add(map);
                    adapter = new ListAdapterForTut(Drawer_Tutorials.this,deptlist);
                    listView.setAdapter(adapter);
                    count++;
                }
            } catch (Exception e) {
                sweetAlertDialog = new SweetAlertDialog(ctx,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Some Network Error Occurred! Please Check Your Internet Connection Or Try Again Later!");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();
            }
        }

    }
}
