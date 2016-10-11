package com.oceanstyxx.pubdriver.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.adapter.BookingListCustomAdapter;
import com.oceanstyxx.pubdriver.adapter.CustomAdapter;
import com.oceanstyxx.pubdriver.model.BookingRowItem;
import com.oceanstyxx.pubdriver.model.RowItem;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainBookingListFragment extends ListFragment  {

    String[] menutitles;

    TypedArray menuIcons;

    BookingListCustomAdapter adapter;
    private List<BookingRowItem> rowItems;

    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View V = inflater.inflate(R.layout.fragment_main_booking_list, container, false);
        list = (ListView)V.findViewById(android.R.id.list);

        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // This is important bit
                Fragment bookingDetailsFragment = new BookingDetailsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.parent, bookingDetailsFragment).commit();
            }
        });

        return inflater.inflate(R.layout.fragment_main_booking_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        /*menutitles = getResources().getStringArray(R.array.titles);
        menuIcons = getResources().obtainTypedArray(R.array.icons);

        rowItems = new ArrayList<RowItem>();

        for (int i = 0; i < menutitles.length; i++) {
            RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(
                    i, -1));

            rowItems.add(items);
        }*/

        rowItems = new ArrayList<BookingRowItem>();

        BookingRowItem bookingItem1 = new BookingRowItem();
        bookingItem1.setBookingNumber("123456");
        bookingItem1.setBookingFrom("Koramangala");
        bookingItem1.setBookingStatus("REQUESTED");
        bookingItem1.setDescription("Your Request is in progress.");
        bookingItem1.setBookingDate("06-SEP-2016 10:30 PM");
        bookingItem1.setPrice("RS. 400");

        BookingRowItem bookingItem2 = new BookingRowItem();
        bookingItem2.setBookingNumber("123456");
        bookingItem2.setBookingFrom("Koramangala");
        bookingItem2.setBookingStatus("REQUESTED");
        bookingItem2.setDescription("Your Request is in progress.");
        bookingItem2.setBookingDate("06-SEP-2016 10:30 PM");
        bookingItem2.setPrice("RS. 400");

        rowItems.add(bookingItem1);
        rowItems.add(bookingItem2);

        adapter = new BookingListCustomAdapter(getActivity(), rowItems,this);
        setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);

    }

}
