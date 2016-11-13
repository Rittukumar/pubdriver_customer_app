package com.oceanstyxx.pubdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.model.Customer;
import com.oceanstyxx.pubdriver.model.Pub;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by mohsin on 06/11/16.
 */

public class PubAdapter extends ArrayAdapter<Pub> {
    private final String MY_DEBUG_TAG = "PubAdapter";
    private ArrayList<Pub> items;
    private int viewResourceId;

    public PubAdapter(Context context, int viewResourceId, ArrayList<Pub> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Pub pub = items.get(position);
        if (pub != null) {
            TextView pubNameLabel = (TextView) v.findViewById(R.id.pubNameLabel);
            if (pubNameLabel != null) {
                pubNameLabel.setText(String.valueOf(pub.getPub_name()));
            }
        }
        return v;
    }
}
