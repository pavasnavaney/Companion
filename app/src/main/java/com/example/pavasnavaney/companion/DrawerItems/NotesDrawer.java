package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pavasnavaney.companion.R;
import com.example.pavasnavaney.companion.TabFragments.ListAdapterForTabs;
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

/**
 * Created by PAVAS NAVANEY on 29-05-2016.
 */
public class NotesDrawer extends Fragment {

    ListView listview;
    String subject,department;
    JSONObject jsonObject;
    JSONArray jsonArray;
    View v;
    ListAdapterForTabs adapter;
    private List<TabList> tablist = null;

    public NotesDrawer(String subject,String department)
    {
        this.subject=subject;
        this.department=department;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Background_NotesDrawer back = new Background_NotesDrawer(getActivity());
        back.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        v  = inflater.inflate(R.layout.notes_layout,container,false);
        listview = (ListView)v.findViewById(R.id.list_view);
        tablist = new ArrayList<TabList>();
        return v;
    }

    public class Background_NotesDrawer extends AsyncTask<String,Void,String>
    {
        String final_res;
        String json_string;
        Context ctx;
        SweetAlertDialog sweetAlertDialog;

        public Background_NotesDrawer(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#C62828"));
            sweetAlertDialog.setTitleText("Loading Notes...");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URList ur = new URList();
            String course_get_url = ur.notesdrawer;
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
                        + URLEncoder.encode(subject, "UTF-8") + "&"
                        + URLEncoder.encode("department", "UTF-8") + "="
                        + URLEncoder.encode(department, "UTF-8");
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
                    listview.setEmptyView(v.findViewById(R.id.empty));
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
                        adapter = new ListAdapterForTabs(getActivity(), tablist);
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
