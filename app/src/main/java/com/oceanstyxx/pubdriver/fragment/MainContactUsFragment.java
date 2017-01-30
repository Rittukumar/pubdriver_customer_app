package com.oceanstyxx.pubdriver.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import com.oceanstyxx.pubdriver.R;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.oceanstyxx.pubdriver.R.id.btnRequestForBooking;

public class MainContactUsFragment extends Fragment implements  MainFragmentInterface {

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

                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.call_paragraphs);
                recipientsTextView.setTextSize(20);
                builder.setView(recipientsLayout);
                builder.setTitle("CALL")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/


                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9740126435"));
                startActivity(callIntent);*/
                onCall();

            }
        });


        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setText(R.string.email_paragraphs);
                recipientsTextView.setTextSize(20);
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
                alert.show();*/


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "contactus@pubdrivers.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pubdrivers for Android - Feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));

            }
        });

        btnUsageGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setTextSize(15);
                recipientsTextView.setText(R.string.user_manual_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("USAGE GUIDE");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismissDialog(0);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        btnPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setTextSize(15);
                recipientsTextView.setText(R.string.pricing_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("PRICING");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismissDialog(0);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        btnTermsAndConditoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }

                final View recipientsLayout = getActivity().getLayoutInflater().inflate(R.layout.message_scrollview, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.invalid_recipients);
                recipientsTextView.setTextSize(15);
                recipientsTextView.setText(R.string.terms_and_conditions_paragraphs);
                builder.setView(recipientsLayout);
                builder.setTitle("TERMS AND CONDITIONS");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismissDialog(0);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        return rootView;
    }


    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            String callNumber = getActivity().getString(R.string.call_paragraphs);
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+callNumber)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void fragmentBecameVisible() {
        System.out.println("TestFragment");
    }
}
