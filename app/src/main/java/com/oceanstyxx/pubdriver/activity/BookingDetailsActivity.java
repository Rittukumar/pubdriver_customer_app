package com.oceanstyxx.pubdriver.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.helper.SessionManager;
import com.oceanstyxx.pubdriver.model.Billing;
import com.oceanstyxx.pubdriver.model.BookingStatus;
import com.oceanstyxx.pubdriver.model.Driver;
import com.oceanstyxx.pubdriver.model.InvoiceData;
import com.oceanstyxx.pubdriver.model.Invoices;
import com.oceanstyxx.pubdriver.model.OtherVenue;
import com.oceanstyxx.pubdriver.utils.Const;
import com.oceanstyxx.pubdriver.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import static com.oceanstyxx.pubdriver.R.id.bookingEndTime;
import static com.oceanstyxx.pubdriver.R.id.bookingFromPub;
import static com.oceanstyxx.pubdriver.R.id.bookingFromPubTitle;
import static com.oceanstyxx.pubdriver.R.id.bookingNumber;
import static com.oceanstyxx.pubdriver.R.id.bookingStartTime;
import static com.oceanstyxx.pubdriver.R.id.bookingStatus;
import static com.oceanstyxx.pubdriver.R.id.bookingTotal;
import static com.oceanstyxx.pubdriver.R.id.bookingTotalTravelTime;
import static com.oceanstyxx.pubdriver.R.id.bookingTravelTime;
import static com.oceanstyxx.pubdriver.R.id.btnLogin;
import static com.oceanstyxx.pubdriver.R.id.checkBoxRemember;
import static com.oceanstyxx.pubdriver.R.id.driverName;
import static com.oceanstyxx.pubdriver.R.id.email;
import static com.oceanstyxx.pubdriver.R.id.licenceNumber;
import static com.oceanstyxx.pubdriver.R.id.mobileNumber;

