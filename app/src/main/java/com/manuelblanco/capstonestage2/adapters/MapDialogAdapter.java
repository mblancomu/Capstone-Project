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
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.presenter.MapPresenter;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 6/08/16.
 */
public class MapDialogAdapter extends RecyclerView.Adapter<MapDialogAdapter.MapDialogViewHolder> {

    private Context context;
    private List<Trip> items;
    private MapPresenter mPresenter;
    private LatLng mCurrentLocation;

    private OnItemClickListener listener;

    public MapDialogAdapter(Context context, OnItemClickListener listener, List<Trip> items, LatLng location) {
        this.context = context;
        this.listener = listener;
        this.items = items;
        mPresenter = new MapPresenter(context, null,null);
        this.mCurrentLocation = location;
    }

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, Trip country);
    }

    @Override
    public MapDialogAdapter.MapDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_map_dialog, parent, false);
        return new MapDialogAdapter.MapDialogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MapDialogViewHolder holder, int position) {

        String textTrip = items.size() == 1 ? context.getResources().getString(R.string.single_trip) :
                context.getResources().getString(R.string.plural_trips);
        String textDistance = "";

        if (mCurrentLocation != null) {

            double distance = SphericalUtil.computeDistanceBetween(mCurrentLocation, mPresenter.getPositionFronDB(items.get(position).getCoords()));

            if (distance < 1000) {
                textDistance = new DecimalFormat("##.##").format(distance) + " " +
                        context.getResources().getString(R.string.mts_plural);
            } else if (distance > 1000) {
                textDistance = new DecimalFormat("##.##").format(distance / 1000) + " " +
                        context.getResources().getString(R.string.km_plural);
            }

        } else {
            textDistance = context.getResources().getString(R.string.no_location);
        }

        holder.viewTitleTrip.setText(items.get(position).getTitle());
        holder.viewDistance.setText(context.getResources().getString(R.string.distance) + " " + textDistance);

    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    private Trip getTripId(int position) {
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
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(this, getTripId(getAdapterPosition()));
        }

    }
}