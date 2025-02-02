package com.oceanstyxx.pubdriver.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.oceanstyxx.pubdriver.adapter.PubSpinAdapter;
import com.oceanstyxx.pubdriver.helper.DateUtility;
import com.oceanstyxx.pubdriver.helper.SessionManager;
import com.oceanstyxx.pubdriver.model.BookingRowItem;
import com.oceanstyxx.pubdriver.model.Pub;
import com.oceanstyxx.pubdriver.model.RowItem;
import com.oceanstyxx.pubdriver.utils.Const;
import android.support.design.widget.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.id.list;

public class MainBookingListFragment extends ListFragment  implements MainFragmentInterface {

    private static final String TAG = MainBookingListFragment.class.getSimpleName();

    String[] menutitles;

    TypedArray menuIcons;

    OkHttpClient client;
    MediaType JSON;
    private View rootView;

    BookingListCustomAdapter adapter;
    private List<BookingRowItem> rowItems;

    private ListView list;

    private static ProgressDialog pleaseWaitDialog;
    private static ProgressDialog pDialog;

    private SessionManager session;

    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View V = inflater.inflate(R.layout.fragment_main_booking_list, container, false);
        list = (ListView)V.findViewById(android.R.id.list);

        FloatingActionButton myFab = (FloatingActionButton)  V.findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
                tabhost.getTabAt(0).select();
            }
        });

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        // Session manager
        session = new SessionManager(getActivity());

        doTimerTask();




        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Fragment bookingDetailsFragment = new BookingDetailsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.parent, bookingDetailsFragment).commit();
            }
        });

        return V;
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

        LoadBookingStatusTask loadBookingStatusTask= new LoadBookingStatusTask();
        loadBookingStatusTask.execute();

    }

    @Override
    public void fragmentBecameVisible() {
        System.out.println("TestFragment");
    }


    public class LoadBookingStatusTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Pubs List");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        protected String doInBackground(Void... params) {
            try {
                Integer customerId = session.getCustomerId();
                String getResponse = get(Const.BASE_URL+"customer/bookingStatus/"+customerId);
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {
            System.out.println(getResponse);

            try {

                if(getResponse != null) {
                    JSONObject jObj = new JSONObject(getResponse);

                    String code = jObj.getString("code");
                    Log.d(TAG, "Register status: " + code);
                    //boolean error = jObj.getBoolean("error");
                    if (code.equals("200")) {

                        JSONArray jsonArray = jObj.getJSONArray("data");


                        rowItems = new ArrayList<BookingRowItem>();

                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObjBookingStatus = new JSONObject(jsonArray.getString(i));
                            BookingRowItem bookingItem = new BookingRowItem();
                            bookingItem.setDriveId(jObjBookingStatus.getString("id"));
                            bookingItem.setBookingNumber(jObjBookingStatus.getString("drive_code"));

                            String bookingStatus = jObjBookingStatus.getString("status");
                            String bookingDescription = null;
                            if (bookingStatus.equals("Requested")) {
                                bookingDescription = "Your Request is in progress. We will get back to you with the driver detail in some time. Thankyou.";
                            } else if (bookingStatus.equals("Assigned")) {
                                bookingDescription = "Driver has been assigned for your request. The driver will arrive within 45 minutes. For more details please click on view detail button.";
                            } else if (bookingStatus.equals("Started")) {
                                bookingDescription = "Your trip has been started. Enjoy the journey.";
                            } else if (bookingStatus.equals("Ended")) {
                                bookingDescription = "Your trip has been ended. Thank you for availing our service.";
                            } else if (bookingStatus.equals("Settled")) {
                                bookingDescription = "Thank you for the payment.";
                            }
                            bookingItem.setDescription(bookingDescription);

                            bookingItem.setBookingStatus(bookingStatus);
                            String bookingDate = DateUtility.formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy", jObjBookingStatus.getString("booking_date_time"));
                            bookingItem.setBookingDate(bookingDate);
                            String totalPrice = jObjBookingStatus.getString("total_drive_rate");
                            if (!totalPrice.equals("null")) {
                                bookingItem.setPrice("RS. " + totalPrice);
                            }


                            String pickUpSrc = jObjBookingStatus.getString("pickup_src");
                            bookingItem.setPickUpSrc(pickUpSrc);
                            if (pickUpSrc.equalsIgnoreCase("Pub")) {
                                JSONObject jObjPub = new JSONObject(jObjBookingStatus.getString("pub"));
                                String pubAddress = jObjPub.getString("pub_name");
                                bookingItem.setBookingFrom(pubAddress);
                            } else {

                                JSONObject jObjOthervenue = new JSONObject(jObjBookingStatus.getString("othervenue"));
                                String venue = jObjOthervenue.getString("venue");
                                String address = jObjOthervenue.getString("address");
                                bookingItem.setVeunue(venue);
                                bookingItem.setBookingFrom(address);
                            }
                            rowItems.add(bookingItem);

                        }

                        adapter = new BookingListCustomAdapter(getActivity(), rowItems);
                        setListAdapter(adapter);

                        //pDialog.dismiss();
                    }
                }
            }
            catch(Exception e ){
                e.printStackTrace();
            }
        }

        public String get(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    public void doTimerTask(){

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("TIMER", "TimerTask run");
                        LoadBookingStatusTask loadBookingStatusTask= new LoadBookingStatusTask();
                        loadBookingStatusTask.execute();
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 0, 15000);  //

    }

    public void stopTask(){

        if(mTimerTask!=null){

            Log.d("TIMER", "timer canceled");
            mTimerTask.cancel();
        }

    }

}
