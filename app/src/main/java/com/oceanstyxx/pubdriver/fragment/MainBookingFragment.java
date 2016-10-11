package com.oceanstyxx.pubdriver.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.oceanstyxx.pubdriver.AndroidHttpPostGetActivity;
import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.activity.MainActivity;
import com.oceanstyxx.pubdriver.activity.RegisterActivity;
import com.oceanstyxx.pubdriver.adapter.CarTypeSpinAdapter;
import com.oceanstyxx.pubdriver.adapter.PubSpinAdapter;
import com.oceanstyxx.pubdriver.model.CarType;
import com.oceanstyxx.pubdriver.model.DriverRequest;
import com.oceanstyxx.pubdriver.model.Pub;
import com.oceanstyxx.pubdriver.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.oceanstyxx.pubdriver.R.id.email;
import static com.oceanstyxx.pubdriver.R.id.phone;


public class MainBookingFragment extends Fragment {

    private static final String TAG = MainBookingFragment.class.getSimpleName();

    OkHttpClient client;
    MediaType JSON;
    private View rootView;

    private Spinner pubSpinner;
    private PubSpinAdapter pubSpinnerAdapter;
    private Pub selectedPub;

    private Spinner carTypeSpinner;
    private CarTypeSpinAdapter carTypeSpinAdapter;
    private CarType selectedCarType;

    private Button btnRequestForBooking;
    private RadioGroup radioTransmissionGroup;
    private RadioButton radioTransmissionButton;

    private static ProgressDialog pleaseWaitDialog;
    private static ProgressDialog pDialog;

    CheckBox checkBoxTermsConditions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_main_booking,
                container, false);

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        checkBoxTermsConditions=(CheckBox)rootView.findViewById(R.id.checkBoxTermsConditions);

        pubSpinner = (Spinner) rootView.findViewById(R.id.pubs_spinner);
        carTypeSpinner = (Spinner) rootView.findViewById(R.id.cartype_spinner);

        pubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                selectedPub = pubSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...
                /*Toast.makeText(getActivity(), "ID: " + selectedPub.getId() + "\nName: " + selectedPub.getPubName(),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        CarType[] carTypes = new CarType[6];
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
                // Here you get the current item (a User object) that is selected by its position
                selectedCarType = carTypeSpinAdapter.getItem(position);
                // Here you can do the action you want to...
                /*Toast.makeText(getActivity(), "ID: " + selectedCarType.getId() + "\nName: " + selectedCarType.getName(),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

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
                driverRequest.setCarTypeId(selectedCarType.getId());

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

                RequestBookingTask requestBookingTask = new RequestBookingTask();
                requestBookingTask.execute(driverRequest);
            }
        });

        LoadPubsTask loadPubsTask = new LoadPubsTask();
        loadPubsTask.execute();

        return rootView;

    }


    public class LoadPubsTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Pubs List");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
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
                    pub.setPubName(jObj.getString("pub_name"));
                    pubs[i] = pub ;
                }

                Pub defaultPub = new Pub();
                defaultPub.setId(0);
                defaultPub.setPubName("Select The Pub");
                pubs[jsonArray.length()-1] = defaultPub ;


                pubSpinnerAdapter = new PubSpinAdapter(getActivity(),android.R.layout.simple_spinner_item,pubs);
                pubSpinnerAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pubSpinner.setAdapter(pubSpinnerAdapter);
                pubSpinner.setSelection((pubSpinnerAdapter.getCount()-1));
                pDialog.dismiss();
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
                    Toast.makeText(getActivity(),
                            "Driver Request added", Toast.LENGTH_LONG).show();
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
