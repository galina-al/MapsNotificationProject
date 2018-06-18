package com.example.user.mapsproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mapsproject.R;
import com.example.user.mapsproject.models.MarkerItem;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    List<MarkerItem> markers;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idNotify;
        TextView textNotify;
        TextView address;

        public MyViewHolder(View itemView) {
            super(itemView);

            idNotify = (TextView) itemView.findViewById(R.id.id_notify);
            textNotify = (TextView) itemView.findViewById(R.id.text_notify);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

    public MyRecyclerAdapter(Context context, List<MarkerItem> markers) {
        this.markers = markers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.idNotify.setText(String.valueOf(position + 1));
        holder.textNotify.setText(markers.get(position).getTitle());
        holder.address.setText(markers.get(position).getAddress(context));
    }

    @Override
    public int getItemCount() {

        return markers.size();
    }


}
