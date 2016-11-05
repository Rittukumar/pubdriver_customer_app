package com.oceanstyxx.pubdriver.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oceanstyxx.pubdriver.R;
import com.oceanstyxx.pubdriver.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.oceanstyxx.pubdriver.R.id.btnLogin;
import static com.oceanstyxx.pubdriver.R.id.btnSignUP;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnSignUP;

    OkHttpClient client;
    MediaType JSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        btnSignUP = (Button) findViewById(R.id.btnSignUP);

        // Login button Click Event
        btnSignUP.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 ) {
                    // login user
                    forgotPassword(email);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }

    /**
     * function to verify login details in mysql db
     * */
    private void forgotPassword(final String email) {
        Log.w("pubdrive"," Inside the forgotPassword - "+email);

        String[] myTaskParams = { email };
        ForgotPasswordPostTask task = new ForgotPasswordPostTask();
        task.execute(myTaskParams);
    }

    public class ForgotPasswordPostTask extends AsyncTask<String, String, String> {
        private Exception exception;

        protected String doInBackground(String... params) {
            try {

                String email = params[0];

                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                manJson.put("email", email);
                json.put("data",manJson);

                String getResponse = post(Const.BASE_URL+"customer/forgotpassword", json.toString());

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

                    // Launch login activity
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
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
