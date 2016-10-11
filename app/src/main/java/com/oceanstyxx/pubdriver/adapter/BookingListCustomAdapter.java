package com.oceanstyxx.pubdriver.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.fragment.BookingDetailsFragment;
import com.oceanstyxx.pubdriver.fragment.MainBookingListFragment;
import com.oceanstyxx.pubdriver.model.BookingRowItem;
import com.oceanstyxx.pubdriver.model.RowItem;

import java.util.List;

import static android.R.attr.fragment;

/**
 * Created by mohsin on 11/10/16.
 */

public class BookingListCustomAdapter extends BaseAdapter {

    Context context;
    List<BookingRowItem> rowItem;

    private MainBookingListFragment bookingListFragment;

    public BookingListCustomAdapter(Context context, List<BookingRowItem> rowItem,MainBookingListFragment bookingListFragment) {
        this.context = context;
        this.rowItem = rowItem;
        this.bookingListFragment = bookingListFragment;

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
                Fragment newFragment = new BookingDetailsFragment();

                FragmentTransaction ft = bookingListFragment.getChildFragmentManager().beginTransaction();
                ft.hide(bookingListFragment);
                ft.commit();
                ft.show(newFragment);
                ft.commit();
            }
        });

        return convertView;

    }
}
