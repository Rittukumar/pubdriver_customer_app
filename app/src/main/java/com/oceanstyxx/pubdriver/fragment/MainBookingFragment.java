package com.oceanstyxx.pubdriver.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.activity.MainActivity;
import com.oceanstyxx.pubdriver.adapter.CarTypeSpinAdapter;
import com.oceanstyxx.pubdriver.adapter.PubAdapter;
import com.oceanstyxx.pubdriver.adapter.PubSpinAdapter;
import com.oceanstyxx.pubdriver.helper.SessionManager;
import com.oceanstyxx.pubdriver.model.CarType;
import com.oceanstyxx.pubdriver.model.DriverRequest;
import com.oceanstyxx.pubdriver.model.Pub;
import com.oceanstyxx.pubdriver.utils.Const;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.oceanstyxx.pubdriver.R.id.pickupaddress;
import static com.oceanstyxx.pubdriver.R.id.pickupvenue;
import static com.oceanstyxx.pubdriver.R.id.textViewCarTypeTitle;
import static com.oceanstyxx.pubdriver.R.id.textViewTransmissionTitle;


public class MainBookingFragment extends Fragment implements MainFragmentInterface {

    private static final String TAG = MainBookingFragment.class.getSimpleName();

    OkHttpClient client;
    MediaType JSON;
    private View rootView;

    /*private Spinner pubSpinner;
    private PubSpinAdapter pubSpinnerAdapter;*/
    private Pub selectedPub;
    private PubAdapter pubAdapter;
    AutoCompleteTextView pubAutoComplete;

    /*private Spinner carTypeSpinner;
    private CarTypeSpinAdapter carTypeSpinAdapter;
    private CarType selectedCarType;*/

    private Button btnRequestForBooking;
    private RadioGroup radioTransmissionGroup;
    private RadioButton radioTransmissionButton;

    private static ProgressDialog pleaseWaitDialog;
    private static ProgressDialog pDialog;

    CheckBox checkBoxTermsConditions;

    private SessionManager session;

    RelativeLayout relativelayoutcartype;

    private int selectedCarTypeId = 1;

    private EditText pickupvenue;

    private EditText pickupaddress;

    private TextView textViewCarTypeTitle;
    private TextView textViewTransmissionTitle;

    Typeface Fonts;
    Typeface FontsBold;

    RadioButton radioButtonManual;
    RadioButton radioButtonAutomatic;

