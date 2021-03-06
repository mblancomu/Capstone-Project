package com.manuelblanco.capstonestage2.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by manuel on 29/08/16.
 */
public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private final int mMarginHorizontal;
    private final int mMarginVertical;


    public MarginItemDecoration(int horizontal, int vertical) {
        mMarginHorizontal = horizontal;
        mMarginVertical = vertical;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(mMarginHorizontal, mMarginVertical, mMarginHorizontal, mMarginVertical);
    }

}
