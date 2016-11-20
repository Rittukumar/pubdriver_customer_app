package com.oceanstyxx.pubdriver.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.model.DriverRequest;

import static com.oceanstyxx.pubdriver.R.id.btnRequestForBooking;
import static com.oceanstyxx.pubdriver.R.id.checkBoxTermsConditions;

public class MainHomeFragment extends Fragment implements MainFragmentInterface {

    private Button btnBookDriver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_main_home,
                container, false);


        btnBookDriver=(Button)v.findViewById(R.id.btnBookDriver);
        btnBookDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
                tabhost.getTabAt(1).select();
            }
        });


        return v;

    }

    @Override
    public void fragmentBecameVisible() {
        System.out.println("TestFragment");
    }
}
