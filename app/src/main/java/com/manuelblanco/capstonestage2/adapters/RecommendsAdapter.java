package com.manuelblanco.capstonestage2.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.model.Recommend;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by manuel on 25/08/16.
 */
public class RecommendsAdapter extends RecyclerView.Adapter<RecommendsAdapter.RecommendsViewHolder> {

    private Context context;
    private ArrayList<Recommend> items;

    public RecommendsAdapter(Context context, ArrayList<Recommend> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecommendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_recommends, parent, false);
        return new RecommendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecommendsViewHolder holder, final int position) {

        holder.mTitle.setText(items.get(position).getName());
        holder.mDescription.setText(items.get(position).getDescription());

        final String phone = items.get(position).getPhone();

        if (phone != null && !phone.isEmpty() && !phone.equals("")) {
            holder.mPhone.setVisibility(View.VISIBLE);
            holder.mPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchPhone(phone);
                }
            });
        }else {
            holder.mPhone.setVisibility(View.GONE);
        }

        final String website =  items.get(position).getWebsite();

        if (website != null && !website.isEmpty() && !website.equals("")) {
            holder.mPhone.setVisibility(View.VISIBLE);
            holder.mWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchWebsite(website);
                }
            });
        }else {
            holder.mWebsite.setVisibility(View.GONE);
        }

        final String coords = items.get(position).getCoords();

        if (coords != null && !coords.isEmpty() && !coords.equals("")) {
            holder.mPhone.setVisibility(View.VISIBLE);
            holder.mPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchMap(coords,items.get(position).getName());
                }
            });
        }else {
            holder.mPosition.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    public class RecommendsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title_recommend)
        TextView mTitle;
        @BindView(R.id.tv_description_recommend)
        TextView mDescription;
        @BindView(R.id.ib_phone)
        ImageButton mPhone;
        @BindView(R.id.ib_website)
        ImageButton mWebsite;
        @BindView(R.id.ib_position)
        ImageButton mPosition;
        @BindView(R.id.divider)
        View mDivider;

        public RecommendsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private void launchPhone(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    private void launchWebsite(String website){
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + website));
        context.startActivity(launchBrowser);
    }

    private void launchMap(String position, String name){

        String uriBegin = "geo:" + position;
        String query = position + "(" + name + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
