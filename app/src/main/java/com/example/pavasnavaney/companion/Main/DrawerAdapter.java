package com.example.pavasnavaney.companion.Main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavasnavaney.companion.DrawerItems.Contact_Us;
import com.example.pavasnavaney.companion.DrawerItems.Department_Changer;
import com.example.pavasnavaney.companion.DrawerItems.Drawer_Bs;
import com.example.pavasnavaney.companion.DrawerItems.Drawer_English;
import com.example.pavasnavaney.companion.DrawerItems.Drawer_Tutorials;
import com.example.pavasnavaney.companion.DrawerItems.Drawer_fbl;
import com.example.pavasnavaney.companion.DrawerItems.TipActivity;
import com.example.pavasnavaney.companion.R;

/**
 * Created by PAVAS NAVANEY on 24-05-2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[];
    private int mIcons[];
    private String name;
    private String email;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        TextView textView;
        ImageView imageView;
        TextView Name;
        TextView email;
        Context contxt;


        public ViewHolder(View itemView, int ViewType,Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            }
            else{
                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                Holderid = 0;
            }
        }

        @Override
        public void onClick(View view) {
            int pos = getPosition();
           if(pos==1)
           {
               Intent i = new Intent(contxt, Drawer_English.class);
               contxt.startActivity(i);
           }
           else if(pos==2)
            {
                Intent i = new Intent(contxt, Drawer_Bs.class);
                contxt.startActivity(i);
            }
           else if(pos==3)
           {
               Intent i = new Intent(contxt, Drawer_fbl.class);
               contxt.startActivity(i);
           }
           else if(pos==4)
           {
               Intent i = new Intent(contxt, Drawer_Tutorials.class);
               contxt.startActivity(i);
           }
           else if(pos==5)
           {
               Intent i = new Intent(contxt, TipActivity.class);
               contxt.startActivity(i);
           }
            else if(pos==6)
           {
               Intent i = new Intent(contxt, Department_Changer.class);
               contxt.startActivity(i);
           }
           else if(pos==7)
           {
               Intent i = new Intent(contxt, Contact_Us.class);
               contxt.startActivity(i);
           }
        }
    }
    DrawerAdapter(String Titles[],int Icons[],String Name,String Email,Context passedContext) {
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
        email = Email;
        this.context = passedContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType,context);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType, context);
            return vhHeader;
        }
            return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.Holderid ==1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position -1]);
        }
        else{
            holder.Name.setText(name);
            holder.email.setText(email);
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}