package com.oceanstyxx.pubdriver.model;

import static android.R.attr.id;

/**
 * Created by mohsin on 20/11/16.
 */

public class OtherVenue {

    private String id;

    private String venue;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
