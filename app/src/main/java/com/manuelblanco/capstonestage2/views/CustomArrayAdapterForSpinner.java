package com.manuelblanco.capstonestage2.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;

import java.util.List;

/**
 * Created by manuel on 22/08/16.
 */
public class CustomArrayAdapterForSpinner extends ArrayAdapter<String> {

    private Context mContext;

    public CustomArrayAdapterForSpinner(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    public CustomArrayAdapterForSpinner(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == getCount()) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == getCount()) {
            tv.setTextColor(mContext.getResources().getColor(R.color.colorSecondary_text));
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary_text));
        }
        return view;
    }

    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
