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


        txtBookingNumber.setText(row_pos.getBookingNumber());
        txtBookingFrom.setText(row_pos.getBookingFrom());
        txtBookingStatus.setText(row_pos.getBookingStatus());
        txtBookingDescription.setText(row_pos.getDescription());
        txtBookingDate.setText(row_pos.getBookingDate());
        txtBookingPrice.setText(row_pos.getPrice());

        ((Activity) context).getFragmentManager();
        Button btnViewDetails = (Button) convertView.findViewById(R.id.btnViewDetails);
        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(((Activity) context),
                        BookingDetailsActivity.class);
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


    public void loadData() {

        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize =0, mediumTextSize = 0;

        textSize = (int) context.getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) context.getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) context.getResources().getDimension(R.dimen.font_size_medium);

        Invoices invoices = new Invoices();
        InvoiceData[] data = invoices.getInvoices();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        int rows = data.length;
        //getSupportActionBar().setTitle("Invoices (" + String.valueOf(rows) + ")");
        TextView textSpacer = null;

        mTableLayout.removeAllViews();

        // -1 means heading row
        for(int i = -1; i < rows; i ++) {
            InvoiceData row = null;
            if (i > -1)
                row = data[i];
            else {
                textSpacer = new TextView(context);
                textSpacer.setText("");

            }
            // data columns
            final TextView tv = new TextView(context);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);

            tv.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv.setText("CHARGE DETAILS");
                tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(row.chargeDetails);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }

            final TextView tv2 = new TextView(context);
            if (i == -1) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv2.setGravity(Gravity.LEFT);

            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setText("QTY");
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv2.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(String.valueOf(row.qty));
                tv2.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }


            final LinearLayout layCustomer = new LinearLayout(context);
            layCustomer.setOrientation(LinearLayout.VERTICAL);
            layCustomer.setPadding(0, 10, 0, 10);
            layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView tv3 = new TextView(context);
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 0, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }

            tv3.setGravity(Gravity.TOP);


            if (i == -1) {
                tv3.setText("UNIT RATE");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setText(String.valueOf(row.unitRate));
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }
            layCustomer.addView(tv3);


            /*if (i > -1) {
                final TextView tv3b = new TextView(this);
                tv3b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tv3b.setGravity(Gravity.RIGHT);
                tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3b.setPadding(5, 1, 0, 5);
                tv3b.setTextColor(Color.parseColor("#aaaaaa"));
                tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3b.setText(row.customerAddress);
                layCustomer.addView(tv3b);
            }*/

            final LinearLayout layAmounts = new LinearLayout(context);
            layAmounts.setOrientation(LinearLayout.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(0, 10, 0, 10);
            layAmounts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));



            final TextView tv4 = new TextView(context);
            if (i == -1) {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setPadding(5, 5, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setPadding(5, 0, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }

            tv4.setGravity(Gravity.RIGHT);

            if (i == -1) {
                tv4.setText("TOTAL");
                tv4.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTextColor(Color.parseColor("#000000"));
                tv4.setText(decimalFormat.format(row.total));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }

            layAmounts.addView(tv4);


            /*if (i > -1) {
                final TextView tv4b = new TextView(this);
                tv4b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tv4b.setGravity(Gravity.RIGHT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));

                String due = "";
                if (row.amountDue.compareTo(new BigDecimal(0.01)) == 1) {
                    due = "Due:" + decimalFormat.format(row.amountDue);
                    due = due.trim();
                }
                tv4b.setText(due);
                layAmounts.addView(tv4b);
            }*/


            // add table row
            final TableRow tr = new TableRow(context);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);



            tr.addView(tv);
            tr.addView(tv2);
            tr.addView(layCustomer);
            tr.addView(layAmounts);

            if (i > -1) {

                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TableRow tr = (TableRow) v;
                        //do whatever action is needed

                    }
                });


            }
            mTableLayout.addView(tr, trParams);

            if (i > -1) {

                // add separator row
                final TableRow trSep = new TableRow(context);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(context);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);

                trSep.addView(tvSep);
                mTableLayout.addView(trSep, trParamsSep);
            }


        }
    }
}
