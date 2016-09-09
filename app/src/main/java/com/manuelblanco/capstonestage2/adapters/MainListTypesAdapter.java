package com.manuelblanco.capstonestage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 25/07/16.
 */
public class MainListTypesAdapter extends RecyclerView.Adapter<MainListTypesAdapter.MainViewHolder> {

    private Context context;
    private String[] mTypes;

    private OnItemClickListener listener;

    public MainListTypesAdapter(Context context, OnItemClickListener listener, String[] types) {
        this.context = context;
        this.listener = listener;
        this.mTypes = types;
    }

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, String type);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_types_main, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        holder.viewType.setText(mTypes[position].toString());
        int numberTrips = SqlHandler.getNumberOfTypes(mTypes[position].toString());

        holder.viewNumberTrips.setText(numberTrips == 1 ? numberTrips + " Trip" : numberTrips + " Trips");

        int iconType = Utils.getIconType(context, mTypes[position].toLowerCase());
        if (iconType == 0){
            iconType = R.drawable.ic_cultural;
        }

        holder.viewIcon.setImageResource(iconType);

        try {
            Picasso.with(context)
                    .load(URLUtils.getURLTypes(mTypes[position].toLowerCase()))
                    .placeholder(R.drawable.trip_placeholder)
                    .resize(context.getResources().getInteger(R.integer.width_imageview_list),
                            context.getResources().getInteger(R.integer.height_imageview_list))
                    .onlyScaleDown()
                    .into(holder.viewPhoto);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mTypes.length;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_type_card)
        TextView viewType;
        @BindView(R.id.iv_iconType)
        ImageView viewIcon;
        @BindView(R.id.tv_number_trips_card)
        TextView viewNumberTrips;
        @BindView(R.id.thumbnail_type)
        public ImageView viewPhoto;

        public MainViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(this, mTypes[getAdapterPosition()].toString());
        }

    }
}

