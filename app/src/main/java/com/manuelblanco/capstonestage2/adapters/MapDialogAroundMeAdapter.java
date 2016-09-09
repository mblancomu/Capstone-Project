package com.manuelblanco.capstonestage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.model.AroundMe;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.presenter.MapPresenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 6/08/16.
 */
public class MapDialogAroundMeAdapter extends RecyclerView.Adapter<MapDialogAroundMeAdapter.MapDialogViewHolder> {

    private Context context;
    private ArrayList<AroundMe> items;

    private OnItemClickListener listener;

    public MapDialogAroundMeAdapter(Context context, OnItemClickListener listener, ArrayList<AroundMe> items) {
        this.context = context;
        this.listener = listener;
        this.items = items;
    }

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, AroundMe point);
    }

    @Override
    public MapDialogAroundMeAdapter.MapDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_map_dialog, parent, false);
        return new MapDialogAroundMeAdapter.MapDialogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MapDialogViewHolder holder, int position) {

        String textTrip = items.size() == 1 ? context.getResources().getString(R.string.single_trip) :
                context.getResources().getString(R.string.plural_trips);
        String textDistance = "";

           double distance = items.get(position).getDistance();

            if (distance < 1000){
                textDistance = new DecimalFormat("##.##").format(distance) + " " +
                        context.getResources().getString(R.string.mts_plural);
            }else if (distance > 1000){
                textDistance = new DecimalFormat("##.##").format(distance/1000) + " " +
                        context.getResources().getString(R.string.km_plural);
            }

        holder.viewTitleTrip.setText(items.get(position).getTrip().getTitle());
        holder.viewDistance.setText(context.getResources().getString(R.string.distance) + " " + textDistance);

    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    private AroundMe getAroundMeId(int position) {
        if (items != null) {
            return items.get(position);
        } else {
            return null;
        }
    }

    public class MapDialogViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_map_trip)
        TextView viewTitleTrip;
        @BindView(R.id.tv_distance)
        TextView viewDistance;

        public MapDialogViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(this, getAroundMeId(getAdapterPosition()));
        }

    }

    public void addMorePoints(ArrayList<AroundMe> points){
        this.items = points;

        notifyDataSetChanged();
    }
}