public class BookingDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BookingDetailsActivity";


    private SessionManager session;
    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;

    OkHttpClient client;
    MediaType JSON;

    private LoadBookingStatusTask loadBookingStatusTask = null;

    private TextView textViewBookingDate;
    private TextView textViewBookingNumber;
    private TextView textViewBookingFromPub;
    private TextView textViewBookingFrom;
    private TextView textViewBookingTravelTime;
    private TextView textViewBookingStartTime;
    private TextView textViewBookingEndTime;
    private TextView textViewBookingTotalTravelTime;
    private TextView textViewDriverName;
    private TextView textViewMobileNumber;
    private TextView textViewLicenceNumber;
    private ImageView imageViewDriverPhoto;
    private TextView textViewBookingTotal;
    private TextView textViewInvoiceNumber;
    private Button btnCancelDrive;

    BitmapWorkerTask bitmapWorkerTask;

    private String driveId;

    private int dimensionInPixel = 80;

    BookingStatus bookingStatus;

    private String strTravelTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        driveId = getIntent().getExtras().getString("driveid");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        session = new SessionManager(getApplicationContext());

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
        btnCancelDrive = (Button) findViewById(R.id.btnCancelDrive);
        textViewBookingDate = (TextView)findViewById(R.id.bookingDate);
        textViewBookingNumber = (TextView)findViewById(R.id.bookingNumber);
        textViewBookingFromPub = (TextView)findViewById(R.id.bookingFromPub);
        textViewBookingFrom = (TextView)findViewById(R.id.bookingFrom);

        textViewBookingTravelTime = (TextView)findViewById(R.id.bookingTravelTime);
        textViewBookingStartTime =(TextView)findViewById(R.id.bookingStartTime);
        textViewBookingEndTime =(TextView)findViewById(R.id.bookingEndTime);
        textViewBookingTotalTravelTime =(TextView)findViewById(R.id.bookingTotalTravelTime);
        textViewDriverName = (TextView)findViewById(R.id.driverName);
        textViewMobileNumber = (TextView)findViewById(R.id.mobileNumber);
        textViewLicenceNumber = (TextView)findViewById(R.id.licenceNumber);
        imageViewDriverPhoto = (ImageView)findViewById(R.id.driverPhoto);
        textViewBookingTotal = (TextView)findViewById(R.id.bookingTotal);
        textViewInvoiceNumber = (TextView)findViewById(R.id.invoiceNumber);

        bitmapWorkerTask = new BitmapWorkerTask(imageViewDriverPhoto);

        mProgressBar = new ProgressDialog(this);

        // setup the table
        mTableLayout = (TableLayout) findViewById(R.id.tableInvoices);

        mTableLayout.setStretchAllColumns(true);

        loadBookingStatusTask = new LoadBookingStatusTask();
        loadBookingStatusTask.execute(driveId);

        btnCancelDrive.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String[] myTaskParams = { driveId };
                CancelDrivePostTask task = new CancelDrivePostTask();
                task.execute(myTaskParams);
            }

        });
    }


    public class CancelDrivePostTask extends AsyncTask<String, String, String> {
        private Exception exception;
        ProgressDialog progDailog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progDailog = new ProgressDialog(BookingDetailsActivity.this);
            progDailog.setMessage("Cancelling the drive...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        protected String doInBackground(String... params) {
            try {

                String drive_id = params[0];

                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                manJson.put("drive_id", drive_id);
                json.put("data",manJson);

                String getResponse = post(Const.BASE_URL+"customer/cancelBookingRequest", json.toString());
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {

            if (progDailog.isShowing()) {
                progDailog.dismiss();
            }

            try {
                JSONObject jObj = new JSONObject(getResponse);

                String code = jObj.getString("code");
                Log.d(TAG, "Register status: " + code);
                //boolean error = jObj.getBoolean("error");
                if (code.equals("200") ) {

                    // Launch login activity
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    i.putExtra("ACTION_TYPE", "CANCELDRIVE");
                    startActivity(i);
                    finish();
                } else {
                    // Error occurred in registration. Get the error
                    // message
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String post(String url, String json) throws IOException {

            Log.w("pubdriver", "json request "+json);

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }


    public class LoadBookingStatusTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(BookingDetailsActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        protected String doInBackground(String... params) {
            try {
                String driverId = params[0];
                String getResponse = get(Const.BASE_URL+"customer/bookingStatusBasedOnDriveId/"+driverId);
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {
            if (progDailog.isShowing()) {
                progDailog.dismiss();
            }

            try {

                JSONArray jsonArray =  new JSONArray(getResponse);

                for (int i=0; i<jsonArray.length(); i++) {
                    //JSONObject jObj = new JSONObject(jsonArray.getString(i));
                    bookingStatus = new Gson().fromJson(jsonArray.getString(i), BookingStatus.class);

                    String status = bookingStatus.getStatus();

                    if(status.equals("Assigned") && status.equals("Requested")){
                        btnCancelDrive.setVisibility(View.VISIBLE);
                    }
                    textViewBookingDate.setText(bookingStatus.getBooking_date_time());
                    textViewBookingNumber.setText(bookingStatus.getDrive_code());

                    String pickUpSrc = bookingStatus.getPickup_src();
                    if(pickUpSrc != null && pickUpSrc.equalsIgnoreCase("Other")) {
                        OtherVenue otherVenue = bookingStatus.getOthervenue();
                        textViewBookingFrom.setText(otherVenue.getAddress());
                    }
                    else {
                        textViewBookingFromPub.setText(bookingStatus.getPub().getPub_name());
                        textViewBookingFrom.setText(bookingStatus.getPub().getAddress());
                    }


                    textViewBookingStartTime.setText(bookingStatus.getDrive_start_time());
                    textViewBookingEndTime.setText(bookingStatus.getDrive_end_time());
                    textViewBookingTotalTravelTime.setText(bookingStatus.getTotal_travel_time());
                    String profile_image = null;
                    if(!status.equalsIgnoreCase("Requested")) {
                        textViewDriverName.setText(bookingStatus.getDriver().getFirst_name() + " " + bookingStatus.getDriver().getLast_name());
                        textViewMobileNumber.setText(bookingStatus.getDriver().getPhone_number());
                        textViewLicenceNumber.setText(bookingStatus.getDriver().getLicence_no());
                        textViewBookingTotal.setText(bookingStatus.getTotal_drive_rate());
                        profile_image = bookingStatus.getDriver().getProfile_image();
                    }

                    textViewInvoiceNumber.setText(bookingStatus.getInvoice_no());

                    int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                    imageViewDriverPhoto.getLayoutParams().height = dimensionInDp;
                    imageViewDriverPhoto.getLayoutParams().width = dimensionInDp;
                    imageViewDriverPhoto.requestLayout();

                    if(profile_image == null ){
                        imageViewDriverPhoto.setImageResource(R.drawable.avatar);
                    }
                    else {
                        String profileImageUrl = Const.IMAGE_URL+profile_image;
                        bitmapWorkerTask.execute(profileImageUrl);
                    }

                }

                loadData();
                loadTravelTime();

                textViewBookingTravelTime.setText(strTravelTime);
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


    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String data;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            try {
                return BitmapFactory.decodeStream((InputStream) new URL(data)
                        .getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    public boolean loadImageFromURL(String fileUrl,
                                    ImageView iv){
        try {

            URL myFileUrl = new URL (fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            iv.setImageBitmap(BitmapFactory.decodeStream(is));

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void loadTravelTime(){
        String sTravelTime = bookingStatus.getDrive_start_time();
        String[] splited = sTravelTime.split("\\s+");

        try {

            boolean isBetween7to10 = false;
            boolean isBetween17to21 = false;
            boolean isBetween10to17 = false;
            boolean isBetween21to7 = false;

            String string7 = "07:00:00";
            Date time7 = new SimpleDateFormat("HH:mm:ss").parse(string7);
            Calendar calendar7 = Calendar.getInstance();
            calendar7.setTime(time7);

            String string10 = "10:00:00";
            Date time10 = new SimpleDateFormat("HH:mm:ss").parse(string10);
            Calendar calendar10 = Calendar.getInstance();
            calendar10.setTime(time10);

            String string17 = "17:00:00";
            Date time17 = new SimpleDateFormat("HH:mm:ss").parse(string17);
            Calendar calendar17 = Calendar.getInstance();
            calendar17.setTime(time17);

            String string21 = "21:00:00";
            Date time21 = new SimpleDateFormat("HH:mm:ss").parse(string21);
            Calendar calendar21 = Calendar.getInstance();
            calendar21.setTime(time21);

            String string7Next = "07:00:00";
            Date time7Next = new SimpleDateFormat("HH:mm:ss").parse(string7Next);
            Calendar calendar7Next = Calendar.getInstance();
            calendar7Next.setTime(time7Next);
            calendar7Next.add(Calendar.DATE, 1);

            String someRandomTime = splited[1];
            Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);

            Date x = calendar3.getTime();
            if (x.after(calendar7.getTime()) && x.before(calendar10.getTime())) {
                isBetween7to10 = true;
            }

            if (x.after(calendar17.getTime()) && x.before(calendar21.getTime())) {
                isBetween17to21 = true;
            }

            if (x.after(calendar10.getTime()) && x.before(calendar17.getTime())) {
                isBetween10to17 = true;
            }

            if (x.after(calendar21.getTime()) && x.before(calendar7Next.getTime())) {
                isBetween21to7 = true;
            }


            if(isBetween7to10 || isBetween17to21){
                strTravelTime = "Peak Hours";
            }

            if(isBetween10to17){
                strTravelTime = "Normal Hours";
            }

            if(isBetween21to7){
                strTravelTime = "Night Hours";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void loadData() {

        ArrayList<Billing> billing = bookingStatus.getBilling();
        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize =0, mediumTextSize = 0;

        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        Invoices invoices = new Invoices();
        InvoiceData[] data = invoices.getInvoices(billing);

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
                textSpacer = new TextView(this);
                textSpacer.setText("");

            }
            // data columns
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);
            tv.setWidth(200);
            tv.setPadding(5, 5, 5, 5);
            if (i == -1) {
                tv.setText("BILL DETAILS");
                //tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            } else {
                //tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(row.chargeDetails);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }
            tv.setTextColor(Color.parseColor("#000000"));

            final TextView tv2 = new TextView(this);
            if (i == -1) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv2.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

            tv2.setPadding(5, 10, 5, 10);
            if (i == -1) {
                tv2.setText("QTY");
                tv2.setTextColor(Color.parseColor("#000000"));
                //tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv2.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }else {
                //tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(String.valueOf(row.qty));
                tv2.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }


            final LinearLayout layCustomer = new LinearLayout(this);
            layCustomer.setOrientation(LinearLayout.VERTICAL);
            layCustomer.setPadding(5, 5, 5, 10);
            //layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 5, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 5, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }

            tv3.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);


            if (i == -1) {
                tv3.setText("RATE");
                //tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv3.setTextColor(Color.parseColor("#000000"));
            } else {
                //tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
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

            final LinearLayout layAmounts = new LinearLayout(this);
            layAmounts.setOrientation(LinearLayout.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(5, 10, 5, 10);
            layAmounts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));



            final TextView tv4 = new TextView(this);
            if (i == -1) {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setPadding(5, 5, 5, 5);
                //layAmounts.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setPadding(5, 5, 5, 5);
                //layAmounts.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }

            tv4.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

            if (i == -1) {
                tv4.setTextColor(Color.parseColor("#000000"));
                tv4.setText("TOTAL");
               // tv4.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
            } else {
                //tv4.setBackgroundColor(Color.parseColor("#ffffff"));
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
            final TableRow tr = new TableRow(this);
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
                final TableRow trSep = new TableRow(this);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
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
