package com.dhakaregency;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakaregency.quickkot.R;

import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class kot_confirmation extends AppCompatActivity {

    TextView textView;
    Button buttonBackToTable;
    String userid;
    String moduleid;
    String isEditMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kot_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.txtKOTFinalNumber);
        buttonBackToTable= (Button) findViewById(R.id.btnBackToTable);



        Bundle b = getIntent().getExtras();
        userid= b.getString("userid");
        moduleid=b.getString("moduleId");
        isEditMode= b.getString("isedit");

    String kotnumber =b.getString("kot");

        textView.setText(kotnumber );


        buttonBackToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),tablechoise.class);
                Bundle bundle=new Bundle();
                bundle.putString("moduleId",moduleid);
                bundle.putString("userid",userid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if (Integer.parseInt(isEditMode)==0) {
            PrintKOT printKOT = new PrintKOT();
            printKOT.execute(kotnumber);
        }
        else
        {
            ReprintKot reprintKot=new ReprintKot();
            reprintKot.execute(kotnumber);
        }
    }
    public class PrintKOT extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String str = "http://192.168.99.12:8080/AuthService.svc/Print";
            String response = "";
            String aa="";
            URL url = null;
            try {
                url = new URL(str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

String queueno=params[0].toString();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("queueno").value(queueno)
                        .endObject();

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    outputStreamWriter.write(userJson.toString());

                    outputStreamWriter.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    aa=line;
                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
    public class ReprintKot extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String str = "http://192.168.99.12:8080/AuthService.svc/RePrint";
            String response = "";
            String aa="";
            URL url = null;
            try {
                url = new URL(str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String queueno=params[0].toString();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("queueno").value(queueno)
                        .endObject();

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    outputStreamWriter.write(userJson.toString());

                    outputStreamWriter.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    aa=line;
                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}
