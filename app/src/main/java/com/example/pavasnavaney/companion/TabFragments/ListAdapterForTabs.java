package com.example.pavasnavaney.companion.TabFragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavasnavaney.companion.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by PAVAS NAVANEY on 28-05-2016.
 */
public class ListAdapterForTabs extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<TabList> tablist = null;
    private ArrayList<TabList> arraylist;

    public ListAdapterForTabs(Context context , List<TabList> tablist)
    {
        this.context = context;
        this.tablist=tablist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<TabList>();
        this.arraylist.addAll(tablist);

    }

    public class ViewHolder {
        TextView title;
        TextView contents;
        TextView download;
    }
    @Override
    public int getCount() {
        return tablist.size();
    }

    @Override
    public Object getItem(int position) {
        return tablist.get(position);
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
            view = inflater.inflate(R.layout.notes_list_item, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.contents = (TextView) view.findViewById(R.id.contents);
            holder.download = (TextView) view
                    .findViewById(R.id.downloadbutton);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(tablist.get(position).getTitle());
        holder.contents.setText(tablist.get(position).getContents());
       holder.download.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             String down = tablist.get(position).getDownlink();
               try {
                   DownloadManager mManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                   DownloadManager.Request mRqRequest = new DownloadManager.Request(
                           Uri.parse(down));
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
