package com.example.pavasnavaney.companion.IntroManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pavasnavaney.companion.Main.MainActivity;
import com.example.pavasnavaney.companion.Main.Splash;
import com.example.pavasnavaney.companion.R;
import com.example.pavasnavaney.companion.URLConstants.URList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by PAVAS NAVANEY on 26-05-2016.
 */
public class ListAdapterForDepartments extends BaseAdapter {

    private PrefManager prefManager;

    String name , email,phone,course;
    Context context;
    LayoutInflater inflater;
    private List<Dept_Loader> deptlist = null;
    private ArrayList<Dept_Loader> arraylist;
    public ListAdapterForDepartments(Context context , List<Dept_Loader> deptlist)
    {
        this.context = context;
        this.deptlist=deptlist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Dept_Loader>();
        this.arraylist.addAll(deptlist);
    }

    public class ViewHolder {
        TextView department;
        TextView select;
    }


    @Override
    public int getCount() {
        return deptlist.size();
    }

    @Override
    public Object getItem(int position) {
        return deptlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.department_list_item, null);
            holder.department = (TextView) view.findViewById(R.id.course_name);
            holder.select = (TextView) view.findViewById(R.id.selectbutton);
            view.setTag(holder);
        }
            else {
                holder = (ViewHolder) view.getTag();
            }
        holder.department.setText(deptlist.get(position)
                .getDepartment());
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                course = deptlist.get(position).getDepartment();
                SharedPreferences pref = context.getSharedPreferences("login",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("course",course);
                editor.commit();
                Intent i = new Intent(context,MainActivity.class);
                context.startActivity(i);
                Background_Register back = new Background_Register(context);
                back.execute();
            }

        });
        return view;
    }
public class Background_Register extends AsyncTask<String,Void,String>
{
    Context ctx;
    SweetAlertDialog sweetAlertDialog;

    public Background_Register(Context ctx)
    {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences pref =  ctx.getSharedPreferences("login",Context.MODE_PRIVATE);
        name = pref.getString("name",null);
        email = pref.getString("email",null);
        phone = pref.getString("phone",null);

        sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#00897B"));
        sweetAlertDialog.setTitleText("Registering...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        URList ur = new URList();
        String register_url = ur.register;
        try {
            URL url = new URL(register_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("name", "UTF-8") + "="
                    + URLEncoder.encode(name, "UTF-8") + "&"
                    + URLEncoder.encode("email", "UTF-8") + "="
                    + URLEncoder.encode(email, "UTF-8") + "&"
                    + URLEncoder.encode("phone", "UTF-8") + "="
                    + URLEncoder.encode(phone, "UTF-8") + "&"
                    + URLEncoder.encode("dept", "UTF-8") + "="
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
        } catch (MalformedURLException | ProtocolException e) {

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
        if(result.equals("Data Inserted"))
        {
            prefManager = new PrefManager(ctx);
            Intent i = new Intent(
                    ctx,
                    Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ctx.startActivity(i);
        }
    }
}
}
