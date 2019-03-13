package com.example.pavasnavaney.companion.DrawerItems;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pavasnavaney.companion.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAVAS NAVANEY on 04-07-2016.
 */
public class ListAdapterForTips extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<TipLoader> tiplist = null;
    private ArrayList<TipLoader> arraylist;

    public ListAdapterForTips(Context context, List<TipLoader> tiplist) {
        this.context = context;
        this.tiplist = tiplist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<TipLoader>();
        this.arraylist.addAll(tiplist);
    }

    public class ViewHolder {
        ImageView photo;
        TextView title;
        ProgressBar progressBar;
    }

    @Override
    public int getCount() {
        return tiplist.size();
    }

    @Override
    public Object getItem(int position) {
        return tiplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tip_layout, null);
            holder.photo = (ImageView)view.findViewById(R.id.photo);
            holder.title = (TextView)view.findViewById(R.id.title);
            holder.progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(tiplist.get(position).getHeading());
        Glide.with(context).load(tiplist.get(position).getPhoto()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource)
            {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.photo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TipExpanded.class);
                i.putExtra("title", tiplist.get(position).getHeading());
                i.putExtra("body", tiplist.get(position).getBody());
                i.putExtra("photo", tiplist.get(position).getPhoto());
                context.startActivity(i);
            }
        });
        return view;
    }
}
