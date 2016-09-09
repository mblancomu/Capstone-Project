package com.manuelblanco.capstonestage2.listeners;

import com.manuelblanco.capstonestage2.model.AroundMe;
import com.manuelblanco.capstonestage2.model.Trip;

/**
 * Created by manuel on 7/08/16.
 */
public interface DialogMapListener {

    public void onTripSelected(Trip trip);
    public void onAroundMeSelected(AroundMe aroundMe);
}
