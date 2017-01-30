package com.oceanstyxx.pubdriver.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.helper.SessionManager;
import com.oceanstyxx.pubdriver.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.oceanstyxx.pubdriver.R.id.btnSignUP;
import static com.oceanstyxx.pubdriver.R.id.email;
import static com.oceanstyxx.pubdriver.R.id.passwordconfirm;
import static com.oceanstyxx.pubdriver.R.id.passwordnew;
import static com.oceanstyxx.pubdriver.R.id.passwordold;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText passwordold;
    private EditText passwordnew;
    private EditText passwordconfirm;
    private Button btnChangePassword;

    OkHttpClient client;
    MediaType JSON;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        // Session manager
        session = new SessionManager(getApplicationContext());
        passwordold = (EditText) findViewById(R.id.passwordold);
        passwordnew = (EditText) findViewById(R.id.passwordnew);
        passwordconfirm = (EditText) findViewById(R.id.passwordconfirm);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        // Login button Click Event
        btnChangePassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String passwordOldValue = passwordold.getText().toString();
                String passwordNewValue = passwordnew.getText().toString();
                String passwordConfirmValue = passwordconfirm.getText().toString();

                // Check for empty data in the form
                if (passwordOldValue.trim().length() > 0 && passwordNewValue.trim().length() > 0 && passwordConfirmValue.trim().length() > 0) {
                    // login user
                    if(passwordNewValue.trim().equals(passwordConfirmValue.trim())){
                        changePassword(passwordOldValue,passwordNewValue,passwordConfirmValue);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "New password and confirm password not matching.", Toast.LENGTH_LONG)
                                .show();
                    }

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

    /**
     * function to verify login details in mysql db
     * */
    private void changePassword(final String emailOldValue,final String emailNewValue,final String emailConfirmValue) {
        String[] myTaskParams = { emailOldValue, emailNewValue, emailConfirmValue};
        ChangePasswordPostTask task = new ChangePasswordPostTask();
        task.execute(myTaskParams);
    }


    public class ChangePasswordPostTask extends AsyncTask<String, String, String> {
        private Exception exception;

        protected String doInBackground(String... params) {
            try {

                Integer customerId = session.getCustomerId();
                String passwordOldValue = params[0];
                String passwordNewValue = params[1];
                String passwordConfirmValue = params[2];

                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                manJson.put("customerid", customerId);
                manJson.put("current_password", passwordOldValue);
                manJson.put("new_password", passwordNewValue);
                manJson.put("confirm_password", passwordConfirmValue);
                json.put("data",manJson);

                String getResponse = post(Const.BASE_URL+"customer/changepassword", json.toString());

                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {

            try {
                JSONObject jObj = new JSONObject(getResponse);

                String code = jObj.getString("code");
                //boolean error = jObj.getBoolean("error");
                if (code.equals("200") ) {

                    session.setLogin(false);
                    // Launch login activity
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
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

}
