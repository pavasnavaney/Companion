package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pavasnavaney.companion.R;
import com.example.pavasnavaney.companion.TabFragments.TabList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAVAS NAVANEY on 27-06-2016.
 */
public class ListAdapterForTutorials extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<TabList> tablist = null;
    private ArrayList<TabList> arraylist;

    public ListAdapterForTutorials(Context context , List<TabList> tablist)
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tutorial_list_item, null);
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
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tablist.get(position).getDownlink())));
            }
        });
        return view;
    }
}
