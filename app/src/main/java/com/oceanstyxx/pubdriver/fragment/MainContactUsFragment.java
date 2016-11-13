package com.oceanstyxx.pubdriver.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import com.oceanstyxx.pubdriver.R;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import static com.oceanstyxx.pubdriver.R.id.btnRequestForBooking;

public class MainContactUsFragment extends Fragment {

    private View rootView;
    private Button btnCall;
    private Button btnEmail;
    private Button btnUsageGuide;
    private Button btnPricing;
    private Button btnTermsAndConditoins;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main_contact_us, container, false);

        btnCall=(Button)rootView.findViewById(R.id.btnCall);
        btnEmail=(Button)rootView.findViewById(R.id.btnEmail);
        btnUsageGuide=(Button)rootView.findViewById(R.id.btnUsageGuide);
        btnPricing=(Button)rootView.findViewById(R.id.btnPricing);
        btnTermsAndConditoins=(Button)rootView.findViewById(R.id.btnTermsAndConditoins);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.call_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("CALL")
                        //.setMessage("This is USAGE GUIDE dialog")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.email_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("EMAIL")
                        //.setMessage("This is USAGE GUIDE dialog")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        btnUsageGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.user_manual_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("USAGE GUIDE")
                        //.setMessage("This is USAGE GUIDE dialog")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        btnPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.pricing_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("PRICING")
                        //.setMessage("This is USAGE GUIDE dialog")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        btnTermsAndConditoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.terms_and_conditions_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("TERMS AND CONDITIONS")
                        //.setMessage("This is USAGE GUIDE dialog")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        return rootView;
    }
}
