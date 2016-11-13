package com.oceanstyxx.pubdriver.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.oceanstyxx.pubdriver.AndroidHttpPostGetActivity;
import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.helper.SQLiteHandler;
import com.oceanstyxx.pubdriver.helper.SessionManager;
import com.oceanstyxx.pubdriver.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.id;
import static com.oceanstyxx.pubdriver.R.id.btnLinkToForgotScreen;

public class LoginActivity extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkToForgotScreen;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private CheckBox checkBoxRemember;

    OkHttpClient client;
    MediaType JSON;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkToForgotScreen = (Button) findViewById(R.id.btnLinkToForgotScreen);
        checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedInRemember()) {
            checkBoxRemember.setChecked(true);
        }
        else {
            checkBoxRemember.setChecked(true);
        }


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        // Link to Forgot Password Screen
        btnLinkToForgotScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        Log.w("pubdrive"," Inside the checkLogin - "+email+" "+password);

        String[] myTaskParams = { email, password };
        LoginPostTask task = new LoginPostTask();
        task.execute(myTaskParams);
    }

    public class LoginPostTask extends AsyncTask<String, String, String> {
        private Exception exception;

        protected String doInBackground(String... params) {
            try {

                String email = params[0];
                String password = params[1];

                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                manJson.put("cred", email);
                manJson.put("password", password);
                json.put("data",manJson);

                String getResponse = post(Const.BASE_URL+"customer/signin", json.toString());

                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {

            System.out.println(getResponse);
            session.setLogin(true);

            try {
                JSONObject jObj = new JSONObject(getResponse);

                String code = jObj.getString("code");
                Log.d(TAG, "Register status: " + code);
                //boolean error = jObj.getBoolean("error");
                if (code.equals("200") ) {

                    // User successfully stored in MySQL
                    // Now store the user in sqlite
                        String uid = "1111";//jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("customer");
                        String customerId = user.getString("id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, phone,uid, created_at);

                        session.setKeyCustomerId(Integer.parseInt(customerId));

                        if(checkBoxRemember.isChecked()) {
                            session.setLoginRemember(true);
                        }
                        else {
                            session.setLoginRemember(false);
                        }

                        // Launch login activity
                        Intent i = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(i);
                        finish();
                } else {
                    hideDialog();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
