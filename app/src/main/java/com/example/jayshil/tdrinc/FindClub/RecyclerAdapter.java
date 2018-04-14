package com.example.jayshil.tdrinc.FindClub;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jayshil.tdrinc.R;

import java.util.ArrayList;

/**
 * Created by jayshil on 10/17/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<club_image_name> list = new ArrayList<club_image_name>();

    public RecyclerAdapter(ArrayList<club_image_name> club_info){

        list = club_info;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_clubs,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        club_image_name name = list.get(position);
        holder.club_name.setText(name.getClub_name());
        holder.club_image.setImageResource(name.getImage_id());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView club_image;
        TextView club_name;

        public ViewHolder(View v){
            super(v);
            club_image = (ImageView)v.findViewById(R.id.club_image);
            club_name = (TextView)v.findViewById(R.id.club_name);
        }
    }
}
