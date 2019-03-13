package com.example.pavasnavaney.companion.TabFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by PAVAS NAVANEY on 25-05-2016.
 */
public class Papers extends Fragment {
    ListView listview;
    String subject,semester,course;
    JSONObject jsonObject;
    ListAdapterForPapers adapter;
    View v;
    private List<PaperList> paperlist = null;
    JSONArray jsonArray;

    public Papers(String subject , String semester , String course)
    {
        this.subject=subject;
        this.semester=semester;
        this.course=course;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Background_Papers back = new Background_Papers(getActivity());
        back.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        v  = inflater.inflate(R.layout.notes_layout,container,false);
        listview = (ListView)v.findViewById(R.id.list_view);
        paperlist = new ArrayList<PaperList>();
        return v;
    }
    public class Background_Papers extends AsyncTask<String,Void,String>
    {
        String final_res;
        String json_string;
        Context ctx;
        SweetAlertDialog sweetAlertDialog;

        public Background_Papers(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#C62828"));
            sweetAlertDialog.setTitleText("Loading Papers...");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URList ur = new URList();
            String course_get_url = ur.papers;
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
                        + URLEncoder.encode("semester", "UTF-8") + "="
                        + URLEncoder.encode(semester, "UTF-8") + "&"
                        + URLEncoder.encode("department", "UTF-8") + "="
                        + URLEncoder.encode(course, "UTF-8");
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
                        PaperList map = new PaperList();
                        JSONObject explore = jsonArray.getJSONObject(count);
                        map.setTitle((String) explore.get("title"));
                        String sol = explore.getString("solution");
                        String down = explore.getString("paper");
                        map.setDownpaperlink(down);
                        map.setDownsolutionlink(sol);
                        paperlist.add(map);
                        adapter = new ListAdapterForPapers(getActivity(),paperlist);
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
