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
import com.manuelblanco.capstonestage2.model.Country;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 28/07/16.
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    private Context context;
    private ArrayList<String> items;
    private int mNumberTrips;

    private OnItemClickListener listener;

    public CountriesAdapter(Context context, OnItemClickListener listener, ArrayList<String> items) {
        this.context = context;
        this.listener = listener;
        this.items = items;
    }

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, int trips, String country);
    }

    @Override
    public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_countries, parent, false);
        return new CountriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountriesViewHolder holder, int position) {

        String textTrip = items.size() == 1 ? context.getResources().getString(R.string.single_trip) :
                context.getResources().getString(R.string.plural_trips);

        Country country = SqlHandler.getCountryName(items.get(position));
        holder.viewTitleCountry.setText(country.getName());

        holder.viewNumberTrips.setText(SqlHandler.getCountryTrips(country.getIso()) + textTrip);
        mNumberTrips = SqlHandler.getCountryTrips(country.getIso());

        int flag = Utils.getFlagIso(context, items.get(position));

        if (flag == 0){
            flag = R.drawable.es;
        }

        try {
            Picasso.with(context).load(URLUtils.getURLFlags(items.get(position)))
                    .placeholder(flag)
                    .into(holder.viewFlag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    private String getCountryId(int posicion) {
        if (items != null) {
            return items.get(posicion);
        } else {
            return null;
        }
    }

    public class CountriesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_country)
        TextView viewTitleCountry;
        @BindView(R.id.tv_number_items)
        TextView viewNumberTrips;
        @BindView(R.id.iv_flag)
        ImageView viewFlag;

        public CountriesViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(this, mNumberTrips, getCountryId(getAdapterPosition()));
        }

    }
}

