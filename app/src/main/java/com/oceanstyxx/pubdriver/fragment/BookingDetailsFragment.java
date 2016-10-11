package com.oceanstyxx.pubdriver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import com.oceanstyxx.pubdriver.R;

/**
 * Created by mohsin on 11/10/16.
 */

public class BookingDetailsFragment extends Fragment {

    Context context ;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        rootView = inflater.inflate(R.layout.fragment_booking_details,
                container, false);
        return rootView;
    }


}
