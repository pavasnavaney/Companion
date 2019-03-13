package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pavasnavaney.companion.IntroManager.Dept_Loader;
import com.example.pavasnavaney.companion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAVAS NAVANEY on 27-06-2016.
 */
public class ListAdapterForTut extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<Dept_Loader> deptlist = null;
    private ArrayList<Dept_Loader> arraylist;

    public ListAdapterForTut(Context context, List<Dept_Loader> deptlist) {
        this.context = context;
        this.deptlist = deptlist;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tut_list_item, null);
            holder.department = (TextView) view.findViewById(R.id.course_name);
            holder.select = (TextView) view.findViewById(R.id.selectbutton);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.department.setText(deptlist.get(position)
                .getDepartment());
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               Intent intent = new Intent(context, Tutpoint.class);
              intent.putExtra("subject",deptlist.get(position).getDepartment().toString());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
