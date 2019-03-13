package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pavasnavaney.companion.IntroManager.Subject_Loader;
import com.example.pavasnavaney.companion.Main.Tabs;
import com.example.pavasnavaney.companion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAVAS NAVANEY on 27-05-2016.
 */
public class ListAdapterForFbl extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<Subject_Loader> sublist = null;
    private ArrayList<Subject_Loader> arraylist;
    public ListAdapterForFbl(Context context, List<Subject_Loader> sublist)
    {
        this.context = context;
        this.sublist=sublist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Subject_Loader>();
        this.arraylist.addAll(sublist);
    }
    public class ViewHolder {
        TextView subject;
        TextView select;
    }

    @Override
    public int getCount() {
        return sublist.size();
    }

    @Override
    public Object getItem(int position) {
        return sublist.get(position);
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
            view = inflater.inflate(R.layout.fbl_list_item, null);
            holder.subject = (TextView) view.findViewById(R.id.course_name);
            holder.select = (TextView) view.findViewById(R.id.selectbutton);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        holder.subject.setText(sublist.get(position)
                .getSubject());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,DrawerFBL.class);
                intent.putExtra("subject",sublist.get(position).getSubject().toString());
                intent.putExtra("semester",sublist.get(position).getSemester().toString());
                intent.putExtra("course",sublist.get(position).getCourse().toString());
                context.startActivity(intent);
            }
        });
        return view;
    }

}
