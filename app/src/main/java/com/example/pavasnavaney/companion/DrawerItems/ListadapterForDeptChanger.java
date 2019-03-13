package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pavasnavaney.companion.IntroManager.Dept_Loader;
import com.example.pavasnavaney.companion.Main.MainActivity;
import com.example.pavasnavaney.companion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAVAS NAVANEY on 05-07-2016.
 */
public class ListadapterForDeptChanger extends BaseAdapter {

    Context context;
    String course;
    LayoutInflater inflater;
    private List<Dept_Loader> deptlist = null;
    private ArrayList<Dept_Loader> arraylist;

    public ListadapterForDeptChanger(Context context , List<Dept_Loader> deptlist)
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
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
            }

        });
        return view;
    }
}