    TextView textViewHatchBack;
    TextView textViewSUV;
    TextView textViewLuxury;
    TextView textViewSedan;
    TextView textViewMPV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_main_booking,
                container, false);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/fonts.ttf");
        FontsBold = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/fontsbold.ttf");

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
        // Session manager
        session = new SessionManager(getActivity());

        RelativeLayout layout = (RelativeLayout)rootView.findViewById(R.id.relativelayoutcartype);
        pubAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.autocomplete_pub);
        pickupvenue = (EditText)rootView.findViewById(R.id.pickupvenue);
        pickupaddress = (EditText)rootView.findViewById(R.id.pickupaddress);
        textViewCarTypeTitle = (TextView)rootView.findViewById(R.id.textViewCarTypeTitle);
        textViewCarTypeTitle.setTypeface(Fonts);
        textViewTransmissionTitle = (TextView)rootView.findViewById(R.id.textViewTransmissionTitle);
        textViewTransmissionTitle.setTypeface(Fonts);

        radioButtonManual = (RadioButton) rootView.findViewById(R.id.radioButtonManual);
        radioButtonAutomatic = (RadioButton) rootView.findViewById(R.id.radioButtonAutomatic);

        pubAutoComplete.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectedPub = (Pub) arg0.getAdapter().getItem(arg2);
                /*Toast.makeText(getActivity(),
                        "Clicked " + arg2 + " name: " + selectedPub.getId(),
                        Toast.LENGTH_SHORT).show();*/
                Integer selectedId = selectedPub.getId();

                if(selectedId != null && selectedId == 500) {
                    pickupvenue.setVisibility(View.VISIBLE);
                    pickupaddress.setVisibility(View.VISIBLE);
                }
            }
        });

        final TextView textViewEmpty1 = new TextView(getActivity());
        int textViewEmptyId1 = 0;
        textViewEmptyId1 = textViewEmptyId1 +1;
        textViewEmpty1.setId(textViewEmptyId1);
        textViewEmpty1.setPadding(30,30,30,30);
        final RelativeLayout.LayoutParams paramsEmpty1 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        //paramsEmpty.addRule(RelativeLayout.RIGHT_OF, textViewSUVId1);
        layout.addView(textViewEmpty1, paramsEmpty1);

        textViewSUV = new TextView(getActivity());
        int textViewSUVId = textViewEmptyId1 +1;
        textViewSUV.setId(textViewSUVId);
        textViewSUV.setText("SUV");
        textViewSUV.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewSUV.setTextSize(15);
        textViewSUV.setTextColor(Color.WHITE);
        textViewSUV.setTypeface(Typeface.DEFAULT_BOLD);
        textViewSUV.setPadding(50,15,50,15);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        textViewSUV.setLayoutParams(layoutParams);
        final RelativeLayout.LayoutParams paramsSUV =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsSUV.addRule(RelativeLayout.RIGHT_OF, textViewEmptyId1);
        layout.addView(textViewSUV, paramsSUV);


        final TextView textViewEmpty2 = new TextView(getActivity());
        int textViewEmptyId2 = textViewSUVId +1;
        textViewEmpty2.setId(textViewEmptyId2);
        textViewEmpty2.setPadding(20,20,20,20);
        final RelativeLayout.LayoutParams paramsEmpty2 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsEmpty2.addRule(RelativeLayout.RIGHT_OF, textViewSUVId);
        layout.addView(textViewEmpty2, paramsEmpty2);

        textViewMPV = new TextView(getActivity());
        int textViewMPVId = textViewEmptyId2 +1;
        textViewMPV.setId(textViewMPVId);
        textViewMPV.setText("MPV");
        textViewMPV.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewMPV.setTextSize(15);
        textViewMPV.setTextColor(Color.WHITE);
        textViewMPV.setTypeface(Typeface.DEFAULT_BOLD);
        textViewMPV.setPadding(50,15,50,15);
        textViewMPV.setLayoutParams(layoutParams);
        final RelativeLayout.LayoutParams paramsMPV =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsMPV.addRule(RelativeLayout.RIGHT_OF, textViewEmptyId2);
        layout.addView(textViewMPV, paramsMPV);

        final TextView textViewEmpty3 = new TextView(getActivity());
        int textViewEmptyId3 = textViewMPVId +1;
        textViewEmpty3.setId(textViewEmptyId3);
        textViewEmpty3.setPadding(20,20,20,20);
        final RelativeLayout.LayoutParams paramsEmpty3 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsEmpty3.addRule(RelativeLayout.RIGHT_OF, textViewMPVId);
        layout.addView(textViewEmpty3, paramsEmpty3);

        textViewSedan = new TextView(getActivity());
        int textViewSedanId = textViewEmptyId3 +1;
        textViewSedan.setId(textViewSedanId);
        textViewSedan.setText("SEDAN");
        textViewSedan.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewSedan.setTextSize(15);
        textViewSedan.setTextColor(Color.WHITE);
        textViewSedan.setTypeface(Typeface.DEFAULT_BOLD);
        textViewSedan.setPadding(50,15,50,15);
        textViewSedan.setLayoutParams(layoutParams);
        final RelativeLayout.LayoutParams paramsSedan =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsSedan.addRule(RelativeLayout.RIGHT_OF, textViewEmptyId3);
        layout.addView(textViewSedan, paramsSedan);

        final TextView textViewEmptyrow = new TextView(getActivity());
        int textViewEmptyIdRow = 20 +1;
        textViewEmptyrow.setId(textViewEmptyIdRow);
        //textViewEmptyrow.setPadding(2,2,2,2);
        final RelativeLayout.LayoutParams paramsEmptyRow =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsEmptyRow.addRule(RelativeLayout.BELOW, textViewSUVId);
        layout.addView(textViewEmptyrow, paramsEmptyRow);


        final TextView textViewEmpty4 = new TextView(getActivity());
        int textViewEmptyId4 = textViewSedanId +1;
        textViewEmpty4.setId(textViewEmptyId4);
        textViewEmpty4.setPadding(20,20,40,20);
        final RelativeLayout.LayoutParams paramsEmpty4 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsEmpty4.addRule(RelativeLayout.BELOW, textViewEmptyIdRow);
        layout.addView(textViewEmpty4, paramsEmpty4);

        textViewHatchBack = new TextView(getActivity());
        int textViewHatchBackId = textViewEmptyId4 +1;
        textViewHatchBack.setId(textViewHatchBackId);
        textViewHatchBack.setText("HATCH BACK");
        textViewHatchBack.setBackgroundColor(Color.parseColor("#00bfff"));
        textViewHatchBack.setTextSize(15);
        textViewHatchBack.setTextColor(Color.WHITE);
        textViewHatchBack.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHatchBack.setPadding(60,15,60,15);
        textViewHatchBack.setLayoutParams(layoutParams);
        final RelativeLayout.LayoutParams paramsHatchBack =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsHatchBack.addRule(RelativeLayout.RIGHT_OF, textViewEmptyId4);
        paramsHatchBack.addRule(RelativeLayout.BELOW, textViewEmptyIdRow);
        layout.addView(textViewHatchBack, paramsHatchBack);


        final TextView textViewEmpty5 = new TextView(getActivity());
        int textViewEmptyId5 = textViewHatchBackId +1;
        textViewEmpty5.setId(textViewEmptyId5);
        textViewEmpty5.setPadding(20,20,20,20);
        final RelativeLayout.LayoutParams paramsEmpty5 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsEmpty5.addRule(RelativeLayout.RIGHT_OF, textViewHatchBackId);
        layout.addView(textViewEmpty5, paramsEmpty5);

        textViewLuxury = new TextView(getActivity());
        int textViewLuxuryId = textViewEmptyId5 +1;
        textViewLuxury.setId(textViewLuxuryId);
        textViewLuxury.setText("Luxury");
        textViewLuxury.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewLuxury.setTextSize(15);
        textViewLuxury.setTextColor(Color.WHITE);
        textViewLuxury.setTypeface(Typeface.DEFAULT_BOLD);
        textViewLuxury.setPadding(75,15,75,15);
        textViewLuxury.setLayoutParams(layoutParams);
        final RelativeLayout.LayoutParams paramsLuxury =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLuxury.addRule(RelativeLayout.RIGHT_OF, textViewEmptyId5);
        paramsLuxury.addRule(RelativeLayout.BELOW, textViewEmptyIdRow);
        layout.addView(textViewLuxury, paramsLuxury);


        textViewSUV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCarTypeId = 4;
                textViewSUV.setBackgroundColor(Color.parseColor("#00bfff"));

                textViewMPV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewSedan.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewHatchBack.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewLuxury.setBackgroundColor(Color.parseColor("#b3b3b3"));
            }
        });

        textViewMPV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCarTypeId = 3;
                textViewMPV.setBackgroundColor(Color.parseColor("#00bfff"));

                textViewSUV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewSedan.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewHatchBack.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewLuxury.setBackgroundColor(Color.parseColor("#b3b3b3"));
            }
        });

        textViewSedan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCarTypeId = 2;
                textViewSedan.setBackgroundColor(Color.parseColor("#00bfff"));

                textViewSUV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewMPV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewHatchBack.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewLuxury.setBackgroundColor(Color.parseColor("#b3b3b3"));
            }
        });

        textViewHatchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCarTypeId = 1;
                textViewHatchBack.setBackgroundColor(Color.parseColor("#00bfff"));

                textViewSUV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewMPV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewSedan.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewLuxury.setBackgroundColor(Color.parseColor("#b3b3b3"));
            }
        });

        textViewLuxury.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCarTypeId = 5;
                textViewLuxury.setBackgroundColor(Color.parseColor("#00bfff"));

                textViewSUV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewMPV.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewSedan.setBackgroundColor(Color.parseColor("#b3b3b3"));
                textViewHatchBack.setBackgroundColor(Color.parseColor("#b3b3b3"));
            }
        });

        /*Random rnd = new Random();
        int prevTextViewId = 0;
        for(int i = 0; i < 10; i++)
        {
            final TextView textView = new TextView(getActivity());
            textView.setText("Text "+i);
            textView.setTextColor(rnd.nextInt() | 0xff000000);

            int curTextViewId = prevTextViewId + 1;
            textView.setId(curTextViewId);
            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.BELOW, prevTextViewId);
            textView.setLayoutParams(params);

            prevTextViewId = curTextViewId;
            layout.addView(textView, params);
        }*/


        checkBoxTermsConditions=(CheckBox)rootView.findViewById(R.id.checkBoxTermsConditions);

        //pubSpinner = (Spinner) rootView.findViewById(R.id.pubs_spinner);
        //carTypeSpinner = (Spinner) rootView.findViewById(R.id.cartype_spinner);

        /*pubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                selectedPub = pubSpinnerAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });*/

        /*CarType[] carTypes = new CarType[6];
        carTypes[0] = new CarType();
        carTypes[0].setId(1);
        carTypes[0].setName("SUV");

        carTypes[1] = new CarType();
        carTypes[1].setId(2);
        carTypes[1].setName("MPV");

        carTypes[2] = new CarType();
        carTypes[2].setId(3);
        carTypes[2].setName("SEDAN");

        carTypes[3] = new CarType();
        carTypes[3].setId(4);
        carTypes[3].setName("HATCHBACK");

        carTypes[4] = new CarType();
        carTypes[4].setId(5);
        carTypes[4].setName("LUXURY");

        carTypes[5] = new CarType();
        carTypes[5].setId(0);
        carTypes[5].setName("Select Car Type");

        carTypeSpinAdapter = new CarTypeSpinAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                carTypes);
        carTypeSpinner = (Spinner) rootView.findViewById(R.id.cartype_spinner);
        carTypeSpinner.setAdapter(carTypeSpinAdapter);
        carTypeSpinner.setSelection((carTypeSpinAdapter.getCount()-1));

        carTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                selectedCarType = carTypeSpinAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });*/

        radioTransmissionGroup=(RadioGroup)rootView.findViewById(R.id.radioGroupTransmission);


        btnRequestForBooking=(Button)rootView.findViewById(R.id.btnRequestForBooking);

        btnRequestForBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radioTransmissionGroup.getCheckedRadioButtonId();
                radioTransmissionButton=(RadioButton)rootView.findViewById(selectedId);

                //checkBoxTermsConditions.isChecked()

                //Toast.makeText(getActivity(),radioTransmissionButton.getText(),Toast.LENGTH_SHORT).show();

                DriverRequest driverRequest = new DriverRequest();
                driverRequest.setPubId(selectedPub.getId());
                driverRequest.setCarTypeId(selectedCarTypeId); //selectedCarType.getId());

                if((radioTransmissionButton.getText().toString()).equalsIgnoreCase("AUTOMATIC")){
                    driverRequest.setTransmissionId(1);
                }
                else {
                    driverRequest.setTransmissionId(2);
                }

                if(checkBoxTermsConditions.isChecked()){
                    driverRequest.setTermsAndConditions(1);
                }
                else {
                    driverRequest.setTermsAndConditions(0);
                }

                String pickupvenueValue = pickupvenue.getText().toString();
                if(pickupvenueValue != null && !pickupvenueValue.isEmpty()) {
                    driverRequest.setPickupvenue(pickupvenueValue);
                }

                String pickupaddressValue = pickupaddress.getText().toString();
                if(pickupaddressValue != null && !pickupaddressValue.isEmpty()) {
                    driverRequest.setPickupaddress(pickupaddressValue);
                }

                driverRequest.setOrderSrc("Online Order");

                RequestBookingTask requestBookingTask = new RequestBookingTask();
                requestBookingTask.execute(driverRequest);
            }
        });

        LoadPubsTask loadPubsTask = new LoadPubsTask();
        loadPubsTask.execute();

        return rootView;

    }

    @Override
    public void fragmentBecameVisible() {
        System.out.println("TestFragment");
        pubAutoComplete.setText("");
        radioButtonManual.setChecked(false);
        radioButtonAutomatic.setChecked(false);
        checkBoxTermsConditions.setChecked(false);

        selectedCarTypeId = 1;
        textViewHatchBack.setBackgroundColor(Color.parseColor("#00bfff"));

        textViewSUV.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewMPV.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewSedan.setBackgroundColor(Color.parseColor("#b3b3b3"));
        textViewLuxury.setBackgroundColor(Color.parseColor("#b3b3b3"));
    }

    public class LoadPubsTask extends AsyncTask<Void, Void, String> {
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
                String getResponse = get(Const.BASE_URL+"master/getPubs");
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {
            System.out.println(getResponse);

            try {
                JSONArray jsonArray = new JSONArray(getResponse);
                Pub[] pubs = new Pub[jsonArray.length()];

                List<String> list = new ArrayList<String>();
                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject jObj = new JSONObject(jsonArray.getString(i));
                    Pub pub = new Pub();
                    pub.setId(jObj.getInt("id"));
                    pub.setPub_name(jObj.getString("pub_name"));
                    pubs[i] = pub ;
                }

                Pub defaultPub = new Pub();
                defaultPub.setId(0);
                defaultPub.setPub_name("Select The Pub");
                pubs[jsonArray.length()-1] = defaultPub ;

                ArrayAdapter<Pub> adapter = new ArrayAdapter<Pub>(getActivity(), R.layout.pub_auto, pubs);
                pubAutoComplete.setAdapter(adapter);
                pubAutoComplete.setThreshold(1);

                /*pubSpinnerAdapter = new PubSpinAdapter(getActivity(),android.R.layout.simple_spinner_item,pubs);
                pubSpinnerAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pubSpinner.setAdapter(pubSpinnerAdapter);
                pubSpinner.setSelection((pubSpinnerAdapter.getCount()-1));*/
                //pDialog.dismiss();
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


    public class RequestBookingTask extends AsyncTask<DriverRequest, String, String> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait booking the request");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(DriverRequest... params) {
            try {
                DriverRequest driverRequest = params[0];
                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                manJson.put("driver_code", driverRequest.getDriverCode());
                manJson.put("pub_id", driverRequest.getPubId());
                manJson.put("transmission_id", driverRequest.getTransmissionId());
                manJson.put("car_type_id", driverRequest.getCarTypeId());
                manJson.put("terms_and_cond_accepted", driverRequest.getTermsAndConditions());
                manJson.put("device_id", "0000000003");
                manJson.put("order_src", driverRequest.getOrderSrc());
                manJson.put("pickup_venue", driverRequest.getPickupvenue());
                manJson.put("pickup_address", driverRequest.getPickupaddress());
                Integer customerId = session.getCustomerId();
                manJson.put("customer_id", customerId);


                json.put("data",manJson);

                String getResponse = post(Const.BASE_URL+"customer/sendBookingRequest", json.toString());
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {
            Log.d(TAG, "Driver Request Response: " + getResponse);
            try {
                JSONObject jObj = new JSONObject(getResponse);

                String code = jObj.getString("code");
                Log.d(TAG, "Register status: " + code);
                //boolean error = jObj.getBoolean("error");
                if (code.equals("200") ) {

                    // User successfully stored in MySQL
                    // Now store the user in sqlite
                    /*String uid = jObj.getString("uid");

                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("name");
                    String email = user.getString("email");
                    String phone = user.getString("phone");
                    String created_at = user
                            .getString("created_at");

                    // Inserting row in users table
                    db.addUser(name, email, phone,uid, created_at);*/

                    // Launch login activity
                    pDialog.dismiss();
                    Toast.makeText(getActivity(),
                            "Driver Request added", Toast.LENGTH_LONG).show();

                    TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
                    tabhost.getTabAt(2).select();

                } else {
                    //hideDialog();
                    // Error occurred in registration. Get the error
                    // message
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getActivity(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}
