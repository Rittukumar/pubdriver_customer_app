package com.oceanstyxx.pubdriver.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.activity.BookingDetailsActivity;
import com.oceanstyxx.pubdriver.activity.MainActivity;
import com.oceanstyxx.pubdriver.fragment.BookingDetailsFragment;
import com.oceanstyxx.pubdriver.fragment.MainBookingListFragment;
import com.oceanstyxx.pubdriver.model.BookingRowItem;
import com.oceanstyxx.pubdriver.model.InvoiceData;
import com.oceanstyxx.pubdriver.model.Invoices;
import com.oceanstyxx.pubdriver.model.RowItem;
import com.oceanstyxx.pubdriver.utils.Const;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.R.attr.fragment;
import static android.R.attr.targetActivity;

/**
 * Created by mohsin on 11/10/16.
 */

public class BookingListCustomAdapter extends BaseAdapter {

    Context context;
    List<BookingRowItem> rowItem;

    private TableLayout mTableLayout;


    public BookingListCustomAdapter(Context context, List<BookingRowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {

        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.bookinglist_list, null);
        }

        TextView txtBookingNumber = (TextView) convertView.findViewById(R.id.bookingNumber);
        TextView txtBookingFrom = (TextView) convertView.findViewById(R.id.bookingFrom);
        TextView txtBookingStatus = (TextView) convertView.findViewById(R.id.bookingStatus);
        TextView txtBookingDescription = (TextView) convertView.findViewById(R.id.bookingDescription);
        TextView txtBookingDate = (TextView) convertView.findViewById(R.id.bookingDate);
        TextView txtBookingPrice = (TextView) convertView.findViewById(R.id.bookingPrice);

        BookingRowItem row_pos = rowItem.get(position);

        String status = row_pos.getBookingStatus();

        txtBookingNumber.setText(row_pos.getBookingNumber());
        txtBookingFrom.setText(row_pos.getBookingFrom());
        txtBookingStatus.setText(status);
        txtBookingDescription.setText(row_pos.getDescription());
        txtBookingDate.setText(row_pos.getBookingDate());
        txtBookingPrice.setText(row_pos.getPrice());

        if(status.equalsIgnoreCase("Assigned")) {
            txtBookingDescription.setTextColor(ContextCompat.getColor(context, R.color.request_assigned));
        }
        else if(status.equalsIgnoreCase("Started")) {
            txtBookingDescription.setTextColor(ContextCompat.getColor(context, R.color.request_started));
        }
        else if(status.equalsIgnoreCase("Ended")) {
            txtBookingDescription.setTextColor(ContextCompat.getColor(context, R.color.request_ended));
        }
        else if(status.equalsIgnoreCase("Settled")) {
            txtBookingDescription.setTextColor(ContextCompat.getColor(context, R.color.request_settled));
        }

        ((Activity) context).getFragmentManager();
        Button btnViewDetails = (Button) convertView.findViewById(R.id.btnViewDetails);
        btnViewDetails.setTag(row_pos.getDriveId());
        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String driveId = (String)v.getTag();
                Intent i = new Intent(((Activity) context),
                        BookingDetailsActivity.class);
                i.putExtra("driveid", driveId);
                ((Activity) context).startActivity(i);
                //((Activity) context).finish();

                /*AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("My title")
                        .setMessage("Enter password");
                final FrameLayout frameView = new FrameLayout(context);
                builder.setView(frameView);

                final AlertDialog alertDialog = builder.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.booking_detail_dialog, frameView);

                mTableLayout = (TableLayout) dialoglayout.findViewById(R.id.tableInvoices);

                mTableLayout.setStretchAllColumns(true);
                loadData();

                alertDialog.show();*/

                //AlertDialog alertDialog = new AlertDialog.Builder(((Activity) context)).create(); //Read Update
                //alertDialog.setTitle("hi");
                //alertDialog.setMessage("this is my app");
                //alertDialog.show();  //<-- See This!

            }
        });

        return convertView;

    }

}
