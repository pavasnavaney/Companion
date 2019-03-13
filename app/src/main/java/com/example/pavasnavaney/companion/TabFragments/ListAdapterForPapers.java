package com.example.pavasnavaney.companion.TabFragments;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.example.pavasnavaney.companion.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by PAVAS NAVANEY on 28-05-2016.
 */
public class ListAdapterForPapers extends BaseAdapter {

    private NotificationManager notificationManager;
    private Builder mBuilder;
    Context context;
    LayoutInflater inflater;
    private List<PaperList> paperlist;
    private ArrayList<PaperList> arraylist;
    int id = 1;
    String paper,solution,passedtitle;

    public ListAdapterForPapers(Context context , List<PaperList> paperlist)
    {
        this.context = context;
        this.paperlist=paperlist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<PaperList>();
        this.arraylist.addAll(paperlist);
    }

    public class ViewHolder {
        TextView title;
        TextView downloadpaper;
        TextView downloadsolution;
    }

    @Override
    public int getCount() {
        return paperlist.size();
    }

    @Override
    public Object getItem(int position) {
        return paperlist.get(position);
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
            view = inflater.inflate(R.layout.papers_list_item, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.downloadpaper = (TextView) view.findViewById(R.id.downloadpaper);
            holder.downloadsolution = (TextView) view
                    .findViewById(R.id.downloadsolution);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        solution = paperlist.get(position).getDownsolutionlink();
        if(solution.equals("-"))
        {
            holder.downloadsolution.setVisibility(View.GONE);
        }
        holder.title.setText(paperlist.get(position).getTitle());
        holder.downloadpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paper = paperlist.get(position).getDownpaperlink();

                try {
                    DownloadManager mManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request mRqRequest = new DownloadManager.Request(
                            Uri.parse(paper));
                    mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    long idDownLoad = mManager.enqueue(mRqRequest);
                }
                catch(Exception e)
                {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Some Network Error Occurred! Please Check Your Internet Connection Or Try Again Later!");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.show();
                }
            }
        });
        holder.downloadsolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String sol =  paperlist.get(position).getDownsolutionlink();
                try {
                    DownloadManager mManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request mRqRequest = new DownloadManager.Request(
                            Uri.parse(sol));
                    mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    long idDownLoad = mManager.enqueue(mRqRequest);
                }
                catch(Exception e)
                {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Some Network Error Occurred! Please Check Your Internet Connection Or Try Again Later!");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.show();
                }
            }
        });
        return view;
    }
}
