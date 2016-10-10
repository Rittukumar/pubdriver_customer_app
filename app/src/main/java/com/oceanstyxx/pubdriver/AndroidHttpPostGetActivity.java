package com.oceanstyxx.pubdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.oceanstyxx.pubdriver.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AndroidHttpPostGetActivity extends AppCompatActivity {

        OkHttpClient client;
        MediaType JSON;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);

            client = new OkHttpClient();
            JSON = MediaType.parse("application/json; charset=utf-8");
        }

        public void makeGetRequest(View v) throws IOException {
            GetTask task = new GetTask();
            task.execute();
        }

        public class GetTask extends AsyncTask<Void, Void, String> {
            private Exception exception;

            protected String doInBackground(Void... params) {
                try {
                    String getResponse = get("http://52.220.4.248/adminportal/public/v1/admin/getAllDrivers");
                    return getResponse;
                } catch (Exception e) {
                    this.exception = e;
                    return null;
                }
            }

            protected void onPostExecute(String getResponse) {
                System.out.println(getResponse);
            }

            public String get(String url) throws IOException {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            }
        }

        public void makePostRequest(View v) throws IOException {
            PostTask task = new PostTask();
            task.execute();
        }

        public class PostTask extends AsyncTask<Void, Void, String> {
            private Exception exception;

            protected String doInBackground(Void... params) {
                try {
                    String getResponse = post("http://52.220.4.248/adminportal/public/v1/customer/signin", "{\"data\":{\"cred\":\"t11111111111@test.com\",\"password\":\"test11111111\"}}");
                    return getResponse;
                } catch (Exception e) {
                    this.exception = e;
                    return null;
                }
            }

            protected void onPostExecute(String getResponse) {
                System.out.println(getResponse);
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
        public String bowlingJson(String player1, String player2) {
            return "{'winCondition':'HIGH_SCORE',"
                    + "'name':'Bowling',"
                    + "'round':4,"
                    + "'lastSaved':1367702411696,"
                    + "'dateStarted':1367702378785,"
                    + "'players':["
                    + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                    + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                    + "]}";
        }

}
